package com.vv.octa.modular;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.InitializableItem;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.data.EffectData;
import se.mickelus.tetra.module.data.ItemProperties;
import se.mickelus.tetra.module.data.SynergyData;
import se.mickelus.tetra.properties.AttributeHelper;

public abstract class ModularAmmo extends ArrowItem implements IModularItem, InitializableItem{

    protected String[] majorModuleKeys;
    protected String[] minorModuleKeys;
    protected String[] requiredModules;
    protected SynergyData[] synergies;
    private final Cache<String, Multimap<Attribute, AttributeModifier>> attributeCache;
    private final Cache<String, EffectData> effectCache;
    private final Cache<String, ItemProperties> propertyCache;

    public ModularAmmo(Properties properties) {
        super(properties);
        this.attributeCache = CacheBuilder.newBuilder().maximumSize(1000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();
        this.effectCache = CacheBuilder.newBuilder().maximumSize(1000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();
        this.propertyCache = CacheBuilder.newBuilder().maximumSize(1000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();
        this.requiredModules = new String[0];
        this.synergies = new SynergyData[0];
        DataManager.instance.moduleData.onReload(this::clearCaches);
    }

    @Override
    public String[] getMajorModuleKeys(ItemStack itemStack) {
        return this.majorModuleKeys;
    }
     
    @Override
    public String[] getMinorModuleKeys(ItemStack itemStack) {
        return this.minorModuleKeys;
    }

    @Override
    public String[] getRequiredModules(ItemStack itemStack) {
        return this.requiredModules;
    }

    @Override
    public boolean canGainHoneProgress(ItemStack arg0) {
        return false;
    }

    @Override
    public void clearCaches() {
        this.attributeCache.invalidateAll();
        this.effectCache.invalidateAll();
        this.propertyCache.invalidateAll();
    }

    @Override
    public SynergyData[] getAllSynergyData(ItemStack arg0) {
        return this.synergies;
    }

    @Override
    public Cache<String, Multimap<Attribute, AttributeModifier>> getAttributeModifierCache() {
        return this.attributeCache;
    }

    @Override
    public Cache<String, EffectData> getEffectDataCache() {
        return this.effectCache;
    }

    @Override
    public Cache<String, ItemProperties> getPropertyCache() {
        return this.propertyCache;
    }

    @Override
    public int getHoneBase(ItemStack arg0) {
        return 0;
    }

    @Override
    public int getHoneIntegrityMultiplier(ItemStack arg0) {
        return 0;
    }

    @Override
    public Item getItem() {
        return this;
    }

    public void onCraftedBy(@NotNull ItemStack itemStack, @NotNull Level world, @NotNull Player player) {
        IModularItem.updateIdentifier(itemStack);
    }

    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level world, List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.addAll(this.getTooltip(stack, world, flag));
    }

    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.literal(this.getItemName(stack));
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack itemStack) {
        if (this.isBroken(itemStack)) {
            return AttributeHelper.emptyMap;
        } else if (slot == EquipmentSlot.MAINHAND) {
            return this.getAttributeModifiersCached(itemStack);
        } else {
            return slot == EquipmentSlot.OFFHAND
                    ? this.getAttributeModifiersCached(itemStack).entries().stream().filter((entry) -> entry.getKey().equals(Attributes.ARMOR)
                            || entry.getKey().equals(Attributes.ARMOR_TOUGHNESS)).collect(Multimaps.toMultimap(Map.Entry::getKey, Map.Entry::getValue, ArrayListMultimap::create))
                    : AttributeHelper.emptyMap;
        }
    }

    public Multimap<Attribute, AttributeModifier> getEffectAttributes(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> result = ArrayListMultimap.create();
        Optional.of(this.getCounterWeightBonus(itemStack)).filter((bonus) -> bonus > 0.0).map((bonus) -> new AttributeModifier("counterweight", bonus, AttributeModifier.Operation.ADDITION)).ifPresent((modifier) -> result.put(Attributes.ATTACK_SPEED, modifier));
        return result;
    }

    public double getCounterWeightBonus(ItemStack itemStack) {
        int counterWeightLevel = this.getEffectLevel(itemStack, ItemEffect.counterweight);
        if (counterWeightLevel > 0) {
            int integrityCost = IModularItem.getIntegrityCost(itemStack);
            return getCounterWeightBonus(counterWeightLevel, integrityCost);
        } else {
            return 0.0;
        }
    }

    public static double getCounterWeightBonus(int counterWeightLevel, int integrityCost) {
        return Math.max(0.0, 0.15 - (double)Math.abs(counterWeightLevel - integrityCost) * 0.05);
    }
}
