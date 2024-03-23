package com.vv.vvaddon.Handler;

import java.util.HashMap;

import com.vv.vvaddon.Config.VVAddonConfig;
import com.vv.vvaddon.Entity.PhantomArrow;
import com.vv.vvaddon.Feature.*;
import com.vv.vvaddon.Feature.Effect.*;
import com.vv.vvaddon.TestE.Phantom;
import com.vv.vvaddon.TestE.Rush;
import com.vv.vvaddon.Utils.PEMap;
import com.vv.vvaddon.Utils.PEMap.LeafNode;

import net.minecraft.world.damagesource.DamageSource;
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
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.ModularItem;

public class VVAddonEventHandler {

    public static HashMap<Player, Integer> hashmap_combo = new HashMap<>();
    public static PEMap pemap = new PEMap();
    
    public static final ItemEffect nightvisioneffect = ItemEffect.get("vvaddon:nightvisioneffect");
    public static final ItemEffect hasteeffect = ItemEffect.get("vvaddon:hasteeffect");
    public static final ItemEffect speedeffect = ItemEffect.get("vvaddon:speedeffect");
    public static final ItemEffect strengtheffect = ItemEffect.get("vvaddon:strengtheffect");
    public static final ItemEffect bloodlow = ItemEffect.get("vvaddon:bloodlow");
    public static final ItemEffect bloodfull = ItemEffect.get("vvaddon:bloodfull");
    public static final ItemEffect charge = ItemEffect.get("vvaddon:charge");
    public static final ItemEffect combo = ItemEffect.get("vvaddon:combo");
    public static final ItemEffect height = ItemEffect.get("vvaddon:height");
    public static final ItemEffect hitaway = ItemEffect.get("vvaddon:hitaway");
    public static final ItemEffect sniper = ItemEffect.get("vvaddon:sniper");
    public static final ItemEffect homology = ItemEffect.get("vvaddon:homology");
    public static final ItemEffect honor = ItemEffect.get("vvaddon:honor");
    public static final ItemEffect evil = ItemEffect.get("vvaddon:evil");
    public static final ItemEffect exp_i = ItemEffect.get("vvaddon:exp_i");
    public static final ItemEffect exp_ii = ItemEffect.get("vvaddon:exp_ii");
    public static final ItemEffect exp_iii = ItemEffect.get("vvaddon:exp_iii");
    public static final ItemEffect rush = ItemEffect.get("vvaddon:rush");
    public static final ItemEffect phantom_rain = ItemEffect.get("vvaddon:phantom_rain");
    public static final ItemEffect phantom_persuit = ItemEffect.get("vvaddon:phantom_persuit");
    
