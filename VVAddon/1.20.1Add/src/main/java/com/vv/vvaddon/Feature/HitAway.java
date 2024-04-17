package com.vv.vvaddon.Feature;

import com.vv.vvaddon.Init.VACoe;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

public class HitAway {
    public static void execute(Player player , Entity entity){
        var targetingCondition = TargetingConditions.forCombat().ignoreLineOfSight().selector(e -> {
            return (true);
        });
        player.level().getNearbyEntities(Mob.class, targetingCondition, player, entity.getBoundingBox().inflate(5)).forEach(entitynear ->{
            final double delta_x = entity.getPosition(1).x - entitynear.getPosition(1).x;
            final double delta_z = entity.getPosition(1).z - entitynear.getPosition(1).z;
            final double dis = Math.sqrt(Math.pow(delta_x,2) + Math.pow(delta_z,2));
            double strength = 2 / (dis < 1 ? 1 : dis);
            double x = (-strength * delta_x / dis) * VACoe.hitaway_hor_coe;
            double y = 0.4 * VACoe.hitaway_ver_coe;
            double z = (-strength * delta_z / dis) * VACoe.hitaway_hor_coe;
            entitynear.setDeltaMovement(x, y, z);
        });
    }
    
}
