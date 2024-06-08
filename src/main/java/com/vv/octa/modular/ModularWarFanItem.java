package com.vv.octa.modular;

import javax.annotation.Nullable;
import com.vv.octa.Octa;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.gui.GuiModuleOffsets;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.RepairSchematic;

public class ModularWarFanItem extends ItemModularHandheld{
    public final static String bladeKey = "warfan/blade";
    public final static String boneKey = "warfan/bone";
    public final static String fanfaceKey = "warfan/fanface";
    public static final String identifier = "modular_warfan";
    private static final GuiModuleOffsets majorOffsets = new GuiModuleOffsets(new int[]{1, -3, -11, 21});
    private static final GuiModuleOffsets minorOffsets = new GuiModuleOffsets(new int[]{-14, 0});

    @ObjectHolder(registryName = "item", value = Octa.MODID + ":" + identifier)
    public static ModularWarFanItem instance;

    public ModularWarFanItem() {
        
        super((new Item.Properties()).stacksTo(1).fireResistant());

        blockDestroyDamage = 2;
        entityHitDamage = 1;

        majorModuleKeys = new String[]{bladeKey, boneKey};
        minorModuleKeys = new String[]{fanfaceKey};
        requiredModules = new String[]{boneKey};

        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, identifier));
    }
    
    @Override
    public void commonInit(PacketHandler packetHandler) {
        DataManager.instance.synergyData.onReload(() -> synergies = DataManager.instance.getSynergyData("warfan/"));
    }

    public void updateConfig(int honeBase, int honeIntegrityMultiplier) {
        this.honeBase = honeBase;
        this.honeIntegrityMultiplier = honeIntegrityMultiplier;
    }

    @Override
    public String getModelCacheKey(ItemStack itemStack, LivingEntity entity) {
        String var10000;
        if (this.isThrowing(itemStack, entity)) {
           var10000 = super.getModelCacheKey(itemStack, entity);
           return var10000 + ":throwing";
        } else {
           return super.getModelCacheKey(itemStack, entity);
        }
     }

    @OnlyIn(Dist.CLIENT)
    public String getTransformVariant(ItemStack itemStack, @Nullable LivingEntity entity) {
        return this.isThrowing(itemStack, entity) ? "throwing" : null;
    }

    @OnlyIn(Dist.CLIENT)
    public GuiModuleOffsets getMajorGuiOffsets(ItemStack itemStack) {
        return majorOffsets;
    }

    @OnlyIn(Dist.CLIENT)
    public GuiModuleOffsets getMinorGuiOffsets(ItemStack itemStack) {
        return minorOffsets;
    }
}
