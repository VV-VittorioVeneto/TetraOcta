package com.vv.octa.client.gui.tweakColorController;

import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;

import com.vv.octa.effect.artillery.CompositeSpectralPowerAttribute;
import net.minecraft.client.gui.GuiGraphics;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;

@ParametersAreNonnullByDefault
public class GuiColorSlider extends GuiClickable {

    private final int valueSteps;
    private final Consumer<Integer> onChange;
    private final GuiElement currentIndicator;
    private final GuiElement hoverIndicator;
    private boolean isDragging = false;
    private int value = 0;
    private final String colorType;
    private final GuiSynchronousDataBase guiSynchronousDataBase;

    public GuiColorSlider(GuiSynchronousDataBase guiSynchronousDataBase, int x, int y, int width, int valueSteps, Consumer<Integer> onChange, String colorType) {
        super(x, y, width, 12, () -> {
        });
        this.colorType = colorType;
        this.guiSynchronousDataBase = guiSynchronousDataBase;
        this.guiSynchronousDataBase.join(this);
        this.hoverIndicator = new GuiElement(0, 4, 1, 4);
        this.currentIndicator = new GuiElement(0, 4, 1, 4);
        this.valueSteps = valueSteps;
        this.onChange = onChange;
        int[] colorArray = generateColorArray(width);
        for(int i = 0; i < width; ++i) {
            this.addChild(new GuiRect(i, 5, 1, 2, colorArray[i]));
        }
        this.hoverIndicator.addChild(new GuiRect(-1, 0, 3, 5, 0));
        this.hoverIndicator.addChild(new GuiRect(0, -1, 1, 3, 8355711));
        this.hoverIndicator.addChild(new GuiRect(0, 3, 1, 1, 8355711));
        this.hoverIndicator.addChild(new GuiRect(0, 5, 1, 1, 8355711));
        this.hoverIndicator.setVisible(false);
        this.addChild(this.hoverIndicator);
        this.currentIndicator.addChild(new GuiRect(-1, 0, 3, 5, 0));
        this.currentIndicator.addChild(new GuiRect(0, -1, 1, 3, 16777215));
        this.currentIndicator.addChild(new GuiRect(0, 3, 1, 1, 16776960));
        this.currentIndicator.addChild(new GuiRect(0, 5, 1, 1, 16777215));
        this.addChild(this.currentIndicator);
        this.currentIndicator.setX(this.value * this.width / (this.valueSteps - 1));
        this.hoverIndicator.setX(this.value * this.width / (this.valueSteps - 1));
    }

    private int []generateColorArray(int width){
        int base;
        int[] colorArray = new int[width];
        switch (colorType) {
            case "artillery/R_value":
                base = (guiSynchronousDataBase.getCache("G") << 8) + guiSynchronousDataBase.getCache("B");
                for (int i = 0 ; i < width ; i++) {
                    colorArray[i] = ((i * 256 / width) << 16) + base;
                }
                break;
            case "artillery/G_value":
                base = (guiSynchronousDataBase.getCache("R") << 16) + guiSynchronousDataBase.getCache("B");
                for (int i = 0 ; i < width ; i++) {
                    colorArray[i] = ((i * 256 / width) << 8) + base;
                }
                break;
            case "artillery/B_value":
                base = ((guiSynchronousDataBase.getCache("R") << 8) + guiSynchronousDataBase.getCache("G")) << 8;
                for (int i = 0 ; i < width ; i++) {
                    colorArray[i] = (i * 256 / width) + base;
                }
                break;
            case "artillery/H_value":
                for (int i = 0 ; i < width ; i++) {
                    colorArray[i] = CompositeSpectralPowerAttribute.HSToRGB(i * 360 / width, guiSynchronousDataBase.getCache("S"));
                }
                break;
            case "artillery/S_value":
                for (int i = 0 ; i < width ; i++) {
                    colorArray[i] = CompositeSpectralPowerAttribute.HSToRGB(guiSynchronousDataBase.getCache("H"), (i * 100 / width));
                }
                break;
        }
        return colorArray;
    }

    public void setValue(int value) {
        this.value = value;
        this.currentIndicator.setX(value * this.width / (this.valueSteps - 1));
    }

