package com.vv.octa.modular.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import se.mickelus.tetra.items.modular.ModularItem;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class EmbeddedChipInventory implements Container {

    protected static final String slotKey = "slot";
    protected final String inventoryKey;
    protected ItemStack laserArtilleryStack;
    protected int id;
    protected NonNullList<ItemStack> inventoryContents;
    protected int numSlots = 0;
    protected int maxSize = 0;
    protected Predicate<ItemStack> predicate = (itemStack) -> true;

    public EmbeddedChipInventory(String inventoryKey, ItemStack itemStack, int maxSize, int id) {
        this.inventoryKey = inventoryKey + "_" + id;
        this.id = id;
        this.laserArtilleryStack = itemStack;
        this.maxSize = maxSize;
        this.inventoryContents = NonNullList.withSize(maxSize, ItemStack.EMPTY);
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public @NotNull ItemStack getItem(int index) {
        return (index == 0) ? this.inventoryContents.get(index) : ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItem(int index, int count) {
        ItemStack itemStack = ContainerHelper.removeItem(this.inventoryContents, index, count);
        if (!itemStack.isEmpty()) {
            this.setChanged();
        }
        return itemStack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int index) {
        ItemStack itemStack = this.inventoryContents.get(index);
        if (!itemStack.isEmpty()) {
            this.inventoryContents.set(index, ItemStack.EMPTY);
        }
        return itemStack;
    }

    @Override
    public void setItem(int index, ItemStack itemStack) {
        if (index != 0) return;
        this.inventoryContents.set(index, itemStack);
        if (!itemStack.isEmpty() && itemStack.getCount() > this.getMaxStackSize()) {
            itemStack.setCount(this.getMaxStackSize());
        }
        this.setChanged();
    }

    @Override
    public void setChanged() {
        for(int i = 0; i < this.getContainerSize(); ++i) {
            if (this.getItem(i).getCount() == 0) {
                this.inventoryContents.set(i, ItemStack.EMPTY);
            }
        }
        this.writeToNBT(this.laserArtilleryStack.getOrCreateTag());
    }


    public void writeToNBT(CompoundTag tag) {
        ListTag items = new ListTag();

        for(int i = 0; i < this.maxSize; ++i) {
            this.getItem(i);
            CompoundTag compound = new CompoundTag();
            this.getItem(i).save(compound);
            compound.putByte("slot", (byte)i);
            items.add(compound);
        }

        tag.put(this.inventoryKey, items);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.inventoryContents.clear();
    }

    public boolean isItemValid(ItemStack itemStack) {
        return itemStack.getItem() instanceof ModularItem;
    }
}
