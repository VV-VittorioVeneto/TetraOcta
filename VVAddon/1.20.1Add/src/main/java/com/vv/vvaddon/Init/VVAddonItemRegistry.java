package com.vv.vvaddon.Init;

import com.vv.vvaddon.MainVVAddon;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("null")
public class VVAddonItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,MainVVAddon.MODID);

    public static final RegistryObject<Item> REFINE_IA = ITEMS.register("refine_ia",
            () -> new Item(new Item.Properties().tab(MainVVAddon.VVADDON_TAB)));

    public static final RegistryObject<Item> REFINE_IB = ITEMS.register("refine_ib",
            () -> new Item(new Item.Properties().tab(MainVVAddon.VVADDON_TAB)));
            
    public static final RegistryObject<Item> REFINE_IIA = ITEMS.register("refine_iia",
            () -> new Item(new Item.Properties().tab(MainVVAddon.VVADDON_TAB)));

    public static final RegistryObject<Item> REFINE_IIB = ITEMS.register("refine_iib",
            () -> new Item(new Item.Properties().tab(MainVVAddon.VVADDON_TAB)));

    public static final RegistryObject<Item> MINE_INGOT = ITEMS.register("mine_ingot",
            () -> new Item(new Item.Properties().tab(MainVVAddon.VVADDON_TAB)));
            
    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

}