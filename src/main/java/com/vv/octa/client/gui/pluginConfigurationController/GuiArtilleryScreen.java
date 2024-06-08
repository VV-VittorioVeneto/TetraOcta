package com.vv.octa.client.gui.pluginConfigurationController;

import com.vv.octa.Octa;
import com.vv.octa.util.OctaKeyBindings;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import se.mickelus.mutil.gui.*;
import se.mickelus.mutil.gui.impl.GuiHorizontalLayoutGroup;
import se.mickelus.tetra.gui.GuiKeybinding;
import se.mickelus.tetra.gui.GuiTextures;
import com.vv.octa.modular.container.ArtilleryContainer;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

import static com.vv.octa.modular.container.ArtilleryContainer.slotsY;
import static com.vv.octa.modular.container.ArtilleryContainer.slotsX;
import static com.vv.octa.modular.container.ArtilleryContainer.gapWidth;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class GuiArtilleryScreen extends AbstractContainerScreen<ArtilleryContainer> {
    private static GuiArtilleryScreen instance;
    private final GuiElement defaultGui;
    private final GuiElement keyBindGui;
    private final GuiElement pluginControllerGui;
    private final GuiElement pluginChipGui;

    public GuiArtilleryScreen(ArtilleryContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        Octa.debug("Open Gui Step6.5");
        int i;
        int x;
        int y;
        this.imageWidth = 179;
        this.imageHeight = 240;
        int slotNum = container.getSlotNum();
        this.defaultGui = new GuiElement(0, 0, this.imageWidth, this.imageHeight);
        this.defaultGui.addChild(new GuiRect(0, 0, 240, 100, 0));
        this.defaultGui.addChild(new GuiTexture(0, 129, 179, 91, GuiTextures.playerInventory));
        this.pluginControllerGui = new GuiElement(0, 69, 240, 100);
        this.pluginChipGui = (new GuiElement(slotsX - (gapWidth - 14) / 2 , slotsY - (gapWidth - 14) / 2 ,  2 * gapWidth, 2 * gapWidth)).setAttachment(GuiAttachment.topCenter);
        this.pluginChipGui.addChild(new GuiRect(0, 0, 2 * gapWidth, 2 * gapWidth, 255));
        if (slotNum > 0) {
            for(i = 0; i < slotNum; ++i) {
                x = (i & 1) * gapWidth;
                y = (i & 2) * gapWidth / 2;
                GuiEmbeddedChipSlot guiEmbeddedChipSlot = new GuiEmbeddedChipSlot(x, y);
                this.pluginChipGui.addChild(guiEmbeddedChipSlot);
            }
        }
        this.keyBindGui = new GuiElement(0, 0, 3840, 23);
        this.keyBindGui.addChild((new GuiRect(0, 0, 3840, 23, -872415232)).setAttachment(GuiAttachment.bottomCenter));
        this.keyBindGui.addChild((new GuiRect(0, -21, 3840, 1, 4210752)).setAttachment(GuiAttachment.bottomCenter));
        GuiHorizontalLayoutGroup keyBindGroup = new GuiHorizontalLayoutGroup(0, -5, 11, 8);
        keyBindGroup.setAttachment(GuiAttachment.bottomCenter);
        this.keyBindGui.addChild(keyBindGroup);
        keyBindGroup.addChild(new GuiKeybinding(0, 0, OctaKeyBindings.switchWindowBinding));
        keyBindGroup.addChild(new GuiRect(0, -1, 1, 13, 4210752));
        keyBindGroup.addChild(new GuiKeybinding(0, 0, OctaKeyBindings.safeModeBinding));
        keyBindGroup.addChild(new GuiRect(0, -1, 1, 13, 4210752));
        keyBindGroup.addChild(new GuiKeybinding(0, 0, OctaKeyBindings.openBinding));



        instance = this;
        Octa.debug("Open Gui Step7");
    }

    protected void slotClicked(Slot slot, int slotIndex, int barIndex, ClickType clickType) {
        if (!(slot instanceof DisabledSlot)) {
            super.slotClicked(slot, slotIndex, barIndex, clickType);
        }

    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        this.keyBindGui.setWidth(this.width);
        this.keyBindGui.updateFocusState(0, this.height - this.keyBindGui.getHeight(), mouseX, mouseY);
        this.keyBindGui.draw(graphics, 0, this.height - this.keyBindGui.getHeight(), this.width, this.height, mouseX, mouseY, 1.0F);
        this.defaultGui.updateFocusState(x, y, mouseX, mouseY);
        this.defaultGui.draw(graphics, x, y, this.width, this.height, mouseX, mouseY, 1.0F);
        this.pluginControllerGui.updateFocusState(x, this.height - this.pluginControllerGui.getHeight(), mouseX, mouseY);
        this.pluginControllerGui.draw(graphics, x, y, this.width, this.height, mouseX, mouseY, 1.0F);
        this.pluginChipGui.updateFocusState(x, this.height - this.pluginChipGui.getHeight(), mouseX, mouseY);
        this.pluginChipGui.draw(graphics, x, y, this.width, this.height, mouseX, mouseY, 1.0F);
    }

    protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderTooltip(graphics, mouseX, mouseY);
        List<Component> tooltipLines = this.defaultGui.getTooltipLines();
        if (tooltipLines != null) {
            graphics.renderTooltip(this.font, tooltipLines, Optional.empty(), mouseX, mouseY);
        }

    }

    protected void renderLabels(GuiGraphics graphics, int x, int y) {
    }

    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void init(){
        super.init();
    }
}