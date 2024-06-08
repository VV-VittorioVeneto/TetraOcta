package com.vv.vvaddon.Feature;

import com.vv.vvaddon.Handler.VVAddonEventHandler;
import com.vv.vvaddon.Init.VACoe;

import net.minecraft.world.entity.player.Player;

public class Combo {
    public static float execute(Player player){
        float bonus = 0.0F;
        if(!VVAddonEventHandler.hashmap_combo.containsKey(player)){
            int new_combo = 0;
            VVAddonEventHandler.hashmap_combo.put(player, new_combo);
        };
        int combo = VVAddonEventHandler.hashmap_combo.get(player);
        bonus = combo * (float)VACoe.combo_bonus;
        VVAddonEventHandler.hashmap_combo.replace(player, (combo < VACoe.combo_max) ? combo + 1 : VACoe.combo_max);
        return bonus;
    }
}