    @SubscribeEvent
    public void tickEvent(TickEvent.PlayerTickEvent event){
        if(event.phase.equals(Phase.END))return;

        final Player player = event.player;
        final LevelAccessor WorldIn = player.level;
        final ItemStack heldStack = ((LivingEntity) player).getMainHandItem();
        
        if (!WorldIn.isClientSide() && player.tickCount % 20 == 0){
            if(pemap.hasKey(player)){
                LeafNode temp = pemap.getKey(player).pair;
                while(temp != null){
                    if(temp.level == 1)Phantom.execute_effect(temp.item, player, temp.entity);
                    if(temp.level == 2)Phantom.execute(temp.item, player, temp.entity, 1);
                    temp.count--;
                    if(temp.count <= 0)pemap.drop(player, temp.entity);
                    temp = temp.next;
                }
            }
        }

        if (!WorldIn.isClientSide() && player.tickCount % 20 == 0){
            if (heldStack.getItem() instanceof ModularItem item) { 
                final int level_nightvisioneffect = item.getEffectLevel(heldStack, nightvisioneffect);
                final int level_hasteeffect = item.getEffectLevel(heldStack, hasteeffect);
                final int level_speedeffect = item.getEffectLevel(heldStack, speedeffect);
                final int level_strengtheffect = item.getEffectLevel(heldStack, strengtheffect);
                final int level_homology = item.getEffectLevel(heldStack, homology);

                if(level_nightvisioneffect > 0)NightVisionProcedure.execute(player);
                if(level_hasteeffect > 0)HasteProcedure.execute(player);
                if(level_speedeffect > 0)SpeedProcedure.execute(player);
                if(level_strengtheffect > 0)StrengthProcedure.execute(player);
                if(level_homology > 0)Homology.execute(player, item ,heldStack);
            }
        }
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent.RightClickItem event){
        final Player player = event.getEntity();
        final LevelAccessor WorldIn = player.getLevel();
        final ItemStack heldStack = player.getMainHandItem();
        
        if (heldStack.getItem() instanceof ModularItem item){
            final int level_honor = item.getEffectLevel(heldStack, honor);
            final int level_evil = item.getEffectLevel(heldStack, evil);
            final int level_rush = item.getEffectLevel(heldStack, rush);

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
            final LevelAccessor WorldIn = player.level;
            final ItemStack heldStack = player.getMainHandItem();
            if (!WorldIn.isClientSide()){
                if (heldStack.getItem() instanceof ModularItem item) { 
                    int level_exp = 0;

                    if(item.getEffectLevel(heldStack, exp_i) == 1) level_exp = 1;
                    if(item.getEffectLevel(heldStack, exp_ii) == 1) level_exp = 2;
                    if(item.getEffectLevel(heldStack, exp_iii) == 1) level_exp = 3;

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
            if(damagesource.isProjectile() && damagesource.getDirectEntity() instanceof PhantomArrow phantom_arrow){
                if(phantom_arrow.getPhantomArrowImmu())entity.invulnerableTime = 0;
            }
            float final_damage = event.getAmount();
            final LevelAccessor WorldIn = player.level;
            final ItemStack heldStack = player.getMainHandItem();
            if (!WorldIn.isClientSide()){
                if (heldStack.getItem() instanceof ModularItem item) { 
                    final int level_bloodlow = item.getEffectLevel(heldStack, bloodlow);
                    final int level_bloodfull = item.getEffectLevel(heldStack, bloodfull);
                    final int level_charge = item.getEffectLevel(heldStack, charge);
                    final int level_combo = item.getEffectLevel(heldStack, combo);
                    final int level_height = item.getEffectLevel(heldStack, height);
                    final int level_hitaway = item.getEffectLevel(heldStack, hitaway);
                    final int level_sniper = item.getEffectLevel(heldStack, sniper);
                    final int level_phantomrain = item.getEffectLevel(heldStack, phantom_rain);
                    final int level_phantompersuit = item.getEffectLevel(heldStack, phantom_persuit);
                    
                    if(level_bloodlow > 0) final_damage = Blood.executelow(player, final_damage);
                    if(level_bloodfull > 0) final_damage = Blood.executefull(player, final_damage);
                    if(level_charge > 0) final_damage = Charge.execute(player, final_damage);
                    if(level_height > 0) final_damage = Height.execute(player, final_damage, entity);
                    if(level_hitaway > 0) HitAway.execute(player, entity);
                    if(level_sniper > 0) final_damage = Sniper.execute(player, final_damage, entity);
                    if(level_combo > 0)final_damage = Combo.execute(player, final_damage);

                    
                    if(damagesource.isProjectile() && !(damagesource.getDirectEntity() instanceof PhantomArrow phantom_arrow && !phantom_arrow.getPhantomArrowTrigger())){
                        if(level_phantomrain > 0 && level_phantompersuit == 0)Phantom.execute(heldStack, player ,entity,0);
                        if(level_phantomrain == 0 && level_phantompersuit > 0)pemap.update(player, entity, 5, heldStack,1);
                        if(level_phantomrain > 0 && level_phantompersuit > 0)pemap.update(player, entity, 3, heldStack,2);
                    }

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
