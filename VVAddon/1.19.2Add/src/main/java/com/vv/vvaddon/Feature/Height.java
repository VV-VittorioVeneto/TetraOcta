package com.vv.vvaddon.Feature;

import com.vv.vvaddon.Init.VACoe;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class Height {

    public static float dis(Vec3 A, Vec3 B){
        return (float)A.y - (float)B.y;
    }

    public static float execute(Player player, Entity entity){
        float bonus = 0.0F;
        float dis = dis(player.getPosition(1),entity.getPosition(1));
        dis = dis>5 ? 5 : dis;
        dis = dis<-5 ? -5 : dis;
        bonus = 1 + dis/10.0F;
        bonus *= VACoe.height_coe;
        return bonus;
    }
}
