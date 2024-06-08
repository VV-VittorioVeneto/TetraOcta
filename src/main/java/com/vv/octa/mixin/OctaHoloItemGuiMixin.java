package com.vv.octa.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.injection.At;

import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloItemGui;

@Mixin(HoloItemGui.class)
public abstract class OctaHoloItemGuiMixin{
    @ModifyArg(method = "<init>(IILse/mickelus/tetra/items/modular/IModularItem;Lnet/minecraft/world/item/ItemStack;ILjava/lang/Runnable;Ljava/util/function/Consumer;)V",
            at = @At(value = "INVOKE", target = "Lse/mickelus/mutil/gui/GuiTexture;<init>(IIIIIILnet/minecraft/resources/ResourceLocation;)V"), index = 6, remap = false)
    public ResourceLocation init(ResourceLocation textureLocation){
        return new ResourceLocation(textureLocation.getNamespace(), "textures/gui/octa_holo.png");
    }
}