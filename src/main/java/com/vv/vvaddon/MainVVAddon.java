package com.vv.vvaddon;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vv.vvaddon.Config.VVAddonConfig;
import com.vv.vvaddon.Handler.VVAddonEventHandler;
import com.vv.vvaddon.Renderer.PhantomArrowRenderer;
import com.vv.vvaddon.Init.VACoe;
import com.vv.vvaddon.Init.VVAddonItemRegistry;

import static com.vv.vvaddon.Init.VVAddonEntityRegistry.ENTITIES;
import static com.vv.vvaddon.Init.VVAddonItemRegistry.ITEMS;
import static com.vv.vvaddon.Init.VVAddonEntityRegistry.PHANTOM_ARROW;
import static com.vv.vvaddon.Init.VVAddonSoundRegistry.SOUNDS;

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
            return new ItemStack(VVAddonItemRegistry.REFINE_IIB.get());
        }
    };

    
    public MainVVAddon()
    {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new VVAddonEventHandler());
        modEventBus.addListener(this::entityRendererSetup);
        ITEMS.register(modEventBus);
        ENTITIES.register(modEventBus);
        SOUNDS.register(modEventBus);
        VVAddonConfig.setup();
        new VACoe(); 
    }
    
    @SuppressWarnings("null")
    private void entityRendererSetup(final EntityRenderersEvent.RegisterRenderers event) {

        event.registerEntityRenderer(PHANTOM_ARROW.get(), PhantomArrowRenderer::new);
    }

}