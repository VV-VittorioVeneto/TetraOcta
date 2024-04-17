package com.vv.vvaddon.Feature;

import com.vv.vvaddon.Config.VVAddonConfig;
import com.vv.vvaddon.Init.VACoe;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.items.modular.ModularItem;

public class Honor {
    @SuppressWarnings("null")
    public static void execute(Player player, ModularItem mitem, ItemStack item){
        int damage_left = mitem.getMaxDamage(item) - mitem.getDamage(item);
        if(damage_left < VACoe.honor_damage){
            return;
        }else if(VVAddonConfig.Honor_switch.get()){
            if(player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)){
                int amplifier = player.getEffect(MobEffects.HERO_OF_THE_VILLAGE).getAmplifier();
                if (amplifier >= VACoe.honor_level - 1){
                    return;
                }else{                           
                    int duration = player.getEffect(MobEffects.HERO_OF_THE_VILLAGE).getDuration();
                    player.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, duration, amplifier + 1));
                }
            }else{                 
                player.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, VACoe.honor_time * 1200, 0));
            }
        }else{
            if(player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)){
                return;
            }else{
                player.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, VACoe.honor_time * 1200, VACoe.honor_level - 1));
            }
        }
        mitem.setDamage(item, mitem.getDamage(item) + VACoe.honor_damage);
    }
}
