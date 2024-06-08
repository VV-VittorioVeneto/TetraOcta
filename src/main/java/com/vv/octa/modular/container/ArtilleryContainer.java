package com.vv.octa.modular.container;

import com.vv.octa.Octa;
import com.vv.octa.modular.ModularLaserArtilleryItem;
import com.vv.octa.modular.inventory.EmbeddedChipInventory;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import se.mickelus.mutil.gui.DisabledSlot;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.PredicateSlot;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class ArtilleryContainer extends AbstractContainerMenu {

    public static final int gapWidth = 28;
    public static final int slotsX = 120;
    public static final int slotsY = 108;
    public static RegistryObject<MenuType<ArtilleryContainer>> type;
    private final ItemStack artilleryStack;
    private final ModularLaserArtilleryItem artilleryItem;
    private final ArrayList<EmbeddedChipInventory> embeddedChipInventoryArrayList = new ArrayList<>();
    private final int slotNum;

    public ArtilleryContainer(int windowId, Container playerInventory, ItemStack laserArtilleryStack, Player player) {
        super(type.get(), windowId);
        Octa.debug("Open Gui Step4");
        this.artilleryStack = laserArtilleryStack;
        this.artilleryItem = (ModularLaserArtilleryItem) laserArtilleryStack.getItem();
        this.slotNum = artilleryItem.getSlotNum(laserArtilleryStack);
        EmbeddedChipInventory embeddedChipSlot;
        int i;
        int j;
        int x;
        int y;
        addEC();

        if (slotNum > 0) {
            for(i = 0; i < slotNum; ++i) {
                embeddedChipSlot = this.embeddedChipInventoryArrayList.get(i);
                x = slotsX + (i & 1) * gapWidth;
                y = slotsY - (i & 2) * gapWidth / 2;
                Objects.requireNonNull(embeddedChipSlot);
                EmbeddedChipInventory tode = this.embeddedChipInventoryArrayList.get(i);
                this.addSlot(new PredicateSlot(embeddedChipSlot, i, x, y, tode::isItemValid));
            }
        }
        for(i = 0; i < 3; ++i) {
            for(j = 0; j < 9; ++j) {
                Slot slot;
                if (artilleryStack.is(playerInventory.getItem(i * 9 + j + 9).getItem())) {
                    slot = new DisabledSlot(playerInventory, i * 9 + j + 9, j * 17 + 12, i * 17 + 142);
                } else {
                    slot = new Slot(playerInventory, i * 9 + j + 9, j * 17 + 12, i * 17 + 142);
                }

                this.addSlot(slot);
            }
        }
        for(i = 0; i < 9; ++i) {
            Slot slot;
            if (artilleryStack.is(playerInventory.getItem(i).getItem())) {
                slot = new DisabledSlot(playerInventory, i, i * 17 + 12, 197);
            } else {
                slot = new Slot(playerInventory, i, i * 17 + 12, 197);
            }

            this.addSlot(slot);
        }
    }

    private void addEC() {
        for (int i = 0 ; i < slotNum ; i++) {
            this.embeddedChipInventoryArrayList.add(new EmbeddedChipInventory("chipInventory", artilleryStack, 1, i));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static ArtilleryContainer create(int windowId, Inventory inv) {
        ItemStack itemStack = inv.player.getMainHandItem();
        return new ArtilleryContainer(windowId, inv, itemStack, inv.player);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public int getSlotNum() {
        return this.slotNum;
    }
}
