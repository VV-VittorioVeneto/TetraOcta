package com.vv.octa.client.gui.pluginConfigurationController;

import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.gui.GuiTextures;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class GuiEmbeddedChipSlot extends GuiElement {
    private final int color;
    public GuiEmbeddedChipSlot(int x, int y) {
        super(x, y, 0, 0);
        this.setAttachment(GuiAttachment.topLeft);
        this.setWidth(22);
        this.setHeight(22);
        this.color = 8355711;
        this.addChild(new GuiRect(0, 0, this.width, this.height, 0));
        this.addChild((new GuiRect(1, 1, 1, this.height - 2, color)).setAttachment(GuiAttachment.topLeft));
        this.addChild((new GuiRect(-1, 1, 1, this.height - 2, color)).setAttachment(GuiAttachment.topRight));
        this.addChild((new GuiRect(1, 1, this.width-2, 1, color)).setAttachment(GuiAttachment.topLeft));
        this.addChild((new GuiRect(1, -1, this.width-2, 1, color)).setAttachment(GuiAttachment.bottomLeft));

//        this.addChild((new GuiTexture(0, 0, 16, 14, 32, 0, GuiTextures.toolbelt)).setAttachmentPoint(GuiAttachment.topRight));
//        this.addChild((new GuiTexture(0, 0, 16, 14, 32, 14, GuiTextures.toolbelt)).setAttachmentPoint(GuiAttachment.bottomRight).setAttachmentAnchor(GuiAttachment.bottomLeft));
//        this.addChild((new GuiTexture(0, 0, 16, 14, 48, 0, GuiTextures.toolbelt)).setAttachmentPoint(GuiAttachment.topLeft).setAttachmentAnchor(GuiAttachment.topRight));
//        this.addChild((new GuiTexture(0, 0, 16, 14, 48, 14, GuiTextures.toolbelt)).setAttachmentPoint(GuiAttachment.bottomLeft).setAttachmentAnchor(GuiAttachment.bottomRight));
    }
}
