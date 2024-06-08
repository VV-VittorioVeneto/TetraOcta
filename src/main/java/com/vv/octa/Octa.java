package com.vv.octa;

import com.vv.octa.init.*;
import com.vv.octa.setup.Messages;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vv.octa.common.BRSounds;
import com.vv.octa.handler.OctaEventHandler;

@Mod(Octa.MODID)
public class Octa{
    public static final Boolean debug = true;
    public static final String MODID = "octa";
    public static final Logger LOGGER = LogManager.getLogger();
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    public Octa(){
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new OctaEventHandler());
        OctaItemsRegistry.register(modEventBus);
        OctaEntitiesRegistry.register(modEventBus);
        OctaAttributesRegistry.register(modEventBus);
        OctaParticlesRegistry.register(modEventBus);
        OctaContainersRegistry.register(modEventBus);
        Messages.register();
        BRSounds.SOUND_EVENTS.register(modEventBus);
    }
    
    public static void debug(String... messages){
        if(!debug)return;
        for(String message : messages){
            LOGGER.debug("\nOctaDebug: {}", message);
        }
    }
}