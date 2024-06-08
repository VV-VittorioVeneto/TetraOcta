package com.vv.octa.damage;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.damagesource.DamageSource;

import javax.annotation.Nullable;
import java.util.HashMap;

@Mod.EventBusSubscriber
public class DamageSources {
//    public static DamageSource get(Level level, ResourceKey<DamageType> damageType) {
//        return level.damageSources().source(damageType);
//    }

    public static Holder<DamageType> getHolderFromResource(Entity entity, ResourceKey<DamageType> damageTypeResourceKey) {
        var option = entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolder(damageTypeResourceKey);
        if (option.isPresent()) {
            return option.get();
        } else {
            return entity.level().damageSources().genericKill().typeHolder();
        }
    }

    public static boolean applyDamage(Entity target, float baseAmount, DamageSource damageSource) {
        if (target instanceof LivingEntity livingTarget && damageSource instanceof LaserDamageSource laserDamageSource) {
            var e = new LaserDamageEvent(livingTarget, baseAmount, laserDamageSource);
            if (MinecraftForge.EVENT_BUS.post(e)) {
                return false;
            }
            baseAmount = e.getAmount();

            ignoreNextKnockback(livingTarget);
            if (damageSource.getEntity() instanceof LivingEntity livingAttacker) {
                if (isFriendlyFireBetween(livingAttacker, livingTarget)) {
                    return false;
                }
                livingAttacker.setLastHurtMob(target);
            }
            return livingTarget.hurt(damageSource, baseAmount);
        } else {
            return target.hurt(damageSource, baseAmount);
        }
    }

    //I can't tell if this is genius or incredibly stupid
    private static final HashMap<LivingEntity, Integer> knockbackImmune = new HashMap<>();

    public static void ignoreNextKnockback(LivingEntity livingEntity) {
        if (!livingEntity.level().isClientSide) {
            knockbackImmune.put(livingEntity, livingEntity.tickCount);
        }
    }

    @SubscribeEvent
    public static void cancelKnockback(LivingKnockBackEvent event) {
        if (knockbackImmune.containsKey(event.getEntity())) {
            var entity = event.getEntity();
            if (entity.tickCount - knockbackImmune.get(entity) <= 1) {
                event.setCanceled(true);
            }
            knockbackImmune.remove(entity);
        }
    }

    public static boolean isFriendlyFireBetween(Entity attacker, Entity target) {
        if (attacker == null || target == null)
            return false;
        if (attacker.isPassengerOfSameVehicle(target)) {
            return true;
        }
        var team = attacker.getTeam();
        if (team != null) {
            return team.isAlliedTo(target.getTeam()) && !team.isAllowFriendlyFire();
        }
        //We already manually checked for teams, so this will only return true for any overrides (such as summons)
        return attacker.isAlliedTo(target);
    }

    @Deprecated(since = "MC_1.20", forRemoval = true)
    public static DamageSource directDamageSource(DamageSource source, Entity attacker) {
        return new DamageSource(source.typeHolder(), attacker);
        //return new EntityDamageSource(source.getMsgId(), attacker);
    }

    @Deprecated(since = "MC_1.20", forRemoval = true)
    public static DamageSource indirectDamageSource(DamageSource source, Entity projectile, @Nullable Entity attacker) {
        return new DamageSource(source.typeHolder(), attacker, projectile);
    }

}
