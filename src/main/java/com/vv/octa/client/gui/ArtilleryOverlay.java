package com.vv.octa.client.gui;

import com.mojang.blaze3d.platform.Window;
import com.vv.octa.Octa;
import com.vv.octa.network.OpenArtilleryGuiPacket;
import com.vv.octa.util.OctaKeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiRoot;
import se.mickelus.tetra.TetraMod;
import se.mickelus.tetra.client.keymap.TetraKeyMappings;
import se.mickelus.tetra.items.modular.impl.holo.ModularHolosphereItem;
import se.mickelus.tetra.items.modular.impl.toolbelt.EquipToolbeltItemPacket;
import se.mickelus.tetra.items.modular.impl.toolbelt.OpenToolbeltItemPacket;
import se.mickelus.tetra.items.modular.impl.toolbelt.StoreToolbeltItemPacket;
import se.mickelus.tetra.items.modular.impl.toolbelt.ToolbeltHelper;
import se.mickelus.tetra.items.modular.impl.toolbelt.gui.overlay.HolosphereGroupGui;
import se.mickelus.tetra.items.modular.impl.toolbelt.gui.overlay.QuickslotGroupGui;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.QuickslotInventory;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.ToolbeltSlotType;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ArtilleryOverlay extends GuiRoot implements IGuiOverlay {
    private final QuickslotGroupGui quickslotGroup = new QuickslotGroupGui(52, 0);
    private final HolosphereGroupGui holosphereGroup;
    private long openTime = -1L;
    private boolean isActive = false;

    public ArtilleryOverlay(Minecraft mc) {
        super(mc);
        this.quickslotGroup.setAttachmentAnchor(GuiAttachment.middleCenter);
        this.addChild(this.quickslotGroup);
        this.holosphereGroup = new HolosphereGroupGui(0, -40);
        this.holosphereGroup.setAttachmentAnchor(GuiAttachment.middleCenter);
        this.addChild(this.holosphereGroup);
    }

    public void setInventories(ItemStack itemStack) {
    }

    public void toggleActive(boolean active) {
        this.isActive = active;
        this.quickslotGroup.setVisible(active);
        this.holosphereGroup.setVisible(active);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        Octa.debug("Open Gui Step-1");
        if (TetraKeyMappings.restockBinding.isDown()) {
            this.storeToolbeltItem();
        } else if (OctaKeyBindings.openBinding.consumeClick()) {
            Octa.debug("Open Gui Step0");
            this.openArtilleryGui();
        } else if (TetraKeyMappings.accessBinding.isDown() && this.mc.isWindowActive() && !this.isActive) {
            this.showView();
        }

    }

    public void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        if (!TetraKeyMappings.accessBinding.isDown() && this.isActive) {
            this.hideView();
        }

        this.draw(graphics);
    }

    public void draw(GuiGraphics graphics) {
        super.draw(graphics);
        if (this.isVisible()) {
            Window window = this.mc.getWindow();
            int mouseX = (int)(this.mc.mouseHandler.xpos() * (double)this.width / (double)window.getScreenWidth());
            int mouseY = (int)(this.mc.mouseHandler.ypos() * (double)this.height / (double)window.getScreenHeight());
            this.updateFocusState(0, 0, mouseX, mouseY);
        }

    }

    private void showView() {
        boolean canOpen = this.updateGuiData();
        if (canOpen) {
            this.toggleActive(true);
            this.mc.mouseHandler.releaseMouse();
            this.openTime = System.currentTimeMillis();
        }

    }

    private void hideView() {
        this.toggleActive(false);
        this.mc.mouseHandler.grabMouse();
        int focusIndex = this.getFocusIndex();
        if (focusIndex != -1) {
            this.equipToolbeltItem(this.getFocusType(), focusIndex, this.getFocusHand());
        } else if (System.currentTimeMillis() - this.openTime < 500L) {
            this.quickEquip();
        }

        this.holosphereGroup.performActions();
    }

    private void openArtilleryGui() {
        if (this.mc.player != null) {
            ItemStack itemStack = this.mc.player.getMainHandItem();
            if (!itemStack.isEmpty()) {
                Octa.debug("Open Gui Step1");
                TetraMod.packetHandler.sendToServer(new OpenArtilleryGuiPacket());
            }
        }
    }

    private boolean updateGuiData() {
        boolean canShow = false;
        ItemStack toolbeltStack = ToolbeltHelper.findToolbelt(this.mc.player);
        if (!toolbeltStack.isEmpty()) {
            this.quickslotGroup.setInventory(new QuickslotInventory(toolbeltStack));
            canShow = true;
        } else {
            this.quickslotGroup.clear();
        }

        ItemStack holosphereStack = ModularHolosphereItem.findHolosphere(this.mc.player);
        if (!holosphereStack.isEmpty()) {
            this.holosphereGroup.update(holosphereStack);
            canShow = true;
        } else {
            this.holosphereGroup.clear();
        }

        return canShow;
    }

    private void equipToolbeltItem(ToolbeltSlotType slotType, int toolbeltItemIndex, InteractionHand hand) {
        if (toolbeltItemIndex > -1) {
            EquipToolbeltItemPacket packet = new EquipToolbeltItemPacket(slotType, toolbeltItemIndex, hand);
            TetraMod.packetHandler.sendToServer(packet);
            ToolbeltHelper.equipItemFromToolbelt(this.mc.player, slotType, toolbeltItemIndex, hand);
        }

    }

    private void storeToolbeltItem() {
        boolean storeItemSuccess = ToolbeltHelper.storeItemInToolbelt(this.mc.player);
        StoreToolbeltItemPacket packet = new StoreToolbeltItemPacket();
        TetraMod.packetHandler.sendToServer(packet);
        if (!storeItemSuccess) {
            this.mc.player.displayClientMessage(Component.translatable("tetra.toolbelt.full"), true);
        }

    }

    private void quickEquip() {
        if (this.mc.hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult raytrace = (BlockHitResult)this.mc.hitResult;
            BlockState blockState = this.mc.level.getBlockState(raytrace.getBlockPos());
            int index = ToolbeltHelper.getQuickAccessSlotIndex(this.mc.player, this.mc.hitResult, blockState);
            if (index > -1) {
                this.equipToolbeltItem(ToolbeltSlotType.quickslot, index, InteractionHand.MAIN_HAND);
            }
        }

    }

    public ToolbeltSlotType getFocusType() {
        return ToolbeltSlotType.quickslot;
    }

    public int getFocusIndex() {
        return this.quickslotGroup.getFocus();
    }

    public InteractionHand getFocusHand() {
        return this.quickslotGroup.getHand();
    }
}
