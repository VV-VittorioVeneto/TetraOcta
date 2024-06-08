package com.vv.octa.handler;

import com.vv.octa.Octa;
import com.vv.octa.effect.MaceImpactEffect;

import com.vv.octa.network.ClientBoundLaserParticles;
import com.vv.octa.setup.Messages;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.items.modular.ModularItem;

@SuppressWarnings("unused")
public class OctaEventHandler {
    public static void addParticle(LevelAccessor world, ParticleOptions particle, Vec3 pos, Vec3 vel) {
        world.addParticle(particle, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
    }

    @SubscribeEvent
    public void tickEvent(TickEvent.PlayerTickEvent event){
        if(event.phase.equals(Phase.END))return;

        final Player player = event.player;
        final LevelAccessor WorldIn = player.level();
        final ItemStack heldStack = player.getMainHandItem();

        if(WorldIn.isClientSide() && player.tickCount % 20 == 0){
        }
//        if(player.getDeltaMovement().y < -0.1){
//            Octa.debug("Speed: " + player.getDeltaMovement().length(),
//                "Dis: " + player.fallDistance);
//        }
        MaceImpactEffect.hitEntity(player);


    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent.RightClickItem event){
        final Player player = event.getEntity();
        final LevelAccessor WorldIn = player.level();
        final ItemStack heldStack = player.getMainHandItem();
        // if(heldStack.getItem() instanceof ModularItem mitem){
        //     Octa.debug(heldStack.toString());
        //     List<String> strs = mitem.getAllModules(heldStack)
        //         .stream().map(m -> m.getVariantData(heldStack).key).collect(Collectors.toList());
        //         Octa.debug(strs.size()+"");
        //         for(String str : strs){
        //             Octa.debug("Key: " + str);
        //         }
        // }
        // if(heldStack.getItem() instanceof ModularArrowItem arrowItem){
        //     ItemStack arrowStack = heldStack.copy();
        //     int rgb = arrowItem.getAllModules(arrowStack).stream().filter(m -> m.getSlot().contains("arrow/head")).map(m -> m.getVariantData(arrowStack).models[0].tint).collect(Collectors.toList()).get(0);
        //     int alpha = (3 == 4) ? 127 : 255;
        //     int color = (alpha << 24) + rgb;
        //     Octa.debug(Integer.toHexString(rgb));
        //     Octa.debug(Integer.toHexString(alpha));
        //     Octa.debug(Integer.toHexString(color));
        // }
        if (heldStack.getItem() instanceof ModularItem modularItem) {
            modularItem.getAllModules(heldStack).forEach(m -> {
                Octa.debug(m.getKey() + " Tweakable: " + m.isTweakable(heldStack));
            });
        }

    }

    @SubscribeEvent
    public void attackEvent(LivingDeathEvent event){
        final Entity source = event.getSource().getEntity();
        final Entity entity = event.getEntity();
    }

    @SubscribeEvent
    public void attackEvent(LivingHurtEvent event){
        final Entity source = event.getSource().getEntity();
        final Entity entity = event.getEntity();
        final DamageSource damagesource = event.getSource();
    }


}
