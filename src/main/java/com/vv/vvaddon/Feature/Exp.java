package com.vv.vvaddon.Feature;

import com.vv.vvaddon.Config.VVAddonConfig;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class Exp {
    public static void execute(Player player , Entity entity , int level){
        if(entity instanceof Mob mob){
            double reward = mob.getExperienceReward();
            switch (level) {
                case 1:  reward *= VVAddonConfig.Exp_bonus_1.get();break;
                case 2:  reward *= VVAddonConfig.Exp_bonus_2.get();break;
                case 3:  reward *= VVAddonConfig.Exp_bonus_3.get();break;
                default: reward *= 0;
            }
            player.giveExperiencePoints((int)Math.ceil(reward));
        }
    }
}
