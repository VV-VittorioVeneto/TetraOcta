package com.vv.octa.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vv.octa.modular.ModularLaserArtilleryItem;
import com.vv.octa.util.OctaUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.model.EntityModel;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class RayOfLaserRenderer {
    public static final ResourceLocation BEACON = new ResourceLocation("octa", "textures/entity/ray/beacon_beam.png");
    public static final ResourceLocation TWISTING_GLOW = new ResourceLocation("octa", "textures/entity/ray/twisting_glow.png");

    @SubscribeEvent
    public static void afterLivingRender(RenderLivingEvent.Post<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
        var livingEntity = event.getEntity();
        if  (livingEntity.getMainHandItem().getItem() instanceof ModularLaserArtilleryItem laserArtilleryItem && laserArtilleryItem.isFiring(livingEntity.getMainHandItem())) {
            renderRayOfLaserByItem(livingEntity, laserArtilleryItem, event);
        }

    }

    public static void renderRayOfLaserByItem(LivingEntity entity, ModularLaserArtilleryItem modularLaserArtilleryItem, RenderLivingEvent.Post<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event){
        renderRayOfLaser(entity, event.getPoseStack(), event.getMultiBufferSource(), event.getPartialTick(), modularLaserArtilleryItem.getLaserRGB(entity.getMainHandItem()), modularLaserArtilleryItem.getLaserAlpha());
    }

    public static void renderRayOfLaser(LivingEntity entity, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks, int RGB, float Alpha) {

        poseStack.pushPose();
        poseStack.translate(0, entity.getEyeHeight() * .8f, 0);
        var pose = poseStack.last();
        Vec3 end;
        float offsetFront = .0F;
        float offsetHeight = .0F;
        float offsetLeft = .0F;
        if (entity instanceof Player) {
            offsetLeft = -0.4F;
            offsetFront = -0.1F;
            offsetHeight = 0.2F;
        }
        Vec3 start = Vec3.ZERO.add(offsetLeft,offsetHeight,offsetFront);
//        TODO: too expensive?
        Vec3 impact = OctaUtils.raycastForEntity(entity.level(), entity, 24, true).getLocation();
        float distance = (float) entity.getEyePosition().distanceTo(impact);
        float radius = .12f;
        int r = (RGB>>>16) & 0xFF;
        int g = (RGB>>>8) & 0xFF;
        int b = (RGB) & 0xFF;
        int a = (int) (Alpha * 255F);

        float deltaTicks = entity.tickCount + partialTicks;
        float deltaUV = -deltaTicks % 10;
        float max = Mth.frac(deltaUV * 0.2F - (float) Mth.floor(deltaUV * 0.1F));
        float min = -1.0F + max;

        var dir = entity.getLookAngle().normalize();

        //y rotation is a triangle of x and z axis
        float dx = (float) dir.x;
        float dz = (float) dir.z;
        //angle = atan o/a
        float yRot = (float) Mth.atan2(dz, dx) - 1.5707f; // for some reason, we are rotated 90 degrees the wrong way. subtracting 2 pi here.
        //x rotation is a triangle of xz and y-axis
        float dxz = Mth.sqrt(dx * dx + dz * dz);
        float dy = (float) dir.y;
        //angle = atan o/a
        float xRot = (float) Mth.atan2(dy, dxz);
        poseStack.mulPose(Axis.YP.rotation(-yRot));
        poseStack.mulPose(Axis.XP.rotation(-xRot));
        for (float j = 1; j <= distance; j += .5f) {
            float decayPercent = 1F - j / distance;
            int aDecay = (int) (((j > 8) ? 1F - (j-8) / (distance-8) : 1F) * a);
            Vec3 wiggle = new Vec3(
                    Mth.sin(deltaTicks * .8f) * .02f,
                    Mth.sin(deltaTicks * .8f + 100) * .02f,
                    Mth.cos(deltaTicks * .8f) * .02f
            );
            end = new Vec3(decayPercent * offsetLeft, decayPercent * offsetHeight, Math.min(j, distance) + decayPercent * offsetFront).add(wiggle);
            VertexConsumer inner = bufferSource.getBuffer(RenderType.itemEntityTranslucentCull(BEACON));
            drawHull(start, end, radius, radius, pose, inner, r, g, b, aDecay, min, max);
            //drawHull(start, end, .25f, .25f, pose, outer, r / 2, g / 2, b / 2, a / 2);
//            VertexConsumer outer = bufferSource.getBuffer(RenderType.entityTranslucent(TWISTING_GLOW));
            VertexConsumer outer = bufferSource.getBuffer(RenderType.itemEntityTranslucentCull(TWISTING_GLOW));
            drawQuad(start, end, radius * 4f, 0, pose, outer, r, g, b, aDecay, min, max);
            drawQuad(start, end, 0, radius * 4f, pose, outer, r, g, b, aDecay, min, max);
            start = end;

        }
        poseStack.popPose();
    }

    private static void drawHull(Vec3 from, Vec3 to, float width, float height, PoseStack.Pose pose, VertexConsumer consumer, int r, int g, int b, int a, float uvMin, float uvMax) {
        //Bottom
        drawQuad(from.subtract(0, height * .5f, 0), to.subtract(0, height * .5f, 0), width, 0, pose, consumer, r, g, b, a, uvMin, uvMax);
        //Top
        drawQuad(from.add(0, height * .5f, 0), to.add(0, height * .5f, 0), width, 0, pose, consumer, r, g, b, a, uvMin, uvMax);
        //Left
        drawQuad(from.subtract(width * .5f, 0, 0), to.subtract(width * .5f, 0, 0), 0, height, pose, consumer, r, g, b, a, uvMin, uvMax);
        //Right
        drawQuad(from.add(width * .5f, 0, 0), to.add(width * .5f, 0, 0), 0, height, pose, consumer, r, g, b, a, uvMin, uvMax);
    }

    private static void drawQuad(Vec3 from, Vec3 to, float width, float height, PoseStack.Pose pose, VertexConsumer consumer, int r, int g, int b, int a, float uvMin, float uvMax) {
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        float halfWidth = width * .5f;
        float halfHeight = height * .5f;

        consumer.vertex(poseMatrix, (float) from.x - halfWidth, (float) from.y - halfHeight, (float) from.z).color(r, g, b, a).uv(0f, uvMin).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, (float) from.x + halfWidth, (float) from.y + halfHeight, (float) from.z).color(r, g, b, a).uv(1f, uvMin).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, (float) to.x + halfWidth, (float) to.y + halfHeight, (float) to.z).color(r, g, b, a).uv(1f, uvMax).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, (float) to.x - halfWidth, (float) to.y - halfHeight, (float) to.z).color(r, g, b, a).uv(0f, uvMax).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0f, 1f, 0f).endVertex();

    }
}
