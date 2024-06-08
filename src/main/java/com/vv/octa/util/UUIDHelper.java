package com.vv.octa.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class UUIDHelper {

    public static LivingEntity getAndCacheLivingEntity(Level level, LivingEntity cachedLivingEntity, UUID livingEntityUUID) {
        if (cachedLivingEntity != null && cachedLivingEntity.isAlive()) {
            return cachedLivingEntity;
        } else if (livingEntityUUID != null && level instanceof ServerLevel serverLevel) {
            if (serverLevel.getEntity(livingEntityUUID) instanceof Player livingEntity)
                cachedLivingEntity = livingEntity;
            return cachedLivingEntity;
        } else {
            return null;
        }
    }

    public static void serializeLivingEntity(CompoundTag compoundTag, UUID livingEntityUUID) {
        if (livingEntityUUID != null) {
            compoundTag.putUUID("livingEntityUUID", livingEntityUUID);
        }
    }

    public static UUID deserializeLivingEntity(CompoundTag compoundTag) {
        if (compoundTag.hasUUID("livingEntityUUID")) {
            return compoundTag.getUUID("livingEntityUUID");
        }
        return null;
    }

}