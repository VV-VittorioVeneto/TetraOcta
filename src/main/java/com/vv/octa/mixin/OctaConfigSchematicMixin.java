package com.vv.octa.mixin;

import com.vv.octa.modular.ModularAmmo;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import se.mickelus.tetra.module.schematic.ConfigSchematic;
import se.mickelus.tetra.module.schematic.OutcomeDefinition;
import se.mickelus.tetra.module.schematic.SchematicDefinition;

import java.util.Optional;

@Mixin(ConfigSchematic.class)
public class OctaConfigSchematicMixin {
    @Mutable
    @Final
    @Shadow(remap = false)
    private final SchematicDefinition definition;

    public OctaConfigSchematicMixin(SchematicDefinition definition) {
        this.definition = definition;
    }

    @Shadow(remap = false)
    public int getRequiredQuantity(ItemStack itemStack, int index, ItemStack materialStack){
        return 100;
    }

    @Inject(method = "getRequiredQuantity", at = @At("TAIL"), cancellable = true, remap = false)
    private void getRequiredQuantityMixin(ItemStack itemStack, int index, ItemStack materialStack, CallbackInfoReturnable<Integer> cir) {
        if(itemStack.getItem() instanceof ModularAmmo) {
            cir.setReturnValue((int) (cir.getReturnValue() * Math.ceil(itemStack.getCount() / 16.0D)));
        }
    }

    @Inject(method = "isMaterialsValid", at = @At("RETURN"), cancellable = true, remap = false)
    public void isMaterialsValidMixin(ItemStack itemStack, String itemSlot, ItemStack[] materials, CallbackInfoReturnable<Boolean> cir) {
        if(itemStack.getItem() instanceof ModularAmmo) {
            boolean b = true;
            for (int i = 0; i < definition.materialSlotCount; i++) {
                b &= !(materials[i].getCount() < getRequiredQuantity(itemStack, i, materials[i]));
            }
            cir.setReturnValue(cir.getReturnValue() && b);
        }
    }

    @Inject(method = "applyUpgrade", at = @At("HEAD"), cancellable = true, remap = false)
    public void applyUpgradeMixin(ItemStack itemStack, ItemStack[] materials, boolean consumeMaterials, String slot, Player player, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack upgradedStack = itemStack.copy();
        int index;
        if (this.definition.materialSlotCount > 0) {
            for(int i = 0; i < materials.length; ++i) {
                index = i;
                int finalIndex = index;
                this.getOutcomeFromMaterial(materials[index], index).ifPresent((outcomex) -> {
                    this.applyOutcome((OutcomeDefinition) outcomex, upgradedStack, consumeMaterials, slot, player);
                    if (consumeMaterials) {
                        materials[finalIndex].shrink((int) (((OutcomeDefinition) outcomex).material.count * Math.ceil(itemStack.getCount() / 16.0D)));
                        this.triggerAdvancement((OutcomeDefinition) outcomex, player, itemStack, upgradedStack, slot);
                    }

                });
            }
        } else {
            OutcomeDefinition[] var11 = this.definition.outcomes;
            index = var11.length;

            for(int var9 = 0; var9 < index; ++var9) {
                OutcomeDefinition outcome = var11[var9];
                this.applyOutcome(outcome, upgradedStack, consumeMaterials, slot, player);
                if (consumeMaterials) {
                    this.triggerAdvancement(outcome, player, itemStack, upgradedStack, slot);
                }
            }
        }

        cir.setReturnValue(upgradedStack);
    }

    @Shadow(remap = false)
    private void triggerAdvancement(OutcomeDefinition outcome, Player player, ItemStack itemStack, ItemStack upgradedStack, String slot) {
    }

    @Shadow(remap = false)
    private Optional<Object> getOutcomeFromMaterial(ItemStack material, int index) {
        return Optional.empty();
    }

    @Shadow(remap = false)
    private void applyOutcome(OutcomeDefinition outcome, ItemStack upgradedStack, boolean consumeMaterials, String slot, Player player) {
    }


}
