package com.vv.octa.client;

import com.vv.octa.Octa;
import com.vv.octa.client.gui.ArtilleryOverlay;
import com.vv.octa.client.gui.pluginConfigurationController.GuiArtilleryScreen;
import com.vv.octa.client.renderer.BoomerangEntityRenderer;
import com.vv.octa.client.renderer.OctaArrowEntityRenderer;
import com.vv.octa.effect.*;
import com.vv.octa.effect.artillery.*;
import com.vv.octa.init.OctaEntitiesRegistry;

import com.vv.octa.modular.container.ArtilleryContainer;
import com.vv.octa.network.OpenArtilleryGuiPacket;
import com.vv.octa.util.OctaKeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import se.mickelus.tetra.blocks.multischematic.MultiblockSchematicGui;
import se.mickelus.tetra.effect.gui.AbilityOverlays;
import se.mickelus.tetra.effect.howling.HowlingOverlay;
import se.mickelus.tetra.interactions.SecondaryInteractionOverlay;
import se.mickelus.tetra.items.modular.ItemColors;
import se.mickelus.tetra.items.modular.impl.BlockProgressOverlay;
import se.mickelus.tetra.items.modular.impl.bow.RangedProgressOverlay;
import se.mickelus.tetra.items.modular.impl.crossbow.CrossbowOverlay;
import se.mickelus.tetra.items.modular.impl.holo.gui.scan.ScannerOverlayGui;
import se.mickelus.tetra.items.modular.impl.toolbelt.booster.OverlayBooster;
import se.mickelus.tetra.items.modular.impl.toolbelt.gui.overlay.ToolbeltOverlay;

import static se.mickelus.tetra.TetraMod.packetHandler;

public class OctaClient{

    @Mod.EventBusSubscriber(modid = Octa.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            addItemColor();
            BoomerangSpeedAttribute.init();
            BoomerangTimeAttribute.init();
            PiercingEnchantment.init();
            WindChargeEffect.init();
            ArrowDamageAttribute.init();
            HeatingSpeedAttribute.init();
            CoolingSpeedAttribute.init();
            HeatResistanceAttribute.init();
            PumpPowerAttribute.init();
            FocusingEfficiencyAttribute.init();
            CompositeSpectralPowerAttribute.init();
            Octa.debug("Find Me 2");
            packetHandler.registerPacket(OpenArtilleryGuiPacket.class, OpenArtilleryGuiPacket::new);
            Octa.debug("Find Me 3");
            MenuScreens.register(ArtilleryContainer.type.get(), GuiArtilleryScreen::new);
        }

        @SubscribeEvent
        public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
            event.register(OctaKeyBindings.openBinding);
        }

        @SubscribeEvent
        public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(OctaEntitiesRegistry.BOOMERANG.get(), BoomerangEntityRenderer::new);
            event.registerEntityRenderer(OctaEntitiesRegistry.OCTA_ARROW.get(), OctaArrowEntityRenderer::new);
        }

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            Minecraft mc = Minecraft.getInstance();
            registerOverlay(event, "artillery", new ArtilleryOverlay(mc));
        }
    }

    @Mod.EventBusSubscriber(modid = Octa.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
    }

    private static void addItemColor(){
        ItemColors.define(Integer.parseInt("c6c6c6", 16),"window");
        ItemColors.define(Integer.parseInt("c6c6c6", 16),"window_glyph");
    }

    private static void registerOverlay(RegisterGuiOverlaysEvent event, String id, IGuiOverlay overlay) {
        event.registerBelowAll(id, overlay);
        MinecraftForge.EVENT_BUS.register(overlay);
    }
}
