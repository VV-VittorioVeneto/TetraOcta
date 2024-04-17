package com.vv.vvaddon.Init;

import com.vv.vvaddon.MainVVAddon;
import com.vv.vvaddon.Entity.PhantomArrow;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class VVAddonEntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MainVVAddon.MODID);

    public static final RegistryObject<EntityType<PhantomArrow>> PHANTOM_ARROW = 
    ENTITIES.register("phantom_arrow", () -> EntityType.Builder.<PhantomArrow>of(PhantomArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).build("phantom_arrow"));
}
