package com.vv.octa.init;

import com.vv.octa.Octa;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OctaParticlesRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Octa.MODID);

    public static void register(IEventBus eventBus) {
        PARTICLES.register(eventBus);
    }
    public static final RegistryObject<SimpleParticleType> LASER_PARTICLE = PARTICLES.register("blood", () -> new SimpleParticleType(false));
}
