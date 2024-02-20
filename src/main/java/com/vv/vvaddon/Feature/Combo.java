package com.vv.vvaddon.Feature;

import com.vv.vvaddon.VVAddonConfig;
import com.vv.vvaddon.VVAddonEffect.RE;

import net.minecraft.world.entity.player.Player;

public class Combo {
    public static RE execute(RE re , Player source){
        int maxcombo = VVAddonConfig.Combo_max.get();
        double bonus = VVAddonConfig.Combo_bonus.get();
        if (source.getHealth() < re.health && VVAddonConfig.Combo_hurt.get()){
            re.combo = 0;
        }else{
            re.combo=(re.combo<maxcombo)?re.combo+1:maxcombo;
        };
        re.damage *= 1 + re.combo * bonus;
        re.health = source.getHealth();
        return re;
    }
}
