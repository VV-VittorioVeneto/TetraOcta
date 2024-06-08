package com.vv.octa.damage;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * SpellDamageEvent is fired when a LivingEntity is set to be hurt by a spell, via {@link com.vv.octa.damage.DamageSources#applyDamage(Entity, float, DamageSource)}.<br>
 * This happens before Entity#hurt, meaning all other forge damage events will also fire if the damage succeeds.
 * <br>
 * <br>
 * This event is {@link Cancelable}.<br>
 * If this event is canceled, the Entity will not be hurt.<br>
 * <br>
 * This event does not have a result. {@link HasResult}<br>
 * <br>
 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.<br>
 **/

public class LaserDamageEvent extends LivingEvent {
    //TODO: pre and post?
    private final LaserDamageSource laserDamageSource;
    private final float baseAmount;
    private float amount;

    public LaserDamageEvent(LivingEntity livingEntity, float amount, LaserDamageSource laserDamageSource) {
        super(livingEntity);
        this.laserDamageSource = laserDamageSource;
        this.baseAmount = amount;
        this.amount = this.baseAmount;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    public float getOriginalAmount() {
        return this.baseAmount;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public LaserDamageSource getSpellDamageSource() {
        return this.laserDamageSource;
    }
}
