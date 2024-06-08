package com.vv.octa.mixin.firstPerson;

import com.vv.octa.util.IUpperPartHelper;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public class OctaArmorFeatureRendererMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initInject(RenderLayerParent<T, M> context, A leggingsModel, A bodyModel, ModelManager modelManager, CallbackInfo ci){
        ((IUpperPartHelper)this).Octa$setUpperPart(false);
    }
}
