package com.vv.octa.client.gui.tweakColorController;

import net.minecraft.client.resources.language.I18n;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiStringSmall;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class GuiTweakColorPreviewer extends GuiElement {

    public GuiTweakColorPreviewer(GuiSynchronousDataBase guiSynchronousDataBase, int x, int y, int width) {
        super(x, y, width, 40);
        this.addChild((new GuiStringSmall(0, 5, I18n.get("tetra.tweak.previewer.bottom"))).setAttachment(GuiAttachment.bottomCenter));
        this.addChild((new GuiStringSmall(0, -5, I18n.get("tetra.tweak.previewer.top"))).setAttachment(GuiAttachment.topCenter));
        GuiColorPreviewer previewer = new GuiColorPreviewer(guiSynchronousDataBase, 0, 0, width);
        previewer.setAttachment(GuiAttachment.topCenter);
        this.addChild(previewer);
    }

}
