package com.vv.octa.client.gui.pluginConfigurationController;

import net.minecraft.client.gui.GuiGraphics;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;

public class GuiPluginSwitcher extends GuiClickable {

    private boolean isSwitching = false;
    private boolean state = false;
    private int timer = 0;
    private final int phase = 4;
    private final GuiElement switcher;

    public GuiPluginSwitcher(int x, int y, int width, int height) {
        super(x, y, width, height,  () -> {
        });
        this.switcher = new GuiElement(1, 0, width - 2, height - phase * 2 - 2);
        this.switcher.addChild(new GuiRect(1, 0, width - 2, height - phase * 2 - 2, 16777215));
        this.addChild(switcher);
    }

    @Override
    public boolean onMouseClick(int x, int y, int button) {
        if (!isSwitching) {
            super.onMouseClick(x, y, button);
            this.isSwitching = true;
            this.timer = 0;
            return true;
        } else {
            return false;
        }
    }

    private int timerOn() {
        int gapTime = 4;
        if (this.timer++ < gapTime * phase) {
            return timer / gapTime;
        } else {
            this.timer = 0;
            this.isSwitching = false;
            return 0;
        }
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        if (this.isSwitching) {
            switcher.setY(timerOn());
        } else if (this.state) {
            switcher.setY(phase);
        }
        super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
    }

    public void setSwitcherOn (boolean state) {
        this.state = state;
    }

}
