package com.vv.octa.mixin.firstPerson;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vv.octa.util.FirstPersonMode;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class OctaLevelRendererMixin {

    @Unique
    private boolean Octa$defaultCameraState = false;

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;isDetached()Z"))
    private void fakeThirdPersonMode(PoseStack poseStack, float f, long l, boolean bl, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo ci) {
        Octa$defaultCameraState = camera.isDetached();
        FirstPersonMode.setFirstPersonPass(!camera.isDetached() && (!(camera.getEntity() instanceof LivingEntity) || !((LivingEntity) camera.getEntity()).isSleeping()));
        ((OctaCameraAccessor)camera).setDetached(true);
    }
    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;isDetached()Z", shift = At.Shift.AFTER))
    private void resetThirdPerson(PoseStack poseStack, float f, long l, boolean bl, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo ci) {
        ((OctaCameraAccessor)camera).setDetached(Octa$defaultCameraState);
    }



    @Inject(method = "renderEntity", at = @At("TAIL"))
    private void dontRenderEntity_End(Entity entity, double cameraX, double cameraY, double cameraZ,
                                      float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, CallbackInfo ci) {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        if (entity == camera.getEntity()) {
            FirstPersonMode.setFirstPersonPass(false); // Unmark this render cycle
        }
    }
}
