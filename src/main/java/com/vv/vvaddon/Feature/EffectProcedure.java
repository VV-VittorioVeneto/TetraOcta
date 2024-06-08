package com.vv.vvaddon.Feature;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class EffectProcedure {
   public static void execute(Entity entity, MobEffect effect, int time, int level) {
      if (entity != null && entity instanceof LivingEntity livingentity) {
         livingentity.addEffect(new MobEffectInstance(effect, time, level));
      }
   }

   public static void execute(Entity entity, MobEffect effect, int time) {
      if (entity != null && entity instanceof LivingEntity livingentity) {
         livingentity.addEffect(new MobEffectInstance(effect, time, 0));
      }
   }

   public static void execute(Entity entity, MobEffect effect) {
      if (entity != null && entity instanceof LivingEntity livingentity) {
         livingentity.addEffect(new MobEffectInstance(effect, 400, 0));
      }
   }

   private static MobEffect str_to_eff(String str){
      switch(str){
         case "ABSORPTION" : return MobEffects.ABSORPTION;
         case "BAD_OMEN" : return MobEffects.BAD_OMEN;
         case "BLINDNESS" : return MobEffects.BLINDNESS;
         case "CONDUIT_POWER" : return MobEffects.CONDUIT_POWER;
         case "CONFUSION" : return MobEffects.CONFUSION;
         case "DAMAGE_BOOST" : return MobEffects.DAMAGE_BOOST;
         case "DAMAGE_RESISTANCE" : return MobEffects.DAMAGE_RESISTANCE;
         default : return MobEffects.GLOWING;
      }
   }
   
   public static void execute(Entity entity, String str, int time, int level) {
      if (entity != null && entity instanceof LivingEntity livingentity) {
         livingentity.addEffect(new MobEffectInstance(str_to_eff(str), time, level));
      }
   }

   public static void execute(Entity entity, String str, int time) {
      if (entity != null && entity instanceof LivingEntity livingentity) {
         livingentity.addEffect(new MobEffectInstance(str_to_eff(str), time, 0));
      }
   }
   
   public static void execute(Entity entity, String str) {
      if (entity != null && entity instanceof LivingEntity livingentity) {
         livingentity.addEffect(new MobEffectInstance(str_to_eff(str), 400, 0));
      }
   }
}