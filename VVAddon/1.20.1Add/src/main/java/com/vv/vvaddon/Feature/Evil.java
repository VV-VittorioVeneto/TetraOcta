package com.vv.vvaddon.Feature;

import com.vv.vvaddon.Config.VVAddonConfig;
import com.vv.vvaddon.Init.VACoe;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.items.modular.ModularItem;

public class Evil {
    @SuppressWarnings("null")
    public static void execute(Player player, ModularItem mitem, ItemStack item){
        int damage_left = mitem.getMaxDamage(item) - mitem.getDamage(item);
        if(damage_left < VACoe.evil_damage){
            return;
        }else if(VVAddonConfig.Evil_switch.get()){
            if(player.hasEffect(MobEffects.BAD_OMEN)){
                int amplifier = player.getEffect(MobEffects.BAD_OMEN).getAmplifier();
                if (amplifier >= VACoe.evil_level - 1){
                    return;
                }else{                           
                    int duration = player.getEffect(MobEffects.BAD_OMEN).getDuration();
                    player.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, duration, amplifier + 1));
                }
            }else{                 
                player.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, VACoe.evil_time * 1200, 0));
            }
        }else{
            if(player.hasEffect(MobEffects.BAD_OMEN)){
                return;
            }else{
                player.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, VACoe.evil_time * 1200, VACoe.evil_level - 1));
            }
        }
        mitem.setDamage(item, mitem.getDamage(item) + VACoe.evil_damage);
    }
}