    public boolean onMouseClick(int x, int y, int button) {
        if (super.onMouseClick(x, y, button)) {
            this.isDragging = true;
            return true;
        } else {
            return false;
        }
    }

    public void onMouseRelease(int x, int y, int button) {
        this.isDragging = false;
    }

    protected int calculateSegment(int refX, int mouseX) {
        return Math.round((float)(this.valueSteps - 1) * Math.min(Math.max((float)(mouseX - refX - this.x) / ((float) this.width), 0.0F), 1.0F));
    }

    public void updateFocusState(int refX, int refY, int mouseX, int mouseY) {
        this.elements.stream().filter(GuiElement::isVisible).forEach((element) -> element.updateFocusState(refX + this.x + getXOffset(this, element.getAttachmentAnchor()) - getXOffset(element, element.getAttachmentPoint()), refY + this.y + getYOffset(this, element.getAttachmentAnchor()) - getYOffset(element, element.getAttachmentPoint()), mouseX, mouseY));
        boolean gainFocus = mouseX >= this.getX() + refX - 5 && mouseX < this.getX() + refX + this.getWidth() + 10 && mouseY >= this.getY() + refY && mouseY < this.getY() + refY + this.getHeight();
        if (gainFocus != this.hasFocus) {
            this.hasFocus = gainFocus;
            if (this.hasFocus) {
                this.onFocus();
            } else {
                this.onBlur();
            }
        }

    }

    protected void onFocus() {
        this.hoverIndicator.setVisible(true);
    }

    protected void onBlur() {
        this.hoverIndicator.setVisible(false);
    }

    public void uploadSliderColor() {
        if (this.guiSynchronousDataBase != null) {
            switch (this.colorType) {
                case "artillery/R_value":
                    guiSynchronousDataBase.updateCache("R", this.value * 255 / (this.valueSteps - 1));
                    break;
                case "artillery/G_value":
                    guiSynchronousDataBase.updateCache("G", this.value * 255 / (this.valueSteps - 1));
                    break;
                case "artillery/B_value":
                    guiSynchronousDataBase.updateCache("B", this.value * 255 / (this.valueSteps - 1));
                    break;
                case "artillery/H_value":
                    guiSynchronousDataBase.updateCache("H", this.value * 360 / (this.valueSteps - 1));
                    break;
                case "artillery/S_value":
                    guiSynchronousDataBase.updateCache("S", this.value * 100 / (this.valueSteps - 1));
                    break;
            }
        }
    }

    public void updateSliderColor() {
        boolean visible = this.hoverIndicator.isVisible();
        this.clearChildren();
        int[] colorArray = generateColorArray(width);
        for(int i = 0; i < width; ++i) {
            this.addChild(new GuiRect(i, 5, 1, 2, colorArray[i]));
        }
        this.hoverIndicator.addChild(new GuiRect(-1, 0, 3, 5, 0));
        this.hoverIndicator.addChild(new GuiRect(0, -1, 1, 3, 8355711));
        this.hoverIndicator.addChild(new GuiRect(0, 3, 1, 1, 8355711));
        this.hoverIndicator.addChild(new GuiRect(0, 5, 1, 1, 8355711));
        this.hoverIndicator.setVisible(visible);
        this.addChild(this.hoverIndicator);
        this.currentIndicator.addChild(new GuiRect(-1, 0, 3, 5, 0));
        this.currentIndicator.addChild(new GuiRect(0, -1, 1, 3, 16777215));
        this.currentIndicator.addChild(new GuiRect(0, 3, 1, 1, 16776960));
        this.currentIndicator.addChild(new GuiRect(0, 5, 1, 1, 16777215));
        this.addChild(this.currentIndicator);
        this.currentIndicator.setX(this.value * this.width / (this.valueSteps - 1));
        this.hoverIndicator.setX(this.value * this.width / (this.valueSteps - 1));
    }

    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        if (this.isDragging) {
            int newSegment = this.calculateSegment(refX, mouseX);
            if (newSegment != this.value) {
                this.value = newSegment;
                this.onChange.accept(this.value);
            }
        } else if (this.hoverIndicator.isVisible()) {
            this.hoverIndicator.setX(this.calculateSegment(refX, mouseX) * this.width / (this.valueSteps - 1));
        }

        uploadSliderColor();
        super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
    }
}
