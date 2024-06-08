package com.vv.octa.effect.artillery;

import com.vv.octa.init.OctaAttributesRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.*;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

import static com.vv.octa.effect.gui.EffectGuiStats.*;
import static se.mickelus.tetra.gui.stats.StatsHelper.barLength;

public class CoolingSpeedAttribute {
    @OnlyIn(Dist.CLIENT)
    public static void init() {
        final IStatGetter effectStatGetter = new StatGetterAttribute(OctaAttributesRegistry.ARTILLERY_COOLING.get());
        final GuiStatBar effectBar = new GuiStatBar
                (0, 0, barLength, artilleryCoolingName, 0, 50, false, effectStatGetter,
                        LabelGetterBasic.decimalLabel, new TooltipGetterDecimal
                        (artilleryCoolingTooltip, effectStatGetter));
        WorkbenchStatsGui.addBar(effectBar);
        HoloStatsGui.addBar(effectBar);
    }
}
