package com.vv.octa.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SculkPerfusionHelper {

    private static final String VALUE = "sculk_value";
    private static final String CAPACITY = "sculk_capacity";

    public boolean isBarVisible(ItemStack itemStack) {
        return getCapacity(itemStack) > 0 && getValue(itemStack) > 0;
    }

    public int getBarWidth(ItemStack itemStack) {
        return (int)((float)getValue(itemStack)/(float)getCapacity(itemStack) * 13.0F);
    }

    public int getCapacity(ItemStack itemStack) {
        if (itemStack.getTag() != null && !itemStack.getTag().contains(CAPACITY)) {
            return -1;
        }
        return itemStack.getOrCreateTag().getInt(CAPACITY);
    }

    public void setCapacity(ItemStack itemStack, int capacity) {
        itemStack.getOrCreateTag().putInt(CAPACITY, capacity);
    }

    public int getValue(ItemStack itemStack) {
        if (itemStack.getTag() != null && !itemStack.getTag().contains(VALUE)) {
            return -1;
        }
        return itemStack.getOrCreateTag().getInt(VALUE);
    }

    public void setValue(ItemStack itemStack, int value) {
        itemStack.getOrCreateTag().putInt(VALUE, value);
    }

    public boolean wearValue(ItemStack itemStack, int wear) {
        if (itemStack.getTag() != null && !itemStack.getTag().contains(VALUE)) {
            if(getValue(itemStack) > wear) {
                itemStack.getTag().putInt(VALUE, getValue(itemStack) - wear);
                return true;
            }else if(getValue(itemStack) > 0) {
                itemStack.getTag().putInt(VALUE, 0);
                return true;
            }
        }
        return false;
    }
}
