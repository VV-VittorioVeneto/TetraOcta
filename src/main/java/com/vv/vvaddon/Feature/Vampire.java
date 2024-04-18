package com.vv.vvaddon.Feature;

import com.vv.vvaddon.Init.VACoe;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class Vampire {
    public static boolean execute(float damage, Player player, ItemStack item){
        final int level_bonus = item.getEnchantmentLevel(Enchantments.MOB_LOOTING) + 1;
        final Level level = player.level();
        final float healamount =(float) (((damage * VACoe.vampire_rate) < VACoe.vampire_maxamount) ? damage * (float)VACoe.vampire_rate : VACoe.vampire_maxamount);
        if(level.random.nextFloat() <= (1.0D - VACoe.vampire_chance)/4.0){
            return false;
        }
        int i;
        for(i = 0 ; i < level_bonus ; i++){
            if(level.random.nextFloat() <= VACoe.vampire_chance){
                player.heal(healamount);
                return true;
            }
        }
        return false;
    }
}
