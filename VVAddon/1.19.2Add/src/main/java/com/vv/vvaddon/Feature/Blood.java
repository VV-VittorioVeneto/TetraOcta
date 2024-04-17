package com.vv.vvaddon.Feature;

import com.vv.vvaddon.Init.VACoe;

import net.minecraft.world.entity.player.Player;

public class Blood {
    public static float executefull(Player player){
        final double rate = player.getHealth()/player.getMaxHealth();
        float bonus = 0.0F;
        if(rate > VACoe.fullblood_threshold)bonus = (float)VACoe.fullblood_bonus;
        return bonus;
    }
    public static float executelow(Player player){
        final double rate = player.getHealth()/player.getMaxHealth();
        float bonus = 0.0F;
        if(rate < VACoe.lowblood_threshold)bonus = (float)VACoe.lowblood_bonus;
        return bonus;
    }
}
