package com.vv.octa.mixin;

import com.vv.octa.modular.ModularAmmo;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import se.mickelus.tetra.blocks.workbench.gui.CraftButtonGui;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@Mixin(CraftButtonGui.class)
public class OctaCraftButtonGuiMixin {
    @Inject(method = "hasInsufficientQuantities", at = @At(value = "INVOKE", target = "Lse/mickelus/tetra/module/schematic/UpgradeSchematic;getRequiredQuantity(Lnet/minecraft/world/item/ItemStack;ILnet/minecraft/world/item/ItemStack;)I", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT, remap = false, cancellable = true)
    private void hasInsufficientQuantities(UpgradeSchematic schematic, ItemStack itemStack, String slot, ItemStack[] materials, CallbackInfoReturnable<Boolean> cir, int i) {
        if(itemStack.getItem() instanceof ModularAmmo) {
            int requiredCount = schematic.getRequiredQuantity(itemStack, i, materials[i]) * (int) Math.ceil(itemStack.getCount() / 16.0D);
            cir.setReturnValue(!materials[i].isEmpty() && requiredCount > materials[i].getCount());
        }
    }
}
