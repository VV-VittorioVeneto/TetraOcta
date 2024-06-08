package com.vv.octa.mixin.firstPerson;

import com.vv.octa.util.IUpperPartHelper;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderLayer.class)
public class OctaFeatureRendererMixin implements IUpperPartHelper {
    @Unique
    private boolean Octa$isUpperPart = true;


    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(RenderLayerParent renderLayerParent, CallbackInfo ci) {
        if (this.getClass().getPackageName().contains("skinlayers") && !this.getClass().getSimpleName().toLowerCase().contains("head")) {
            Octa$isUpperPart = false;
        }
    }

    @Override
    public boolean Octa$isUpperPart() {
        return this.Octa$isUpperPart;
    }

    @Override
    public void Octa$setUpperPart(boolean bl) {
        this.Octa$isUpperPart = bl;
    }
}
