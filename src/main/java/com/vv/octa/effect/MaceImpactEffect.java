package com.vv.octa.effect;

import com.vv.octa.Octa;
import com.vv.octa.entity.BoomerangEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import se.mickelus.tetra.items.modular.ModularItem;

public class MaceImpactEffect {
    public static void hitEntity(LivingEntity source) {
        if (source.getDeltaMovement().y < -0.2 && source.getMainHandItem().getItem() instanceof ModularItem item) {
            final Level worldIn = source.level();
            if (worldIn instanceof ServerLevel serverLevel) {
                EntityHitResult result = rayCastEntity(source);
                if(result != null) {
                    final Entity target = result.getEntity();
                    final Vec3 motion = source.getDeltaMovement();
                    final double impactSpeed = motion.length();
                    final double attackDamage = item.getAttributeValue(source.getMainHandItem(), Attributes.ATTACK_DAMAGE);
                    target.hurt(source.damageSources().anvil((Entity) source), (float) (impactSpeed * attackDamage));
                    source.setDeltaMovement(motion.x, -1.5 * motion.y ,motion.z);
                    source.resetFallDistance();
                    serverLevel.playSeededSound(null, source.position().x, source.position().y, source.position().z, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.4F, 0.8F + worldIn.random.nextFloat() * 0.3F, 42L);
                    Octa.debug("t" + (impactSpeed * attackDamage));
                }
            }
        }
    }

    @Nullable
    protected static EntityHitResult rayCastEntity(LivingEntity source) {
        Vec3 motion = source.getDeltaMovement();
        Vec3 from = source.position();
        Vec3 to = from.add(motion);

        return ProjectileUtil.getEntityHitResult(source.level(), source, from, to, source.getBoundingBox().expandTowards(motion).inflate(1.0D), (entity) -> !entity.isSpectator()
                && entity.isAlive());
    }
}
