package com.vv.octa.modular;

import javax.annotation.ParametersAreNonnullByDefault;

import com.vv.octa.Octa;

import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.tetra.items.modular.impl.ModularDoubleHeadedItem;

import se.mickelus.tetra.items.modular.impl.ModularSingleHeadedItem;
import se.mickelus.tetra.items.modular.impl.ModularBladedItem;
import se.mickelus.tetra.items.modular.impl.bow.ModularBowItem;

@ParametersAreNonnullByDefault
public class ModularLongDoubleItem extends ModularDoubleHeadedItem{

    public static final String headLeftKey = "longdouble/head_left";
    public static final String headRightKey = "longdouble/head_right";
    public static final String handleKey = "longdouble/handle";
    public static final String bindingKey = "longdouble/binding";
    public static final String accessoryKey = "longdouble/accessory";
    public static final String leftSuffix = "_left";
    public static final String rightSuffix = "_right";
    public static final String identifier = "modular_longdouble";

    @ObjectHolder(registryName = "item", value = Octa.MODID + ":" + identifier)
    public static ModularLongDoubleItem instance;

    public ModularLongDoubleItem() {
        
        super();

        this.majorModuleKeys = new String[]{"longdouble/head_left", "longdouble/head_right", "longdouble/handle"};
        this.minorModuleKeys = new String[]{"longdouble/binding"};
        this.requiredModules = new String[]{"longdouble/handle", "longdouble/head_left", "longdouble/head_right"};

    }

}