package com.vv.vvaddon.TestE;

import com.vv.vvaddon.Entity.PhantomArrow;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("null")
public class Phantom {
    private static AbstractArrow createArrow(Player player, ItemStack bowStack, ItemStack ammoIn) {
        ItemStack ammo = ammoIn.isEmpty() ? player.getProjectile(bowStack) : ammoIn;
        ArrowItem arrowitem = (ArrowItem)(ammo.getItem() instanceof ArrowItem ? ammo.getItem() : Items.ARROW);
        AbstractArrow abstractArrow =  arrowitem.createArrow(player.getLevel(), ammo, player);
        return abstractArrow;
    }

    private static Vec3 BP_to_Vec3(BlockPos bp){
        return new Vec3(bp.getX() , bp.getY() , bp.getZ());
    } 

    private static BlockPos Vec3_to_BP(Vec3 vec){
        return new BlockPos(vec);
    } 

    public static void execute(ItemStack itemStack, Player player, Entity entity, int type) {
        Level level = player.getLevel();
        ItemStack ammoStack = new ItemStack(Items.ARROW);
        AbstractArrow abstractArrow = createArrow(player, itemStack, ammoStack);
        if(abstractArrow != null){
            BlockPos mutableSkyPos = Vec3_to_BP(entity.getEyePosition());
            int maxFallHeight = 15;
            int k = 0;
            while(mutableSkyPos.getY() < level.getMaxBuildHeight() && level.isEmptyBlock(mutableSkyPos) && k < maxFallHeight){
                mutableSkyPos = mutableSkyPos.above();
                k++;
            }
            if(k<4) return;
            int maxArrows = 8;
            for(int j = 0; j < maxArrows; j++){
                PhantomArrow phantomArrowEntity = new PhantomArrow(level, player);
                if(type == 0)phantomArrowEntity.setPhantomArrowTick(0);
                if(type == 1)phantomArrowEntity.setPhantomArrowTick(j);
                if(type == 2)phantomArrowEntity.setPhantomArrowTick(j * 2);
                phantomArrowEntity.setPhantomArrowImmu(true);
                phantomArrowEntity.setPhantomArrowTrigger(false);
                abstractArrow = phantomArrowEntity;
                Vec3 vec3 = BP_to_Vec3(mutableSkyPos).add(level.random.nextFloat() * 16 - 8, level.random.nextFloat() * 4 - 2, level.random.nextFloat() * 16 - 8);
                int clearTries = 0;
                while (clearTries < 6 && !level.isEmptyBlock(Vec3_to_BP(vec3)) && level.getFluidState(Vec3_to_BP(vec3)).isEmpty()){
                    clearTries++;
                    vec3 = BP_to_Vec3(mutableSkyPos).add(level.random.nextFloat() * 16 - 8, level.random.nextFloat() * 4 - 2, level.random.nextFloat() * 16 - 8);
                }
                if(!level.isEmptyBlock(Vec3_to_BP(vec3)) && level.getFluidState(Vec3_to_BP(vec3)).isEmpty()){
                    continue;
                }
                abstractArrow.setPos(vec3);
                Vec3 vec31 = entity.getEyePosition().subtract(vec3);
                float randomness = 5F + level.random.nextFloat() * 10F;
                if(level.random.nextFloat() < 0.5F){
                    randomness = level.random.nextFloat();
                }
                abstractArrow.shoot(vec31.x, vec31.y, vec31.z, 0.01F,  randomness);
                abstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                level.addFreshEntity(abstractArrow);
            }
        }
    }
    
    public static void execute_effect(ItemStack itemStack, Player player, Entity entity) {
        Level level = player.getLevel();
        ItemStack ammoStack = new ItemStack(Items.ARROW);
        AbstractArrow abstractArrow = createArrow(player, itemStack, ammoStack);
        if(abstractArrow != null){
            BlockPos mutableSkyPos = Vec3_to_BP(entity.getEyePosition());
            int maxFallHeight = 4, k = 0;
            while(mutableSkyPos.getY() < level.getMaxBuildHeight() && level.isEmptyBlock(mutableSkyPos) && k < maxFallHeight){
                mutableSkyPos = mutableSkyPos.above();
                k++;
            }
            int maxArrows = 1;
            for(int j = 0; j < maxArrows; j++){
                PhantomArrow phantomArrowEntity = new PhantomArrow(level , player);
                phantomArrowEntity.setPhantomArrowTick(20);
                phantomArrowEntity.setPhantomArrowImmu(true);
                phantomArrowEntity.setPhantomArrowTrigger(false);
                abstractArrow = phantomArrowEntity;
                Vec3 vec3 = BP_to_Vec3(mutableSkyPos).add(level.random.nextFloat() * 8 - 4, level.random.nextFloat() * 4 - 2, level.random.nextFloat() * 8 - 4);
                int clearTries = 0;
                while (clearTries < 10 && !level.isEmptyBlock(Vec3_to_BP(vec3)) && level.getFluidState(Vec3_to_BP(vec3)).isEmpty()){
                    clearTries++;
                    vec3 = BP_to_Vec3(mutableSkyPos).add(level.random.nextFloat() * 8 - 4, level.random.nextFloat() * 4 - 2, level.random.nextFloat() * 8 - 4);
                }
                if(!level.isEmptyBlock(Vec3_to_BP(vec3)) && level.getFluidState(Vec3_to_BP(vec3)).isEmpty()){
                    continue;
                }
                abstractArrow.setPos(vec3);
                Vec3 vec31 = entity.getEyePosition().subtract(vec3);
                abstractArrow.shoot(vec31.x, vec31.y, vec31.z, 0.015F,  0.0F);
                abstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                level.addFreshEntity(abstractArrow);
            }
        }
    }
}
