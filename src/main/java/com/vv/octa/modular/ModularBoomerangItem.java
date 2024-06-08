package com.vv.octa.modular;

import java.util.Collection;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.vv.octa.Octa;
import com.vv.octa.common.BRSounds;
import com.vv.octa.entity.BoomerangEntity;

import com.google.common.collect.Lists;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.gui.GuiModuleOffsets;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.RepairSchematic;
import se.mickelus.tetra.properties.AttributeHelper;


@ParametersAreNonnullByDefault
public class ModularBoomerangItem extends ItemModularHandheld{
    public final static String bladeKey = "boomerang/blade";
    public final static String staveKey = "boomerang/stave";
    public final static String bindingKey = "boomerang/binding";
    public static final String identifier = "modular_boomerang";
    private static final GuiModuleOffsets majorOffsets = new GuiModuleOffsets(1, -3, -11, 21);
    private static final GuiModuleOffsets minorOffsets = new GuiModuleOffsets(-14, 0);
    public static final TagKey<Block> boomerangImmuneTag = BlockTags.create(new ResourceLocation(Octa.MODID, "boomerang_immune"));
    @ObjectHolder(registryName = "item", value = Octa.MODID + ":" + identifier)
    public static ModularBoomerangItem instance;

    public ModularBoomerangItem(){
        
        super((new Item.Properties()).stacksTo(1).fireResistant());
        blockDestroyDamage = 1;
        entityHitDamage = 1;

        majorModuleKeys = new String[]{bladeKey, staveKey};
        minorModuleKeys = new String[]{bindingKey};
        requiredModules = new String[]{staveKey};

        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, identifier));
    }

    public static Collection<ItemStack> getCreativeTabItemStacks() {
        return Lists.newArrayList(createItemStack("oak", "iron", "diamond"));
    }

    public static ItemStack createItemStack(String staveMaterial, String bladeMaterial, String bindingMaterial) {
        ItemStack itemStack = new ItemStack(instance);
        IModularItem.putModuleInSlot(itemStack, "boomerang/blade", "boomerang/basic_blade", "boomerang/basic_blade_material",
                "basic_blade/" + bladeMaterial);
        IModularItem.putModuleInSlot(itemStack, "boomerang/stave", "boomerang/basic_stave", "boomerang/basic_stave_material",
                "basic_stave/" + staveMaterial);
        IModularItem.putModuleInSlot(itemStack, "boomerang/binding", "boomerang/binding", "boomerang/binding_material",
                "binding/" + bindingMaterial);
        IModularItem.updateIdentifier(itemStack);
        return itemStack;
    }
    
    @Override
    public void commonInit(PacketHandler packetHandler) {
        DataManager.instance.synergyData.onReload(() -> synergies = DataManager.instance.getSynergyData("boomerang/"));
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
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            BoomerangEntity.shootFromEntity((ServerLevel) level, player, Util.make(stack.copy(), stack1 -> stack1.setCount(1)));
        }
        stack = ItemStack.EMPTY;
        level.playSound(player, player.getX(), player.getY(), player.getZ(), BRSounds.BANANARANG_THROW.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        return InteractionResultHolder.success(stack);
    }

    public double getAttributeValue(ItemStack itemStack, Attribute attribute) {
        if (isBroken(itemStack)) {
            return 0;
        }
        return AttributeHelper.getMergedAmount(getAttributeModifiersCached(itemStack).get(attribute));
    }

}