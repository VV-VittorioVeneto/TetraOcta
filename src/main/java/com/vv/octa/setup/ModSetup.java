package com.vv.octa.setup;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModSetup {
    public static void setup() {
        IEventBus bus = MinecraftForge.EVENT_BUS;
//        bus.addGenericListener(Entity.class, MagicEvents::onAttachCapabilitiesPlayer);
//        bus.addListener(MagicEvents::onRegisterCapabilities);
//        bus.addListener(MagicEvents::onWorldTick);
    }

    public static void init(FMLCommonSetupEvent event) {
        Messages.register();
    }
}
