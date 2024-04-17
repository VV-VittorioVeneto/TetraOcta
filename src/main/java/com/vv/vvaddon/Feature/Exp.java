package com.vv.vvaddon.Feature;

import com.vv.vvaddon.Init.VACoe;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class Exp {
    public static void execute(Player player , Entity entity , int level){
        if(entity instanceof Mob mob){
            double reward = mob.getExperienceReward();
            switch (level) {
                case 1:  reward *= VACoe.exp_bonus_1;break;
                case 2:  reward *= VACoe.exp_bonus_2;break;
                case 3:  reward *= VACoe.exp_bonus_3;break;
                default: reward *= 0;
            }
            player.giveExperiencePoints((int)Math.ceil(reward));
        }
    }
}
