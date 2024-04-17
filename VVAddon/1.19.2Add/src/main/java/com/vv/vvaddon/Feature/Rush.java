package com.vv.vvaddon.Feature;

import static com.vv.vvaddon.Init.VVAddonSoundRegistry.KATANA_RUSH;

import com.vv.vvaddon.Init.VACoe;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("null")
public class Rush {

    private static class Trail{
        private Vec3 trail_begin = new Vec3(0 ,-1000, 0);
        private Vec3 trail_fold1 = new Vec3(0 ,-1000, 0);
        private Vec3 trail_fold2 = new Vec3(0 ,-1000, 0);
        private Vec3 trail_fold3 = new Vec3(0 ,-1000, 0);
        private Vec3 trail_end = new Vec3(0 ,-1000, 0);
        private int end_fold = -1;
    }

    final private static double rush_dis = 10;
    
    public static void addParticle(LevelAccessor world, ParticleOptions particle, Vec3 pos, Vec3 vel) {
        world.addParticle(particle, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
    }

    public static void executeb(Player player, ItemStack itemstack, LevelAccessor WorldIn) {
        Vec3 begin = player.getPosition(1.0F).add(0.0D, 0.25D, 0.0D);
        Vec3 direction = player.getViewVector(1.0F);
        Trail hitPos = getHitPos(player, begin, direction);
        player.getCooldowns().addCooldown(itemstack.getItem(), 40);
        if(!WorldIn.isClientSide()){
            damageEntities(player, hitPos);
        }else{
            spawnTrailParticles(hitPos, WorldIn, player.getLevel());
        }
        player.level.playSound(player, player, KATANA_RUSH.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        player.teleportTo(hitPos.trail_end.x, hitPos.trail_end.y, hitPos.trail_end.z);
    }

    private static void spawnTrailParticles(Trail trail, LevelAccessor WorldIn, Level level) {
        float r_dis = 0.0F;
        switch(trail.end_fold){
            case 3: r_dis = spawnLineParticles(trail.trail_fold3, trail.trail_fold2, WorldIn, r_dis, level);
            case 2: r_dis = spawnLineParticles(trail.trail_fold2, trail.trail_fold1, WorldIn, r_dis, level);
            case 1: r_dis = spawnLineParticles(trail.trail_fold1, trail.trail_begin, WorldIn, r_dis, level);
        }
        addParticle(WorldIn, ParticleTypes.END_ROD, trail.trail_fold1, new Vec3(0, -1, 0));
    }
    
    private static float spawnLineParticles(Vec3 begin, Vec3 end, LevelAccessor WorldIn, float r_dis, Level level) {
        float dis = outh_dis(end, begin);
        int particle_num = Mth.floor(r_dis + dis);
        for(int i = 0 ; i < particle_num ; i ++){
            addParticle(WorldIn, ParticleTypes.END_ROD, begin.add(0,1,0).add(getRandomVec3(level , 0.2F)).add(end.subtract(begin).scale((1.0F - r_dis + (float)i) / dis)), new Vec3(0, -0.1, 0));
        }
        return r_dis + dis - (float)particle_num;
    }

    private static Vec3 getRandomVec3(Level level, float scale){
        return new Vec3(level.random.nextFloat() , level.random.nextFloat() , level.random.nextFloat()).scale(scale);
    }

    private static float dis_to_trail_3 (Vec3 point, Trail trail){
        float dis = 100.0F;
        switch(trail.end_fold){
            case 3:dis = dis_to_trail_1(point, trail.trail_fold2, trail.trail_fold3, dis);
            case 2:dis = dis_to_trail_1(point, trail.trail_fold1, trail.trail_fold2, dis);
            case 1:dis = dis_to_trail_1(point, trail.trail_begin, trail.trail_fold1, dis);
        }
        return dis;
    }

    private static float dis_to_trail_1 (Vec3 pt, Vec3 begin, Vec3 end, float p_dis){
        double dx = begin.x - end.x;
	    double dy = begin.y - end.y;
        double dz = begin.z - end.z;
	    double u = (pt.x - begin.x)*(begin.x - end.x) + (pt.y - begin.y)*(begin.y - end.y) + (pt.z - begin.z)*(begin.z - end.z);
	    u = u/((dx*dx)+(dy*dy)+(dz*dz));
        double xt = begin.x + u*dx;
        double yt = begin.y + u*dy;
        double zt = begin.z + u*dz;

        double max_x, min_x;
        Vec3 minpoint = new Vec3(xt, yt, zt);
        max_x = (begin.x > end.x)? begin.x : end.x;
        min_x = (begin.x > end.x)? end.x : begin.x;
        if(xt > max_x) minpoint = (begin.x > end.x)? begin : end;
        if(xt < min_x) minpoint = (begin.x < end.x)? begin : end;
        float dis = outh_dis(minpoint, pt);
        if(p_dis < dis) dis = p_dis;
        return dis;
    }
    
    public static float dis_to_trail_1 (Vec3 pt, Vec3 begin, Vec3 end){
        return dis_to_trail_1 (pt, begin, end, 100.0F);
    }

    public static float outh_dis(Vec3 A, Vec3 B){
        return Mth.sqrt((float) (Mth.square(A.x - B.x) + Mth.square(A.y - B.y) + Mth.square(A.z - B.z)));
    }

    private static void damageEntities(Player player, Trail ptrail){
        var targetingCondition = TargetingConditions.forCombat().ignoreLineOfSight().selector(e -> {
            return (true);
        });
        player.level.getNearbyEntities(LivingEntity.class, targetingCondition, player, player.getBoundingBox().inflate(rush_dis)).forEach(entitynear ->{
            if(dis_to_trail_3(entitynear.getPosition(1.0F), ptrail) <= entitynear.getBbWidth() + 0.6){
                entitynear.hurt(DamageSource.playerAttack(player) ,(float)VACoe.rush_damage);
            }
        });
    }

    private static Trail getHitPos(Player player, Vec3 begin, Vec3 direction) {
        Trail ptrail = new Trail();
        float vec3_r;
        ptrail.trail_begin = begin;
        ptrail.end_fold = 1;
        
        ptrail.trail_fold1 = player.level.clip(new ClipContext(begin, begin.add(direction.scale(10)),
        ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getLocation();
        if(outh_dis(ptrail.trail_fold1, begin.add(direction.scale(10))) < 0.001){
            ptrail.trail_end = ptrail.trail_fold1;
        }else{
            ptrail.end_fold++;
            vec3_r = outh_dis(begin.add(direction.scale(10)), ptrail.trail_fold1);
            if(Mth.abs((float)ptrail.trail_fold1.x) % 1 > 0.9999 || Mth.abs((float)ptrail.trail_fold1.x) % 1 < 0.0001)direction = direction.multiply(-0.01,1,1);
            if(Mth.abs((float)ptrail.trail_fold1.y) % 1 > 0.9999 || Mth.abs((float)ptrail.trail_fold1.y) % 1 < 0.0001)direction = direction.multiply(1,-0.01,1);
            if(Mth.abs((float)ptrail.trail_fold1.z) % 1 > 0.9999 || Mth.abs((float)ptrail.trail_fold1.z) % 1 < 0.0001)direction = direction.multiply(1,1,-0.01);
            ptrail.trail_fold2 = player.level.clip(new ClipContext(ptrail.trail_fold1, ptrail.trail_fold1.add(direction.scale(vec3_r)),
            ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getLocation();
            if(outh_dis(ptrail.trail_fold2, ptrail.trail_fold1.add(direction.scale(vec3_r))) < 0.001){
                ptrail.trail_end = ptrail.trail_fold2;
            }else{
                ptrail.end_fold++;
                vec3_r = outh_dis(ptrail.trail_fold1.add(direction.scale(vec3_r)), ptrail.trail_fold2);
                if(Mth.abs((float)ptrail.trail_fold2.x) % 1 > 0.9999 || Mth.abs((float)ptrail.trail_fold2.x) % 1 < 0.0001)direction = direction.multiply(-0.01,1,1);
                if(Mth.abs((float)ptrail.trail_fold2.y) % 1 > 0.9999 || Mth.abs((float)ptrail.trail_fold2.y) % 1 < 0.0001)direction = direction.multiply(1,-0.01,1);
                if(Mth.abs((float)ptrail.trail_fold2.z) % 1 > 0.9999 || Mth.abs((float)ptrail.trail_fold2.z) % 1 < 0.0001)direction = direction.multiply(1,1,-0.01);
                ptrail.trail_fold3 = player.level.clip(new ClipContext(ptrail.trail_fold2, ptrail.trail_fold2.add(direction.scale(vec3_r)),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getLocation();
                ptrail.trail_end = ptrail.trail_fold3;
            }
        }
        return ptrail;
    }
}
