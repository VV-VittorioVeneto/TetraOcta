package com.vv.octa.util;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ClipContext;
import net.minecraftforge.entity.PartEntity;
import net.minecraft.world.phys.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class OctaUtils {
    public static float inverse(float x, float lim, float decay){
        return lim * (x / (x + decay));
    }
    
    public static float inverse(float x, float lim){
        return inverse(x, lim, 6.0F);
    }

    public static float rangeLimit(float min, float max, float input){
        input = Math.min(input, max);
        input = Math.max(input, min);
        return input;
    }

    public static double getRandomScaled(double scale) {
        return (2.0D * Math.random() - 1.0D) * scale;
    }

    public static float disToTrail(Vec3 pt, Vec3 begin, Vec3 end){
        double dx = begin.x - end.x;
	    double dy = begin.y - end.y;
        double dz = begin.z - end.z;
        var u = (pt.x - begin.x)*(begin.x - end.x) + (pt.y - begin.y)*(begin.y - end.y) + (pt.z - begin.z)*(begin.z - end.z);
	    u = u/((dx*dx)+(dy*dy)+(dz*dz));
        double xt = begin.x + u*dx;
        double yt = begin.y + u*dy;
        double zt = begin.z + u*dz;

        double max_x, min_x;
        Vec3 minPoint = new Vec3(xt, yt, zt);
        max_x = Math.max(begin.x, end.x);
        min_x = Math.min(begin.x, end.x);
        if(xt > max_x) minPoint = (begin.x > end.x)? begin : end;
        if(xt < min_x) minPoint = (begin.x < end.x)? begin : end;
        return outhDis(minPoint, pt);
    }

    public static float disToRay(Vec3 pt, Vec3 begin, Vec3 ray){
        final Vec3 end = begin.add(ray);
        final double dx = begin.x - end.x;
	    final double dy = begin.y - end.y;
        final double dz = begin.z - end.z;
	    double u = (pt.x - begin.x)*(begin.x - end.x) + (pt.y - begin.y)*(begin.y - end.y) + (pt.z - begin.z)*(begin.z - end.z);
	    u = u/((dx*dx)+(dy*dy)+(dz*dz));
        final double xt = begin.x + u*dx;
        final double yt = begin.y + u*dy;
        final double zt = begin.z + u*dz;
        final Vec3 minPoint = new Vec3(xt, yt, zt);
        return outhDis(minPoint, pt);
    }

    public static float outhDis(Vec3 A, Vec3 B){
        return Mth.sqrt((float) (Mth.square(A.x - B.x) + Mth.square(A.y - B.y) + Mth.square(A.z - B.z)));
    }

    public static HitResult raycastForEntity(Level level, Entity originEntity, float distance, boolean checkForBlocks) {
        Vec3 start = originEntity.getEyePosition();
        Vec3 end = originEntity.getLookAngle().normalize().scale(distance).add(start);

        return raycastForEntity(level, originEntity, start, end, checkForBlocks);
    }

    public static HitResult raycastForEntity(Level level, Entity originEntity, float distance, boolean checkForBlocks, float bbInflation) {
        Vec3 start = originEntity.getEyePosition();
        Vec3 end = originEntity.getLookAngle().normalize().scale(distance).add(start);

        return internalRaycastForEntity(level, originEntity, start, end, checkForBlocks, bbInflation, OctaUtils::canHitWithRaycast);
    }

    public static HitResult raycastForEntity(Level level, Entity originEntity, Vec3 start, Vec3 end, boolean checkForBlocks) {
        return internalRaycastForEntity(level, originEntity, start, end, checkForBlocks, 0, OctaUtils::canHitWithRaycast);
    }

    public static HitResult raycastForEntity(Level level, Entity originEntity, Vec3 start, Vec3 end, boolean checkForBlocks, float bbInflation, Predicate<? super Entity> filter) {
        return internalRaycastForEntity(level, originEntity, start, end, checkForBlocks, bbInflation, filter);
    }

    public static HitResult raycastForEntityOfClass(Level level, Entity originEntity, Vec3 start, Vec3 end, boolean checkForBlocks, Class<? extends Entity> c) {
        return internalRaycastForEntity(level, originEntity, start, end, checkForBlocks, 0, (entity) -> entity.getClass() == c);
    }

    private static boolean canHitWithRaycast(Entity entity) {
        return entity.isPickable() && entity.isAlive();
    }

    private static HitResult internalRaycastForEntity(Level level, Entity originEntity, Vec3 start, Vec3 end, boolean checkForBlocks, float bbInflation, Predicate<? super Entity> filter) {
        BlockHitResult blockHitResult = null;
        if (checkForBlocks) {
            blockHitResult = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, originEntity));
            end = blockHitResult.getLocation();
        }
        AABB range = originEntity.getBoundingBox().expandTowards(end.subtract(start));

        List<HitResult> hits = new ArrayList<>();
        List<? extends Entity> entities = level.getEntities(originEntity, range, filter);
        for (Entity target : entities) {
            HitResult hit = checkEntityIntersecting(target, start, end, bbInflation);
            if (hit.getType() != HitResult.Type.MISS)
                hits.add(hit);
        }

        if (!hits.isEmpty()) {
            hits.sort((o1, o2) -> o1.getLocation().distanceToSqr(start) < o2.getLocation().distanceToSqr(start) ? -1 : 1);
            return hits.get(0);
        } else if (checkForBlocks) {
            return blockHitResult;
        }
        return BlockHitResult.miss(end, Direction.UP, BlockPos.containing(end));
    }

    public static HitResult checkEntityIntersecting(Entity entity, Vec3 start, Vec3 end, float bbInflation) {
        Vec3 hitPos = null;
        if (entity.isMultipartEntity()) {
            for (PartEntity p : entity.getParts()) {
                Vec3 hit = null;
                if (p != null) {
                    hit = p.getBoundingBox().inflate(bbInflation).clip(start, end).orElse(null);
                }
                if (hit != null) {
                    hitPos = hit;
                    break;
                }
            }
        } else {
            hitPos = entity.getBoundingBox().inflate(bbInflation).clip(start, end).orElse(null);
        }
        if (hitPos != null)
            return new EntityHitResult(entity, hitPos);
        else
            return BlockHitResult.miss(end, Direction.UP, BlockPos.containing(end));

    }
}
