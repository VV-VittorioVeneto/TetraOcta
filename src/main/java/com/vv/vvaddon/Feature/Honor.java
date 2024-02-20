package com.vv.vvaddon.Feature;

import com.vv.vvaddon.VVAddonConfig;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.items.modular.ModularItem;

public class Honor {
    @SuppressWarnings("null")
    public static void execute(Player player, ModularItem mitem, ItemStack item){
        int damage_left = mitem.getMaxDamage(item) - mitem.getDamage(item);
        if(damage_left < VVAddonConfig.Honor_damage.get()){
            return;
        }else if(VVAddonConfig.Honor_switch.get()){
            if(player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)){
                int amplifier = player.getEffect(MobEffects.HERO_OF_THE_VILLAGE).getAmplifier();
                if (amplifier >= VVAddonConfig.Honor_level.get()-1){
                    return;
                }else{                           
                    int duration = player.getEffect(MobEffects.HERO_OF_THE_VILLAGE).getDuration();
                    player.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, duration, amplifier + 1));
                }
            }else{                 
                player.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, VVAddonConfig.Honor_time.get() * 1200, 0));
            }
        }else{
            if(player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)){
                return;
            }else{
                player.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, VVAddonConfig.Honor_time.get() * 1200, VVAddonConfig.Honor_level.get()-1));
            }
        }
        mitem.setDamage(item, mitem.getDamage(item) + VVAddonConfig.Honor_damage.get());
    }
}
