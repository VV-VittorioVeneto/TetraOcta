package com.vv.vvaddon.Init;

import com.vv.vvaddon.MainVVAddon;
import com.vv.vvaddon.Effect.PhantomPersuitMobEffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class VVAddonEffectRegistry {
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MainVVAddon.MODID);
	public static final RegistryObject<MobEffect> PHANTOM_PERSUIT = REGISTRY.register("phantom_persuit", PhantomPersuitMobEffect::new);  
}