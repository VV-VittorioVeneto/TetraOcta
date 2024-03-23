package com.vv.vvaddon.Feature;

import com.vv.vvaddon.Config.VVAddonConfig;
import com.vv.vvaddon.Handler.VVAddonEventHandler;

import net.minecraft.world.entity.player.Player;

public class Combo {
    public static float execute(Player player , float damage){
        if(!VVAddonEventHandler.hashmap_combo.containsKey(player)){
            int new_combo = 0;
            VVAddonEventHandler.hashmap_combo.put(player, new_combo);
        };
        int combo = VVAddonEventHandler.hashmap_combo.get(player);
        damage *= 1 + combo * VVAddonConfig.Combo_bonus.get();
        VVAddonEventHandler.hashmap_combo.replace(player, (combo < VVAddonConfig.Combo_max.get()) ? combo + 1 : VVAddonConfig.Combo_max.get());
        return damage;
    }
}
