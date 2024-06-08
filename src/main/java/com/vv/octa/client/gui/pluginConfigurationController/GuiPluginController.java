package com.vv.octa.client.gui.pluginConfigurationController;

import com.vv.octa.client.gui.tweakColorController.GuiColorPreviewer;
import com.vv.octa.client.gui.tweakColorController.GuiSynchronousDataBase;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.resources.language.I18n;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;
import se.mickelus.mutil.gui.GuiStringSmall;
import se.mickelus.tetra.gui.GuiKeybinding;

public class GuiPluginController extends GuiElement {

    private final GuiElement switcherGui;
    private final GuiElement indicatorGui;
    private final GuiElement bindingKeyGui;
    private final int colorOn = 0;
    private final int colorOff = 0;

    public GuiPluginController(KeyMapping keyMapping, int x, int y) {
        super(x, y, 20, 100);
        this.addChild((new GuiStringSmall(0, -5, I18n.get("tetra.plugin.switcher.top"))).setAttachment(GuiAttachment.topCenter));

        this.switcherGui = new GuiElement(0, 0, 50, 50);
        GuiPluginSwitcher guiPluginSwitcher = new GuiPluginSwitcher(0, -5, 11, 8);
        this.switcherGui.addChild(guiPluginSwitcher);
        this.addChild(switcherGui);

        this.indicatorGui = new GuiElement(0,0,0,0);
        this.indicatorGui.addChild(new GuiRect(0,0,0,0,colorOff));
        this.addChild(indicatorGui);

        this.bindingKeyGui = new GuiElement(0,0,0,0);
        this.bindingKeyGui.addChild(new GuiKeybinding(0,0,keyMapping));
        this.addChild(bindingKeyGui);
    }

}
