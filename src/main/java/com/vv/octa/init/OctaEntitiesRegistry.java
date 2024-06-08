package com.vv.octa.init;

import com.vv.octa.Octa;
import com.vv.octa.entity.BoomerangEntity;
import com.vv.octa.entity.OctaArrowEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OctaEntitiesRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Octa.MODID);

    public static final RegistryObject<EntityType<BoomerangEntity>> BOOMERANG = 
            ENTITIES.register("boomerang", () -> EntityType.Builder.of(BoomerangEntity::new, MobCategory.MISC).sized(BoomerangEntity.HITBOX_SIZE, BoomerangEntity.HITBOX_SIZE)
                    .updateInterval(1)
                    .clientTrackingRange(8)
                    .setShouldReceiveVelocityUpdates(true)
                    .build(new ResourceLocation(Octa.MODID, "boomerang").toString()));

    public static final RegistryObject<EntityType<OctaArrowEntity>> OCTA_ARROW = 
            ENTITIES.register("octa_arrow", () -> EntityType.Builder.<OctaArrowEntity>of(OctaArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F)
                    .build(new ResourceLocation(Octa.MODID, "octa_arrow").toString()));

    public static void register(IEventBus bus){
        ENTITIES.register(bus);
    }
}
