package com.vv.vvaddon.Feature;

import com.vv.vvaddon.VVAddonConfig;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class Sniper {

    public static double dis(Vec3 A, Vec3 B){
        return Math.sqrt(Math.pow(A.x-B.x,2)+Math.pow(A.y-B.y,2)+Math.pow(A.z-B.z,2));
    }

    public static float execute(Player player , float damage , Entity entity){
        double dis = dis(player.getPosition(1),entity.getPosition(1));
        dis = dis>50?50:dis;
        if(dis >= 30){
            damage *= dis/25;
        }else if(dis >= 10){
            damage *= 0.9 + dis/100;
        }else{
            damage *= 0.5 + dis/20;
        }
        damage *= VVAddonConfig.Sniper_coefficient.get();
        return damage;
    }
}
