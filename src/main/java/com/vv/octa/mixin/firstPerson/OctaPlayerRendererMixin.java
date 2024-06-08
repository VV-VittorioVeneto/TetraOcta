package com.vv.octa.mixin.firstPerson;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vv.octa.util.FirstPersonMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class OctaPlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public OctaPlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> entityModel, float f) {
        super(context, entityModel, f);
    }
    @Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
    private void hideBonesInFirstPerson(AbstractClientPlayer entity,
                                        float f, float g, PoseStack matrixStack,
                                        MultiBufferSource vertexConsumerProvider,
                                        int i, CallbackInfo ci) {
        if (FirstPersonMode.isFirstPersonPass()) {
            if (entity == Minecraft.getInstance().getCameraEntity()) {
                this.model.head.visible = false;
                this.model.body.visible = false;
                this.model.leftLeg.visible = false;
                this.model.rightLeg.visible = false;
                this.model.rightArm.visible = false;
                this.model.leftArm.visible = false;
                this.model.hat.visible = false;
                this.model.leftSleeve.visible = false;
                this.model.rightSleeve.visible = false;
                this.model.leftPants.visible = false;
                this.model.rightPants.visible = false;
                this.model.jacket.visible = false;
                this.model.setAllVisible(false);
            }
        }

    }
}
