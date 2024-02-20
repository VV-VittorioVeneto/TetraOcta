package com.vv.vvaddon.Feature;

import com.vv.vvaddon.VVAddonConfig;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class Height {

    public static double dis(Vec3 A, Vec3 B){
        return A.y-B.y;
    }

    public static float execute( Player player ,float damage , Entity entity){
        double dis = dis(player.getPosition(1),entity.getPosition(1));
        dis = dis>5?5:dis;
        dis = dis<-5?-5:dis;
        damage *= 1 + dis/10;
        damage *= VVAddonConfig.Height_coefficient.get();
        return damage;
    }
}
