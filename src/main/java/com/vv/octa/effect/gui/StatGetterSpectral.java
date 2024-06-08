package com.vv.octa.effect.gui;

import com.vv.octa.effect.artillery.CompositeSpectralPowerAttribute;
import com.vv.octa.modular.ModularLaserArtilleryItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class StatGetterSpectral implements IStatGetter {
    public StatGetterSpectral() {
    }

    public boolean shouldShow(Player player, ItemStack currentStack, ItemStack previewStack) {
        return currentStack.getItem() instanceof ModularLaserArtilleryItem;
    }

    public double getValue(Player player, ItemStack itemStack) {
        if (itemStack.getItem() instanceof ModularLaserArtilleryItem artilleryItem) {
            return CompositeSpectralPowerAttribute.RGBToEnergy(artilleryItem.getCrystalRGB(itemStack));
        } else return 0;
    }

    public double getValue(Player player, ItemStack itemStack, String slot) {
        return getValue(player, itemStack);
    }

    public double getValue(Player player, ItemStack itemStack, String slot, String improvement) {
        return getValue(player, itemStack);
    }

}