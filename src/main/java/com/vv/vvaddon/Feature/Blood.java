package com.vv.vvaddon.Feature;

import com.vv.vvaddon.Config.VVAddonConfig;

import net.minecraft.world.entity.player.Player;

public class Blood {
    public static float executefull(Player player , float damage){
        final double rate = player.getHealth()/player.getMaxHealth();
        if(rate > VVAddonConfig.Fullblood_threshold.get())damage *= 1 + VVAddonConfig.Fullblood_bonus.get();
        return damage;
    }
    public static float executelow(Player player , float damage){
        final double rate = player.getHealth()/player.getMaxHealth();
        if(rate < VVAddonConfig.Lowblood_threshold.get())damage *= 1 + VVAddonConfig.Lowblood_bonus.get();
        return damage;
    }
}
