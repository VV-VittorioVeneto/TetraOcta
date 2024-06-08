package com.vv.octa.mixin.firstPerson;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vv.octa.util.FirstPersonMode;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class OctaEntityRenderDispatcherMixin {
    @Inject(method = "renderShadow", at = @At("HEAD"), cancellable = true)
    private static void renderShadow_HEAD_PlayerAnimator(PoseStack matrices, MultiBufferSource vertexConsumers, Entity entity, float opacity, float tickDelta, LevelReader world, float radius, CallbackInfo ci) {
        if (entity instanceof Player && FirstPersonMode.isFirstPersonPass()) {
            ci.cancel();
        }
    }
}
