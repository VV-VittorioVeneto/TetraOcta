package com.vv.octa.effect;

import static com.vv.octa.effect.gui.EffectGuiStats.*;
import static se.mickelus.tetra.gui.stats.StatsHelper.barLength;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.TargetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterInteger;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

public class WindChargeEffect {
    @OnlyIn(Dist.CLIENT)
    public static void init() {
        final IStatGetter effectStatGetter = new StatGetterEffectLevel(windCharge,1);
        final GuiStatBar effectBar = new GuiStatBar
                (0, 0, barLength, windChargeName, 0, 2, false, effectStatGetter,
                        LabelGetterBasic.decimalLabel, new TooltipGetterInteger
                        (windChargeTooltip, effectStatGetter));
        WorkbenchStatsGui.addBar(effectBar);
        HoloStatsGui.addBar(effectBar);
    }
    
    public static void windExplode(LivingEntity livingentity, Entity entity, int level){
        if(level <= 0)return;
        var targetingCondition = TargetingConditions.forCombat().ignoreLineOfSight().selector(e -> (true));
        entity.level().getNearbyEntities(LivingEntity.class, targetingCondition, livingentity, entity.getBoundingBox().inflate(3 + entity.distanceTo(livingentity))).forEach(entitynear ->{
            final double dis = entity.distanceTo(entitynear);
            if(dis > 3)return;
            final double delta_x = entity.getPosition(1).x - entitynear.getPosition(1).x;
            final double delta_y = entity.getPosition(1).y - entitynear.getPosition(1).y;
            final double delta_z = entity.getPosition(1).z - entitynear.getPosition(1).z;
            double strength = 2 / (dis < 1 ? 1 : dis) * (0.7 + 0.3 * level);
            double x = (-strength * delta_x / dis);
            double y = (-strength * delta_y / dis);
            double z = (-strength * delta_z / dis);
            entitynear.hurt(livingentity.damageSources().explosion(entity, livingentity),1);
            entitynear.setDeltaMovement(x, y, z);
        });
    }

    public static boolean hitInteractableBlock(int level, Level worldIn, BlockHitResult blockHitResult, Projectile projectile, LivingEntity entity){
        if (level <= 0) return false;
        BlockPos blockPos = blockHitResult.getBlockPos();
        BlockState blockState = worldIn.getBlockState(blockPos);
        Block block = blockState.getBlock();
        Direction direction = blockHitResult.getDirection();
        if(direction.equals(Direction.UP) || direction.equals(Direction.DOWN)){
            extinguish(worldIn, blockPos.relative(direction).north());
            extinguish(worldIn, blockPos.relative(direction).south());
            extinguish(worldIn, blockPos.relative(direction).east());
            extinguish(worldIn, blockPos.relative(direction).west());
        }else if(direction.equals(Direction.SOUTH) || direction.equals(Direction.NORTH)){
            extinguish(worldIn, blockPos.relative(direction).below());
            extinguish(worldIn, blockPos.relative(direction).east());
            extinguish(worldIn, blockPos.relative(direction).west());
        }else{
            extinguish(worldIn, blockPos.relative(direction).below());
            extinguish(worldIn, blockPos.relative(direction).north());
            extinguish(worldIn, blockPos.relative(direction).south());
        }
        if(block instanceof TargetBlock target){
            projectile.setOwner(entity);
            target.onProjectileHit(worldIn, blockState, blockHitResult, projectile);
        }else if(block instanceof ButtonBlock buttonBlock){
            buttonBlock.press(blockState, worldIn, blockPos);
        }else if(block instanceof LeverBlock leverBlock){
            leverBlock.pull(blockState, worldIn, blockPos);
        }else if(block instanceof DoorBlock doorBlock){
            doorBlock.setOpen(projectile, worldIn, blockState, blockPos, true);
        }else if(block instanceof BellBlock bellBlock){
            bellBlock.onProjectileHit(worldIn, blockState, blockHitResult, projectile);
        }else if(block instanceof CandleBlock){
            extinguish(worldIn, blockPos);
        }else{
            return (testForBlock(worldIn, blockPos.relative(direction)));
        }
        return true;
    }

    private static boolean testForBlock(Level worldIn, BlockPos blockPos){
        BlockState blockState = worldIn.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if(block instanceof ButtonBlock buttonBlock){
            buttonBlock.press(blockState, worldIn, blockPos);
        }else if(block instanceof LeverBlock leverBlock){
            leverBlock.pull(blockState, worldIn, blockPos);
        }else if(block instanceof CandleBlock){
            extinguish(worldIn, blockPos);
        }else{
            return false;
        }
        return true;
    }

    private static void extinguish(Level worldIn, BlockPos blockPos){
        if(worldIn.getBlockState(blockPos).getBlock() instanceof CandleBlock){
            worldIn.setBlock(blockPos, worldIn.getBlockState(blockPos).setValue(CandleBlock.LIT, Boolean.FALSE), 11);
        }
    }
}
