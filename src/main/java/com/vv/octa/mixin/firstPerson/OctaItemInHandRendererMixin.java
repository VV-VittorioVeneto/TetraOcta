package com.vv.octa.mixin.firstPerson;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vv.octa.modular.ModularLaserArtilleryItem;
import com.vv.octa.util.FirstPersonMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class OctaItemInHandRendererMixin {
    @Inject(method = "renderHandsWithItems", at = @At("HEAD"), cancellable = true)
    private void disableDefaultItemIfNeeded(float f, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, LocalPlayer localPlayer, int i, CallbackInfo ci) {
        if (FirstPersonMode.isFirstPersonPass()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderStatic(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;III)V"), cancellable = true)
    private void cancelItemRender(LivingEntity entity, ItemStack itemStack, ItemDisplayContext transformType, boolean bl, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        if (entity != Minecraft.getInstance().getCameraEntity()) {
            return;
        }
        if (FirstPersonMode.isFirstPersonPass()) {
            ci.cancel();
        } else if (entity.getMainHandItem().getItem() instanceof ModularLaserArtilleryItem){
            ci.cancel();
        }
    }
}
