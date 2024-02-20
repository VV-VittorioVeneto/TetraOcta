package com.vv.vvaddon;

import java.util.HashMap;

import com.vv.vvaddon.Effect.*;
import com.vv.vvaddon.Feature.*;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.ModularItem;

public class VVAddonEffect {
    
    public class RE{
        public int combo;
        public double health;
        public float damage;
    }

    HashMap<Player,RE> hashmap = new HashMap<>();
    
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
    
    @SubscribeEvent
    public void tickEvent(TickEvent.PlayerTickEvent event){
        final Player player = event.player;
        final LevelAccessor WorldIn = player.level;
        final ItemStack heldStack = ((LivingEntity) player).getMainHandItem();
        if (!WorldIn.isClientSide() && player.tickCount % 20 == 0){
            MainVVAddon.LOGGER.info("tick is" + player.tickCount);
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
        if (!WorldIn.isClientSide()){
            if (heldStack.getItem() instanceof ModularItem item) {
                final int level_honor = item.getEffectLevel(heldStack, honor);
                final int level_evil = item.getEffectLevel(heldStack, evil);

                if(level_honor > 0)Honor.execute(player, item, heldStack);
                if(level_evil > 0)Evil.execute(player, item, heldStack);
            }
        }
    }

    @SubscribeEvent
    public void attackEvent(LivingHurtEvent event){
        final Entity source = event.getSource().getEntity();
        final Entity entity = event.getEntity();
        if((source instanceof final Player player) && (entity != null)){
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
                    if(level_bloodlow > 0) final_damage = Blood.executelow(player, final_damage);
                    if(level_bloodfull > 0) final_damage = Blood.executefull(player, final_damage);
                    if(level_charge > 0) final_damage = Charge.execute(player, final_damage);
                    if(level_height > 0) final_damage = Height.execute(player, final_damage, entity);
                    if(level_hitaway > 0) HitAway.execute(player, entity);
                    if(level_sniper > 0) final_damage = Sniper.execute(player, final_damage, entity);
                    if(level_combo > 0){
                        if(hashmap.containsKey(player) != true){
                            RE newr = new RE();
                            newr.health = player.getHealth();
                            newr.damage = final_damage;
                            newr.combo = 0; 
                            hashmap.put(player, newr);
                        }; 
                        final_damage = Combo.execute(hashmap.get(player), player).damage;
                    } 
                    event.setAmount(final_damage);
                }
            }
        }
    }
}
