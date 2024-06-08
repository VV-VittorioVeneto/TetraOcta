package com.vv.octa.effect.gui;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import se.mickelus.tetra.effect.ChargedAbilityEffect;
import se.mickelus.tetra.effect.ItemEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EffectGuiStats {
    // BoomerangSpeed
    public static final String boomerangSpeedName = "octa.effect.boomerang_speed.name";
    public static final String boomerangSpeedTooltip = "octa.effect.boomerang_speed.tooltip";
    
    // BoomerangTime
    public static final String boomerangTimeName = "octa.effect.boomerang_time.name";
    public static final String boomerangTimeTooltip = "octa.effect.boomerang_time.tooltip";

    // ArrowDamage
    public static final String arrowDamageName = "octa.effect.arrow_damage.name";
    public static final String arrowDamageTooltip = "octa.effect.arrow_damage.tooltip";
    
    // Piercing
    public static final String piercingName = "octa.effect.piercing.name";
    public static final String piercingTooltip = "octa.effect.piercing.tooltip";

    public static final String artilleryFocusName = "octa.effect.artillery_focus.name";
    public static final String artilleryFocusTooltip = "octa.effect.artillery_focus.tooltip";
    public static final String artilleryTemperatureName = "octa.effect.artillery_temperature.name";
    public static final String artilleryTemperatureTooltip = "octa.effect.artillery_temperature.tooltip";
    public static final String artilleryPumpName = "octa.effect.artillery_pump.name";
    public static final String artilleryPumpTooltip = "octa.effect.artillery_pump.tooltip";
    public static final String artilleryCoolingName = "octa.effect.artillery_cooling.name";
    public static final String artilleryCoolingTooltip = "octa.effect.artillery_cooling.tooltip";
    public static final String artilleryHeatingName = "octa.effect.artillery_heating.name";
    public static final String artilleryHeatingTooltip = "octa.effect.artillery_heating.tooltip";
    public static final String artillerySpectralName = "octa.effect.artillery_spectral.name";
    public static final String artillerySpectralTooltip = "octa.effect.artillery_spectral.tooltip";
    // WindCharge
    public static final ItemEffect windCharge = ItemEffect.get("octa:wind_charge");
    public static final String windChargeName = "octa.effect.wind_charge.name";
    public static final String windChargeTooltip = "octa.effect.wind_charge.tooltip";

    //ItemEffect
    public static final ItemEffect weightless = ItemEffect.get("octa:weightless");
    public static final ItemEffect accelerator = ItemEffect.get("octa:accelerator");
    public static final ItemEffect cluster = ItemEffect.get("octa:cluster");
    public static final ItemEffect seeker = ItemEffect.get("octa:seeker");
    public static final ItemEffect RValue = ItemEffect.get("octa:R_value");
    public static final ItemEffect GValue = ItemEffect.get("octa:G_value");
    public static final ItemEffect BValue = ItemEffect.get("octa:B_value");
    public static final ItemEffect HValue = ItemEffect.get("octa:H_value");
    public static final ItemEffect SValue = ItemEffect.get("octa:S_value");
    public static final ItemEffect windowSwitch = ItemEffect.get("octa:window_switch");
    public static final ItemEffect safeMode = ItemEffect.get("octa:safe_mode");
    public static final ItemEffect slotNumber = ItemEffect.get("octa:slot_number");

    // Bosses
    // public static boolean isBossEntity(EntityType<?> entity) {
    //     return Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.tags()).getTag(BOSS_ENTITIES).contains(entity);
    // }

    // Percentage Math
    public static float getDecimalPercentage(float percentage, float base) {
        float totalPercentage = (base * (percentage / 100));

        return totalPercentage;
    }

    public static float getExactPercentage(float base, float percentage) {
        float totalPercentage = base + percentage;

        return totalPercentage;
    }


    // Charged Abilities
    public static List<ChargedAbilityEffect> setupAbilites() {
        List<ChargedAbilityEffect> list = new ArrayList<>();
        // list.addAll(Arrays.stream(ItemModularHandheldAccessor.getAbilities()).toList());

        // Add abilities here
        // list.add(SoulChargedEffect.instance);
        // list.add(SonicShockEffect.instance);
        // list.add(SubjugationEffect.instance);

        // End of abilities
        list = list.stream().filter(Objects::nonNull).toList();
        ChargedAbilityEffect[] abilityEffects = list.toArray(new ChargedAbilityEffect[list.size()]);
        for (int i = 0; i < list.size(); i++) {
            abilityEffects[i] = list.get(i);
        }
        // ItemModularHandheldAccessor.setAbilities(abilityEffects);
        return list;
    }
}