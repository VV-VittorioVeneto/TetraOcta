package com.vv.vvaddon.Init;

import com.vv.vvaddon.MainVVAddon;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class VVAddonCreativeTabRegistry {
    public static final String VVADDON_TAB_STRING = "creativetab.vvaddon_tab";
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MainVVAddon.MODID);

    public static final RegistryObject<CreativeModeTab> VVADDON_TAB = TABS.register("vvaddon_tab",
            ()-> CreativeModeTab.builder().icon(()->new ItemStack(VVAddonItemRegistry.REFINE_IA.get()))
                    .title(Component.translatable(VVADDON_TAB_STRING))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(VVAddonItemRegistry.REFINE_IA.get());
                        pOutput.accept(VVAddonItemRegistry.REFINE_IIA.get());
                        pOutput.accept(VVAddonItemRegistry.REFINE_IIB.get());
                        pOutput.accept(VVAddonItemRegistry.REFINE_IB.get());
                        pOutput.accept(VVAddonItemRegistry.MINE_INGOT.get());
                    })
                    .build());
}