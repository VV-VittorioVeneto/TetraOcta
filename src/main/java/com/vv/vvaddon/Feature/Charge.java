package com.vv.vvaddon.Feature;

import com.vv.vvaddon.Config.VVAddonConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class Charge {
    
    public static float execute(Player player , float damage){
        int num = 0 , i = 0;
        for(i = 1 ; i <= player.getInventory().getContainerSize() ; i++) {
            ItemStack itemstack = player.getInventory().getItem(i);
            ResourceLocation resourceLocation = ForgeRegistries.ITEMS.getKey(itemstack.getItem());
            String itemName = resourceLocation.toString();
            if (VVAddonConfig.Charge_item.get().contains(itemName)){
                num = itemstack.getCount();
                itemstack.setCount(num-1);
                damage *= 1 + VVAddonConfig.Charge_bonus.get();
                break;
            }
        }
        return damage;
    }
}
