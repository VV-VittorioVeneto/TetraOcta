package com.vv.octa.init;

import com.vv.octa.Octa;
import com.vv.octa.modular.*;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OctaItemsRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Octa.MODID);

    public static final RegistryObject<Item> BOOMERANG_INSTANCE =
        ITEMS.register(ModularBoomerangItem.identifier, ModularBoomerangItem::new);
    public static final RegistryObject<Item> LONGSINGLE_INSTANCE =
        ITEMS.register(ModularLongSingleItem.identifier, ModularLongSingleItem::new);
    public static final RegistryObject<Item> LONGDOUBLE_INSTANCE =
        ITEMS.register(ModularLongDoubleItem.identifier, ModularLongDoubleItem::new);
    public static final RegistryObject<Item> WARFAN_INSTANCE =
        ITEMS.register(ModularWarFanItem.identifier, ModularWarFanItem::new);
    public static final RegistryObject<Item> ARROW_INSTANCE =
        ITEMS.register(ModularArrowItem.identifier, ModularArrowItem::new);
    public static final RegistryObject<Item> LASER_ARTILLERY =
        ITEMS.register(ModularLaserArtilleryItem.identifier, ModularLaserArtilleryItem::new);
    
    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}