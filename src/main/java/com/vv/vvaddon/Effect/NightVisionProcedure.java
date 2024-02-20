package com.vv.vvaddon.Effect;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class NightVisionProcedure {
    public NightVisionProcedure() {
    }
 
    @SuppressWarnings("null")
    public static void execute(Entity entity) {
       if (entity != null) {
          if (entity instanceof LivingEntity) {
             LivingEntity _entity = (LivingEntity)entity;
             _entity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 400, 0));
          }
 
       }
    }
 }