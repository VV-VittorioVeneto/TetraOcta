package com.vv.vvaddon.Feature;

import com.vv.vvaddon.Config.VVAddonConfig;
import com.vv.vvaddon.Init.VACoe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import se.mickelus.tetra.items.modular.ModularItem;

public class Homology {
    
    public static void execute(Player player , ModularItem mitem , ItemStack item){
        int i = 0;
        for(i = 1 ; i <= player.getInventory().getContainerSize() ; i++) {
            ItemStack itemstack = player.getInventory().getItem(i);
            ResourceLocation resourceLocation = ForgeRegistries.ITEMS.getKey(itemstack.getItem());
            String itemName = resourceLocation.toString();
            if (VVAddonConfig.Homology_item.get().contains(itemName)){
                int effnum = itemstack.getCount() > VACoe.homology_maxnum ? VACoe.homology_maxnum : itemstack.getCount();
                int efffix = Mth.ceil(effnum * VACoe.homology_speed);
                int damage = mitem.getDamage(item);
                efffix = damage>efffix ? efffix : damage;
                mitem.setDamage(item, (int)damage - efffix);
                break;
            }
        }
    }
}
