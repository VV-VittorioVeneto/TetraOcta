package com.vv.octa.client.gui.tweakColorController;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringSmall;
import se.mickelus.tetra.module.data.TweakData;

@ParametersAreNonnullByDefault
public class GuiTweakColorSlider extends GuiElement {
    private final GuiString labelString;
    private final GuiColorSlider slider;
    private final List<Component> tooltip;
    private final int steps;

    public GuiTweakColorSlider(GuiSynchronousDataBase guiSynchronousDataBase, int x, int y, int width, TweakData tweak, Consumer<Integer> onChange, String colorType) {
        super(x, y, width, 16);
        this.labelString = new GuiStringSmall(0, 0, I18n.get("tetra.tweak." + tweak.key + ".label"));
        this.labelString.setAttachment(GuiAttachment.topCenter);
        this.addChild(this.labelString);
        this.addChild((new GuiStringSmall(-2, 1, I18n.get("tetra.tweak." + tweak.key + ".left"))).setAttachment(GuiAttachment.bottomLeft));
        this.addChild((new GuiStringSmall(-1, 1, I18n.get("tetra.tweak." + tweak.key + ".right"))).setAttachment(GuiAttachment.bottomRight));
        this.slider = new GuiColorSlider(guiSynchronousDataBase, -2, 3, width, tweak.steps, (step) -> onChange.accept(step - tweak.steps), colorType);
        this.slider.setAttachment(GuiAttachment.topCenter);
        this.addChild(this.slider);
        this.steps = tweak.steps;
        this.tooltip = Collections.singletonList(Component.translatable("tetra.tweak." + tweak.key + ".tooltip"));
    }

    public void setValue(int value) {
        this.slider.setValue(value + this.steps);
    }

    public List<Component> getTooltipLines() {
        return this.labelString.hasFocus() ? this.tooltip : super.getTooltipLines();
    }
}
