package com.vv.octa.effect;

import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatGetterAttribute;
import se.mickelus.tetra.gui.stats.getter.StatGetterEnchantmentLevel;
import se.mickelus.tetra.gui.stats.getter.StatGetterSum;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterInteger;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

import static com.vv.octa.effect.gui.EffectGuiStats.*;
import static se.mickelus.tetra.gui.stats.StatsHelper.barLength;

import com.vv.octa.init.OctaAttributesRegistry;

public class BoomerangSpeedAttribute {
    
    @OnlyIn(Dist.CLIENT)
    public static void init() {
        final IStatGetter effectStatGetter = new StatGetterSum(
            new StatGetterEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY,0.1), new StatGetterAttribute(OctaAttributesRegistry.BOOMERANG_SPEED.get()));
        final GuiStatBar effectBar = new GuiStatBar
                (0, 0, barLength, boomerangSpeedName, 0, 2, false, effectStatGetter,
                        LabelGetterBasic.decimalLabel, new TooltipGetterInteger
                        (boomerangSpeedTooltip, effectStatGetter));
        WorkbenchStatsGui.addBar(effectBar);
        HoloStatsGui.addBar(effectBar);
    }

}
