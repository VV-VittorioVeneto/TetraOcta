package com.vv.vvaddon.Init;

import com.vv.vvaddon.MainVVAddon;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class VVAddonSoundRegistry {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MainVVAddon.MODID);

    public static final RegistryObject<SoundEvent> KATANA_RUSH = registerSoundEvent("katana_rush");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MainVVAddon.MODID, name)));
    }
}
