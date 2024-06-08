package com.vv.octa.init;

import com.vv.octa.Octa;
import com.vv.octa.modular.container.ArtilleryContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class OctaContainersRegistry {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Octa.MODID);

    public static void register(IEventBus bus) {
        CONTAINERS.register(bus);
        ArtilleryContainer.type = CONTAINERS.register("modular_artillery", () -> IForgeMenuType.create((windowId, inv, data) -> ArtilleryContainer.create(windowId, inv)));
    }
}
