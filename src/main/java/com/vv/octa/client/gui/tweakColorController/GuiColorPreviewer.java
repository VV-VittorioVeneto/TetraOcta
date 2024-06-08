package com.vv.octa.client.gui.tweakColorController;

import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;

public class GuiColorPreviewer extends GuiElement {

    private final int originColor;
    private final GuiSynchronousDataBase guiSynchronousDataBase;

    public GuiColorPreviewer(GuiSynchronousDataBase guiSynchronousDataBase, int x, int y, int width) {
        super(x, y, width, 40);
        this.originColor = guiSynchronousDataBase.getRGBColor();
        this.guiSynchronousDataBase = guiSynchronousDataBase;
        this.guiSynchronousDataBase.join(this);
        updatePreviewerColor();
    }

    private void drawBackground() {
        int white = 16777215;
        int gray = 8355711;
        int black = 0;
        int y1 = 3;
        int x1 = 3;
        int y2 = height - 4;
        int x2 = width - 4;
        this.addChild(new GuiRect(0, y1, width, 1, gray));
        this.addChild(new GuiRect(0, y2, width, 1, gray));
        this.addChild(new GuiRect(x1, 0, 1, height, gray));
        this.addChild(new GuiRect(x2, 0, 1, height, gray));

        this.addChild(new GuiRect(x1-1, y1-1, 3, 3, black));
        this.addChild(new GuiRect(x2-1, y1-1, 3, 3, black));
        this.addChild(new GuiRect(x1-1, y2-1, 3, 3, black));
        this.addChild(new GuiRect(x2-1, y2-1, 3, 3, black));

        this.addChild(new GuiRect(x1, y1, 1, 1, white));
        this.addChild(new GuiRect(x1, y2, 1, 1, white));
        this.addChild(new GuiRect(x2, y1, 1, 1, white));
        this.addChild(new GuiRect(x2, y2, 1, 1, white));

        this.addChild(new GuiRect(5, height/2, width-10, height/2 - 5, originColor));
    }

    public void updatePreviewerColor() {
        this.clearChildren();
        drawBackground();
        this.addChild(new GuiRect(5, 5, width-10, height/2 - 5, guiSynchronousDataBase.getRGBColor()));
    }

}
