package com.vv.vvaddon.Handler;

import java.util.HashMap;

import com.vv.vvaddon.Config.VVAddonConfig;
import com.vv.vvaddon.Entity.PhantomArrow;
import com.vv.vvaddon.Feature.*;
import com.vv.vvaddon.Init.VACoe;
import com.vv.vvaddon.Utils.PEMap;
import com.vv.vvaddon.Init.VAName;
import com.vv.vvaddon.Utils.PEMap.LeafNode;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.items.modular.ModularItem;

public class VVAddonEventHandler {

    public static HashMap<Player, Integer> hashmap_combo = new HashMap<>();
    public static PEMap pemap = new PEMap();
    
    @SubscribeEvent
    public void tickEvent(TickEvent.PlayerTickEvent event){
        if(event.phase.equals(Phase.END))return;

        final Player player = event.player;
        final LevelAccessor WorldIn = player.level();
        final ItemStack heldStack = ((LivingEntity) player).getMainHandItem();
        
        if (!WorldIn.isClientSide() && player.tickCount % 20 == 0){
            if(pemap.hasKey(player)){
                LeafNode temp = pemap.getKey(player).pair;
                while(temp != null){
                    if(temp.level == 1)Phantom.execute_effect(temp.item, player, temp.entity);
                    if(temp.level == 2)Phantom.execute(temp.item, player, temp.entity, 1, VACoe.phantom_pr_damage, VACoe.phantom_pr_num);
                    temp.count--;
                    if(temp.count <= 0)pemap.drop(player, temp.entity);
                    temp = temp.next;
                }
            }
        }

        if (!WorldIn.isClientSide() && player.tickCount % 20 == 0){
            if (heldStack.getItem() instanceof ModularItem item) { 
                final int level_nightvisioneffect = item.getEffectLevel(heldStack, VAName.nightvisioneffect);
                final int level_hasteeffect = item.getEffectLevel(heldStack, VAName.hasteeffect);
                final int level_speedeffect = item.getEffectLevel(heldStack, VAName.speedeffect);
                final int level_strengtheffect = item.getEffectLevel(heldStack, VAName.strengtheffect);
                final int level_homology = item.getEffectLevel(heldStack, VAName.homology);

                if(level_nightvisioneffect > 0)EffectProcedure.execute(player, MobEffects.NIGHT_VISION);
                if(level_hasteeffect > 0)EffectProcedure.execute(player, MobEffects.DIG_SPEED);
                if(level_speedeffect > 0)EffectProcedure.execute(player, MobEffects.MOVEMENT_SPEED);
                if(level_strengtheffect > 0)EffectProcedure.execute(player, MobEffects.DAMAGE_BOOST);
                if(level_homology > 0)Homology.execute(player, item ,heldStack);
            }
        }
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent.RightClickItem event){
        final Player player = event.getEntity();
        final LevelAccessor WorldIn = player.level();
        final ItemStack heldStack = player.getMainHandItem();
        
        if (heldStack.getItem() instanceof ModularItem item){
            final int level_honor = item.getEffectLevel(heldStack, VAName.honor);
            final int level_evil = item.getEffectLevel(heldStack, VAName.evil);
            final int level_rush = item.getEffectLevel(heldStack, VAName.rush);

            if(level_rush > 0)Rush.executeb(player, heldStack, WorldIn);
            
            if (!WorldIn.isClientSide()) {
                if(level_honor > 0)Honor.execute(player, item, heldStack);
                if(level_evil > 0)Evil.execute(player, item, heldStack);
            }
        }
    }

    @SubscribeEvent
    public void attackEvent(LivingDeathEvent event){
        final Entity source = event.getSource().getEntity();
        final Entity entity = event.getEntity();
        pemap.drop(entity);

        if((source instanceof final Player player) && (entity != null)){
            final LevelAccessor WorldIn = player.level();
            final ItemStack heldStack = player.getMainHandItem();
            if (!WorldIn.isClientSide()){
                if (heldStack.getItem() instanceof ModularItem item) { 
                    int level_exp = 0;

                    if(item.getEffectLevel(heldStack, VAName.exp_i) == 1) level_exp = 1;
                    if(item.getEffectLevel(heldStack, VAName.exp_ii) == 1) level_exp = 2;
                    if(item.getEffectLevel(heldStack, VAName.exp_iii) == 1) level_exp = 3;

                    Exp.execute(player, entity, level_exp);
                }
            }
        }
    }

    @SubscribeEvent
    public void attackEvent(LivingHurtEvent event){
        final Entity source = event.getSource().getEntity();
        final Entity entity = event.getEntity();
        final DamageSource damagesource = event.getSource();
        if((source instanceof final Player player) && (entity != null)){
            float final_damage = event.getAmount();
            if(damagesource.getDirectEntity() instanceof PhantomArrow phantom_arrow){
                if(phantom_arrow.getPhantomArrowImmu())entity.invulnerableTime = 0;
                final_damage = phantom_arrow.getPhantomArrowDamage();
            }
            final LevelAccessor WorldIn = player.level();
            final ItemStack heldStack = player.getMainHandItem();
            if (!WorldIn.isClientSide()){
                if (heldStack.getItem() instanceof ModularItem item) { 
                    final int level_hitaway = item.getEffectLevel(heldStack, VAName.hitaway);
                    final int level_phantomrain = item.getEffectLevel(heldStack, VAName.phantom_rain);
                    final int level_phantompersuit = item.getEffectLevel(heldStack, VAName.phantom_persuit);

                    if(level_hitaway > 0) HitAway.execute(player, entity);
                    if(level_hitaway > 0) Confuse.execute(player, entity);

                    if(!(damagesource.getDirectEntity() instanceof PhantomArrow phantom_arrow && !phantom_arrow.getPhantomArrowTrigger())){
                        if(level_phantomrain > 0 && level_phantompersuit == 0)Phantom.execute(heldStack, player ,entity, 0, VVAddonConfig.Phantom_rain_damage.get(), VVAddonConfig.Phantom_rain_num.get());
                        if(level_phantomrain == 0 && level_phantompersuit > 0)pemap.update(player, entity, VVAddonConfig.Phantom_persuit_count.get(), heldStack,1);
                        if(level_phantomrain > 0 && level_phantompersuit > 0)pemap.update(player, entity, VVAddonConfig.Phantom_pr_count.get(), heldStack,2);
                    }
                    
                    final_damage *= VVAddonDamageHandler.getFinalMultiplyCoe(heldStack, player, entity);
                    event.setAmount(final_damage);
                }
            }
        }       
        if(entity instanceof final Player player){
            if(hashmap_combo.containsKey(player) && VVAddonConfig.Combo_hurt.get()){
                hashmap_combo.replace(player, 0);
            }
        }
    }
}
