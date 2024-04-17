package com.vv.vvaddon.TestE;

import java.util.Vector;

import com.vv.vvaddon.Entity.PhantomArrow;
import com.vv.vvaddon.Feature.Rush;

import java.util.Iterator;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import se.mickelus.tetra.items.modular.impl.bow.ModularBowItem;

@SuppressWarnings("null")
public class Cheater {
    private static Vector<Entity> entity_n = new Vector<>();

    private static final float cheat_dis = 50.0F;

    private static AbstractArrow createArrow(Player player, ItemStack bowStack, ItemStack ammoIn) {
        ItemStack ammo = ammoIn.isEmpty() ? player.getProjectile(bowStack) : ammoIn;
        ArrowItem arrowitem = (ArrowItem)(ammo.getItem() instanceof ArrowItem ? ammo.getItem() : Items.ARROW);
        AbstractArrow abstractArrow =  arrowitem.createArrow(player.getLevel(), ammo, player);
        return abstractArrow;
    }

    public static void scanner(ItemStack itemStack, Level level, LivingEntity livingEntity){
        if (livingEntity instanceof Player player && itemStack.getItem() instanceof ModularBowItem) {
            entity_n.clear();
            var targetingCondition = TargetingConditions.forCombat().ignoreLineOfSight().selector(e -> {
                return (true);
            });
            player.level.getNearbyEntities(LivingEntity.class, targetingCondition, player, player.getBoundingBox().inflate(cheat_dis)).forEach(entitynear ->{
                entity_n.add(entitynear);
            });
        }
    } 

    public static void remove(Entity entity){
        Iterator<Entity> iterator = entity_n.iterator();
        while(iterator.hasNext()){
            if(iterator.next() == entity)iterator.remove();
        }
    }

    public static void lock(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof Player player && itemStack.getItem() instanceof ModularBowItem && !entity_n.isEmpty()) {
            int count = 0;
            for(Entity entitynear : entity_n){
                if(count == 1)return;
                if(Rush.dis_to_trail_1(entitynear.getPosition(1.0F), player.getPosition(1.0F), player.getPosition(1.0F).add(player.getViewVector(1.0F).scale(cheat_dis))) 
                <= entitynear.getBbWidth() + 1 + Rush.outh_dis(entitynear.getEyePosition(),player.getEyePosition())/5.0F){
                    count++;
                    Vec3 vec31 = entitynear.getEyePosition().subtract(player.getEyePosition()).scale(1/Rush.outh_dis(entitynear.getEyePosition(),player.getEyePosition()));
                    float x = (float)vec31.x;
                    float y = (float)vec31.y;
                    float z = (float)vec31.z;
                    float y_rot = (float)Math.atan(z/x)*180.0F/Mth.PI - 90.0F + ((x>0) ? 0 : 180);
                    float x_rot = (float)-Math.atan(y/Math.sqrt(z*z + x*x))*180.0F/Mth.PI;
                    player.setYRot(y_rot);
                    player.setXRot(x_rot);
                }
            }  
        }
    }

    public static void shot(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof Player player && itemStack.getItem() instanceof ModularBowItem && !entity_n.isEmpty()) {
            ItemStack ammoStack = player.getProjectile(itemStack);
            if(ammoStack.isEmpty()){
                ammoStack = new ItemStack(Items.ARROW);
            }
            int count = 0;
            for(Entity entitynear : entity_n){
                if(count == 1)return;
                if(Rush.dis_to_trail_1(entitynear.getPosition(1.0F), player.getPosition(1.0F), player.getPosition(1.0F).add(player.getViewVector(1.0F).scale(cheat_dis))) 
                <= entitynear.getBbWidth() + 1 + Rush.outh_dis(entitynear.getEyePosition(),player.getEyePosition())/5.0F){
                    count++;
                    Vec3 vec31 = entitynear.getEyePosition().subtract(player.getEyePosition()).scale(1/Rush.outh_dis(entitynear.getEyePosition(),player.getEyePosition()));
                    AbstractArrow abstractArrow = createArrow(player, itemStack, ammoStack);
                    if(abstractArrow != null){
                        abstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        PhantomArrow phantomArrowEntity = new PhantomArrow(level, livingEntity);
                        phantomArrowEntity.setPhantomArrowTick(0);
                        phantomArrowEntity.setPhantomArrowImmu(true);
                        phantomArrowEntity.setPhantomArrowTrigger(false);
                        abstractArrow = phantomArrowEntity;
                        abstractArrow.setPos(player.getEyePosition().add(vec31));
                        abstractArrow.shoot(vec31.x, vec31.y, vec31.z, 1.0F,  0.0F);
                        level.addFreshEntity(abstractArrow);
                    }
                    if(!player.isCreative()){
                        itemStack.hurtAndBreak(1, player, (player1) -> {
                            player1.broadcastBreakEvent(player1.getUsedItemHand());
                        });
                        if(!ammoStack.is(Items.ARROW)){
                            ammoStack.shrink(1);
                        }
                    }
                }
            }
            
        }
    }
}

    

