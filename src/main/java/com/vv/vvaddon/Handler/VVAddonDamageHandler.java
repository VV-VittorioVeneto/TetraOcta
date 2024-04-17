package com.vv.vvaddon.Handler;

import com.vv.vvaddon.Feature.Blood;
import com.vv.vvaddon.Feature.Charge;
import com.vv.vvaddon.Feature.Combo;
import com.vv.vvaddon.Feature.Height;
import com.vv.vvaddon.Feature.Sniper;
import com.vv.vvaddon.Init.VAName;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.items.modular.ModularItem;

public class VVAddonDamageHandler {
    private final static int multiplyfieldnum = 10;

    public static float getFinalMultiplyCoe(ItemStack heldStack, Player player, Entity entity){
        float[] multiplyfield = new float[multiplyfieldnum]; 
        float finalcoe = 1;
        if(heldStack.getItem() instanceof ModularItem item){
            final int level_bloodlow = item.getEffectLevel(heldStack, VAName.bloodlow);
            final int level_bloodfull = item.getEffectLevel(heldStack, VAName.bloodfull);
            final int level_charge = item.getEffectLevel(heldStack, VAName.charge);
            final int level_combo = item.getEffectLevel(heldStack, VAName.combo);
            final int level_height = item.getEffectLevel(heldStack, VAName.height);
            final int level_sniper = item.getEffectLevel(heldStack, VAName.sniper);
            
            if(level_bloodlow > 0) multiplyfield[0] = Blood.executelow(player);
            if(level_bloodfull > 0) multiplyfield[0] = Blood.executefull(player);
            if(level_charge > 0) multiplyfield[0] = Charge.execute(player);
            if(level_height > 0) multiplyfield[0] = Height.execute(player, entity);
            if(level_sniper > 0) multiplyfield[0] = Sniper.execute(player, entity);
            if(level_combo > 0) multiplyfield[0] = Combo.execute(player);
        }

        for(float f : multiplyfield){
            finalcoe *= (1.0F + f);
        }
        return finalcoe;
    }
}