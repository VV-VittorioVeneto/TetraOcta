package com.vv.vvaddon.Feature;

import java.util.Vector;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

public class Confuse {
    private static Vector<LivingEntity> entity_n = new Vector<>();
    public static boolean execute(Player player, Entity entity){
        if(entity instanceof LivingEntity livingentity){
            if(livingentity.getMaxHealth() > 100.0F){
                return false;
            }
        }
        entity_n.clear();
        var targetingCondition = TargetingConditions.forCombat().ignoreLineOfSight().selector(e -> {
            return (true);
        });
        if(entity instanceof Mob mob){
            entity.level().getNearbyEntities(Mob.class, targetingCondition, mob, entity.getBoundingBox().inflate(20D)).forEach(entitynear ->{
                entity_n.add(entitynear);
            });
            for(Entity entity_near : entity_n){
                if(!(entity_near.isAlliedTo(entity) || entity_near.isAlliedTo(player))){
                    mob.setTarget((LivingEntity)entity_near);
                    return true;
                }
            }
            for(Entity entity_near : entity_n){
                if(!(entity_near.isAlliedTo(player))){
                    mob.setTarget((LivingEntity)entity_near);
                    return true;
                }
            }
        };
        return false;
    }
}
