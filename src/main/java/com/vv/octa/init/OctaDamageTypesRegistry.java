package com.vv.octa.init;

import com.vv.octa.Octa;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class OctaDamageTypesRegistry {

    public static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Octa.MODID, name));
    }
    public static final ResourceKey<DamageType> LASER_BEAM = register("laser_beam");
}
