package com.vv.octa.damage;

import com.vv.octa.init.OctaDamageTypesRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class LaserDamageSource extends DamageSource {

    protected LaserDamageSource(@NotNull Entity Entity, @Nullable Vec3 damageSourcePosition) {
        super(DamageSources.getHolderFromResource(Entity, OctaDamageTypesRegistry.LASER_BEAM), damageSourcePosition);
    }

    @Override
    public @NotNull Component getLocalizedDeathMessage(@NotNull LivingEntity pLivingEntity) {
        String s = "death.attack.artillery_laser";
        Component component = Objects.requireNonNull(this.getEntity()).getDisplayName();
        return Component.translatable(s, pLivingEntity.getDisplayName(), component);
    }

    public static LaserDamageSource source(@NotNull Entity Entity) {
        return new LaserDamageSource(Entity, null);
    }

    public DamageSource get() {
        return this;
    }

}
