package com.vv.octa.modular;

import com.vv.octa.Octa;
import com.vv.octa.entity.OctaArrowEntity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;
import org.jetbrains.annotations.NotNull;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.gui.GuiModuleOffsets;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.RepairSchematic;

public class ModularArrowItem extends ModularAmmo{

    public static final String headKey = "arrow/head";
    public static final String shaftKey = "arrow/shaft";
    public static final String fletchingKey = "arrow/fletching";
    public static final String fullerKey = "arrow/fuller";
    public static final String identifier = "modular_arrow";
    private static final GuiModuleOffsets majorOffsets = new GuiModuleOffsets(1, -3, -11, 21, 1, 21);
    private static final GuiModuleOffsets minorOffsets = new GuiModuleOffsets(-14, 0);

    @ObjectHolder(registryName = "item", value = Octa.MODID + ":" + identifier)
    public static ModularArrowItem instance;

    public ModularArrowItem() {
        super((new Item.Properties()).stacksTo(64));
        
        majorModuleKeys = new String[]{headKey, shaftKey, fletchingKey};
        minorModuleKeys = new String[]{fullerKey};
        requiredModules = new String[]{headKey, shaftKey, fletchingKey};

        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, identifier));
    }
    
    @Override
    public void commonInit(PacketHandler packetHandler) {
        DataManager.instance.synergyData.onReload(() -> synergies = DataManager.instance.getSynergyData("arrow/"));
    }

    @OnlyIn(Dist.CLIENT)
    public GuiModuleOffsets getMajorGuiOffsets(ItemStack itemStack) {
        return majorOffsets;
    }

    @OnlyIn(Dist.CLIENT)
    public GuiModuleOffsets getMinorGuiOffsets(ItemStack itemStack) {
        return minorOffsets;
    }

    @Override
    public @NotNull AbstractArrow createArrow(@NotNull Level worldIn, @NotNull ItemStack itemStack, @NotNull LivingEntity livingEntity) {
        return new OctaArrowEntity(livingEntity, worldIn, itemStack);
    }
}
