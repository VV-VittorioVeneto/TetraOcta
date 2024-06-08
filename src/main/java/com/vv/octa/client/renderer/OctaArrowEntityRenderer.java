package com.vv.octa.client.renderer;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vv.octa.entity.OctaArrowEntity;
import com.vv.octa.modular.ModularArrowItem;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemStack;

public class OctaArrowEntityRenderer extends EntityRenderer<OctaArrowEntity> {
    private static final ResourceLocation ARROW_HEAD = new ResourceLocation("octa", "textures/entity/octa_arrow/arrow_head.png");
    private static final ResourceLocation ARROW_SHAFT = new ResourceLocation("octa", "textures/entity/octa_arrow/arrow_shaft.png");
    private static final ResourceLocation ARROW_FLETCHING = new ResourceLocation("octa", "textures/entity/octa_arrow/arrow_fletching.png");
    private static final ResourceLocation NORMAL_ARROW_LOCATION = new ResourceLocation("textures/entity/projectiles/arrow.png");
    private static final ResourceLocation[] ARROW_PARTS = {ARROW_HEAD, ARROW_SHAFT, ARROW_FLETCHING};
    private static final String[] PARTS = {ModularArrowItem.headKey, ModularArrowItem.shaftKey, ModularArrowItem.fletchingKey};

    private final ResourceLocation[] textureLocations;

    public OctaArrowEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.textureLocations = ARROW_PARTS;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull OctaArrowEntity entity) {
        return NORMAL_ARROW_LOCATION;
    }

    private TextureInformation getTextureLocation(OctaArrowEntity entity, int partIndex) {
        if (partIndex >= 0 && partIndex < textureLocations.length) {
            ItemStack arrowStack = entity.getArrowStack();
            ModularArrowItem arrowItem = (ModularArrowItem)arrowStack.getItem();
            int rgb = arrowItem.getAllModules(arrowStack).stream().filter(m -> m.getSlot().contains(PARTS[partIndex])).map(m -> m.getVariantData(arrowStack).models[0].tint).toList().get(0);
            int alpha = (partIndex == 4) ? 127 : 255;
            int color = (alpha << 24) + rgb;
            return new TextureInformation(textureLocations[partIndex], color);
        }
        return new TextureInformation(getTextureLocation(entity), 0xFFFFFFFF);
    }

    @Override
    public void render(OctaArrowEntity arrow, float p_225623_2_, float p_225623_3_, PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int p_225623_6_) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(p_225623_3_, arrow.yRotO, arrow.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(p_225623_3_, arrow.xRotO, arrow.getXRot())));
        float f9 = (float)arrow.shakeTime - p_225623_3_;
        if (f9 > 0.0F) {
            float f10 = -Mth.sin(f9 * 3.0F) * f9;
            poseStack.mulPose(Axis.ZP.rotationDegrees(f10));
        }

        poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));
        poseStack.scale(0.05625F, 0.05625F, 0.05625F);
        poseStack.translate(-4.0D, 0.0D, 0.0D);

        if (arrow.getArrowStack() != null) {
            for (int i = 0; i < ((ModularArrowItem)arrow.getArrowStack().getItem()).getAllModules(arrow.getArrowStack()).stream().toList().size(); i++) {
                renderPart(i, arrow, poseStack, multiBufferSource, p_225623_6_);
            }
        } else {
            renderPartless(arrow, poseStack, multiBufferSource, p_225623_6_);
        }

        poseStack.popPose();
        super.render(arrow, p_225623_2_, p_225623_3_, poseStack, multiBufferSource, p_225623_6_);
    }

    public void renderPart(int partIndex, OctaArrowEntity arrow, PoseStack poseStack, MultiBufferSource multiBufferSource, int p_225623_6_) {
        TextureInformation information = getTextureLocation(arrow, partIndex);
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutout(information.resourceLocation));

        performRendering(vertexConsumer, poseStack, p_225623_6_, information.color);
    }

    public void renderPartless(OctaArrowEntity arrow, PoseStack poseStack, MultiBufferSource multiBufferSource, int p_225623_6_) {
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutout(getTextureLocation(arrow)));

        performRendering(vertexConsumer, poseStack, p_225623_6_, 0xFFFFFFFF);
    }

    public void performRendering(VertexConsumer vertexConsumer, PoseStack poseStack, int p_225623_6_, int color) {
        PoseStack.Pose posestack$pose = poseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, p_225623_6_, color);
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, p_225623_6_, color);
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, p_225623_6_, color);
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, p_225623_6_, color);
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, p_225623_6_, color);
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, p_225623_6_, color);
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, p_225623_6_, color);
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, p_225623_6_, color);

        for(int j = 0; j < 4; ++j) {
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            this.vertex(matrix4f, matrix3f, vertexConsumer, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, p_225623_6_, color);
            this.vertex(matrix4f, matrix3f, vertexConsumer, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, p_225623_6_, color);
            this.vertex(matrix4f, matrix3f, vertexConsumer, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, p_225623_6_, color);
            this.vertex(matrix4f, matrix3f, vertexConsumer, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, p_225623_6_, color);
        }
    }

    public void vertex(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, int x, int y, int z, float p_229039_7_, float p_229039_8_, int p_229039_9_, int p_229039_10_, int p_229039_11_, int p_229039_12_, int color) {
        vertexConsumer.vertex(matrix4f, (float)x, (float)y, (float)z).color((color >>> 16) & 0xFF, (color >>> 8) & 0xFF, (color) & 0xFF, (color >>> 24) & 0xFF).uv(p_229039_7_, p_229039_8_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229039_12_).normal(matrix3f, (float)p_229039_9_, (float)p_229039_11_, (float)p_229039_10_).endVertex();
    }

    private static class TextureInformation {
        public ResourceLocation resourceLocation;
        public int color;

        public TextureInformation(ResourceLocation resourceLocation, int color) {
            this.resourceLocation = resourceLocation;
            this.color = color;
        }
    }
}
