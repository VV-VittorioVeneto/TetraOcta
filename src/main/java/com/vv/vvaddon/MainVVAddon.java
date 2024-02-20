package com.vv.vvaddon;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MainVVAddon.MODID)
public class MainVVAddon
{
    public static final String MODID = "vvaddon";
    public static final Logger LOGGER = LogManager.getLogger();
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    public static final CreativeModeTab VVADDON_TAB = new CreativeModeTab(MODID) {
        @SuppressWarnings("null")
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.REFINE_IIB.get());
        }
    };

    
    public MainVVAddon()
    {
        ModItems.ITEMS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new VVAddonEffect());
        VVAddonConfig.setup();
    }

}