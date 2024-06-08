package com.vv.octa.modular;

import com.google.common.collect.Multimap;
import com.vv.octa.Octa;
import com.vv.octa.client.gui.pluginConfigurationController.GuiArtilleryScreen;
import com.vv.octa.damage.DamageSources;
import com.vv.octa.damage.LaserDamageSource;
import com.vv.octa.effect.artillery.CompositeSpectralPowerAttribute;
import com.vv.octa.init.OctaAttributesRegistry;
import com.vv.octa.modular.container.ArtilleryContainer;
import com.vv.octa.network.OpenArtilleryGuiPacket;
import com.vv.octa.util.OctaUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.gui.GuiModuleOffsets;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.RepairSchematic;
import se.mickelus.tetra.properties.AttributeHelper;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.vv.octa.effect.gui.EffectGuiStats.*;

@ParametersAreNonnullByDefault
public class ModularLaserArtilleryItem extends ModularItem implements MenuProvider {
    public final static String pumpKey = "artillery/pump";
    public final static String windowKey = "artillery/window";
    public final static String crystalKey = "artillery/crystal";
    public final static String cavityKey = "artillery/cavity";
    public final static String bodyKey = "artillery/body";
    public final static String pluginKey = "artillery/plugin";
    public static final String identifier = "modular_artillery";

    private static final float preheatTemperature = 400;

    private static final GuiModuleOffsets majorOffsets = new GuiModuleOffsets(2, -4, 1, 23, -11, 23, -12, -4);
    private static final GuiModuleOffsets minorOffsets = new GuiModuleOffsets(-24, 12, 15, 12);
    public static final String[] TemperatureBarColor = {"1c81fe", "79b5ff", "f0ff71", "fff044", "ffc444", "ff8a44", "ff7d44", "ff4200", "920000", "550000"};

    @ObjectHolder(registryName = "item", value = Octa.MODID + ":" + identifier)
    public static ModularLaserArtilleryItem instance;

    public ModularLaserArtilleryItem(){

        super((new Item.Properties()).stacksTo(1));

        majorModuleKeys = new String[]{pumpKey, crystalKey, cavityKey, bodyKey};
        minorModuleKeys = new String[]{windowKey, pluginKey};
        requiredModules = new String[]{pumpKey, crystalKey, cavityKey, bodyKey};

        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, identifier));

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
    public @NotNull InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (this.isBroken(itemStack)) {
            return InteractionResultHolder.pass(itemStack);
        } else {
            if (isCooling(itemStack)) {
                return InteractionResultHolder.fail(itemStack);
            } else {
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(itemStack);
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (isBroken(itemStack)) return;
        boolean isUsing = entityIn instanceof LivingEntity livingEntity && livingEntity.isUsingItem() && livingEntity.getUseItem().equals(itemStack);
        if (!isUsing) {
            setFiringState(itemStack, false);
        }
        if (getTemperature(itemStack) > getAirTemperature() && !isUsing) {
            addTemperature(itemStack, -getCoolingSpeed(itemStack));
        } else if (getTemperature(itemStack) < getAirTemperature() && !isUsing) {
            setTemperature(itemStack, getAirTemperature());
        }
        if (isCooling(itemStack) && getTemperature(itemStack) == getAirTemperature()) {
            setCoolingState(itemStack, false);
        }
    }

    @Override
    public void onUseTick(Level worldIn, LivingEntity entity, ItemStack itemStack, int count) {
        super.onUseTick(worldIn, entity, itemStack, count);
        if (isBroken(itemStack)) return;
        float temperature = getTemperature(itemStack);
        addTemperature(itemStack, getHeatingSpeed(itemStack));
        if (temperature > getHeatResistance(itemStack)) {
            setFiringState(itemStack, false);
            setCoolingState(itemStack, true);
            entity.releaseUsingItem();
        } else if (temperature >= preheatTemperature && temperature <= getHeatResistance(itemStack)) {
            setFiringState(itemStack, true);
        }
        if (isFiring(itemStack)) {
            var hitResult = OctaUtils.raycastForEntity(worldIn, entity, getRange(), true, .15f);
            if (hitResult.getType() == HitResult.Type.ENTITY) {
                Entity target = ((EntityHitResult) hitResult).getEntity();
                if (target instanceof LivingEntity) {
                    DamageSources.applyDamage(target, getTickDamage(itemStack), getDamageSource(entity));
                }
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level world, LivingEntity entity, int timeLeft) {
        setFiringState(itemStack, false);
        Octa.debug("Stop Using");
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack itemStack, Level world, LivingEntity entity) {
        setFiringState(itemStack, false);
        Octa.debug("Finish Using");
        return super.finishUsingItem(itemStack, world, entity);
    }

    private float getTemperature(ItemStack itemStack){
        CompoundTag tag = itemStack.getOrCreateTag();
        if (!tag.contains("temperature")) tag.putFloat("temperature", 0);
        return tag.getFloat("temperature");
    }

    private void setTemperature(ItemStack itemStack, float temperature){
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.putFloat("temperature", temperature);
    }

    private float getAirTemperature(){
        return 0;
    }

    private void setFiringState(ItemStack itemStack, boolean state){
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.putBoolean("fire_state", state);
    }

    private void setCoolingState(ItemStack itemStack, boolean state){
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.putBoolean("cooling_state", state);
    }

    public boolean isCooling(ItemStack itemStack){
        CompoundTag tag = itemStack.getOrCreateTag();
        if (!tag.contains("cooling_state")) tag.putBoolean("cooling_state", false);
        return tag.getBoolean("cooling_state");
    }

    public boolean isFiring(ItemStack itemStack){
        CompoundTag tag = itemStack.getOrCreateTag();
        if (!tag.contains("fire_state")) tag.putBoolean("fire_state", false);
        return tag.getBoolean("fire_state");
    }

    private void addTemperature(ItemStack itemStack, float temperature){
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.putFloat("temperature", tag.getFloat("temperature") + temperature);
    }

    private float getHeatResistance(ItemStack itemStack){
        return (float) this.getAttributeValue(itemStack, OctaAttributesRegistry.ARTILLERY_TEMPERATURE.get());
    }

    private float getCoolingSpeed(ItemStack itemStack){
        return (float) this.getAttributeValue(itemStack, OctaAttributesRegistry.ARTILLERY_COOLING.get());
    }

    private float getHeatingSpeed(ItemStack itemStack){
        return (float) this.getAttributeValue(itemStack, OctaAttributesRegistry.ARTILLERY_HEATING.get());
    }

    public float getCompositeSpectralPower(ItemStack itemStack) {
        return CompositeSpectralPowerAttribute.RGBToEnergy(getCrystalRGB(itemStack));
    }

    public float getTickDamage(ItemStack itemStack){
        float focusEfficiency = (float) this.getAttributeValue(itemStack, OctaAttributesRegistry.ARTILLERY_FOCUS.get());
        float compositeSpectralPower = this.getCompositeSpectralPower(itemStack);
        float pumpPower = (float) this.getAttributeValue(itemStack, OctaAttributesRegistry.ARTILLERY_PUMP.get());
        return focusEfficiency * compositeSpectralPower * pumpPower;
    }

    public float getLaserAlpha() {
        return 0.8F;
    }

    public int getLaserRGB(ItemStack itemStack) {
        if (this.getAllModules(itemStack).stream().anyMatch(m -> m.getKey().contains("rgb_window")) && isWindowOn(itemStack)) {
            return ((getEffectLevel(itemStack, RValue) + 256) << 16) + ((getEffectLevel(itemStack, GValue) + 256) << 8) + getEffectLevel(itemStack, BValue) + 256;
        } else if (this.getAllModules(itemStack).stream().anyMatch(m -> m.getKey().contains("hs_window")) && isWindowOn(itemStack)) {
            return CompositeSpectralPowerAttribute.HSToRGB(getEffectLevel(itemStack, HValue) + 360, getEffectLevel(itemStack, SValue) + 100);
        }
        return getCrystalRGB(itemStack);
    }

    private boolean isSafeMode (ItemStack itemStack) {
        return getEffectLevel(itemStack, safeMode) > 0;
    }

    private boolean isWindowOn (ItemStack itemStack) {
//        return getEffectLevel(itemStack, windowSwitch) > 0;
        return true;
    }

    public float getRange() {
        return 24F;
    }

    public LaserDamageSource getDamageSource(Entity attacker) {
        return LaserDamageSource.source(attacker);
    }

    public int getCrystalRGB(ItemStack itemStack) {
        return getAllModules(itemStack).stream().filter(m -> m.getSlot().contains(ModularLaserArtilleryItem.crystalKey)).map(m -> m.getVariantData(itemStack).models[0].tint).toList().get(0);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack itemStack) {
        if (isBroken(itemStack) || slot == EquipmentSlot.OFFHAND) {
            return AttributeHelper.emptyMap;
        } else if (slot == EquipmentSlot.MAINHAND) {
            return getAttributeModifiersCached(itemStack);
        }
        return AttributeHelper.emptyMap;
    }

    @Override
    public void commonInit(PacketHandler packetHandler) {
        packetHandler.registerPacket(OpenArtilleryGuiPacket.class, OpenArtilleryGuiPacket::new);
        DataManager.instance.synergyData.onReload(() -> synergies = DataManager.instance.getSynergyData("artillery/"));
    }

    @Override
    public void clientInit() {
        super.clientInit();
        MenuScreens.register(ArtilleryContainer.type.get(), GuiArtilleryScreen::new);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 500;
    }

    public int getTemperatureBarStage(ItemStack itemStack){
        float preHeatPercent = Math.min((getTemperature(itemStack) - getAirTemperature()) / (preheatTemperature - getAirTemperature()), 1);
        float fireHeatPercent = Math.max(getTemperature(itemStack) - preheatTemperature, 0) / (getHeatResistance(itemStack) - preheatTemperature);
        return (int) (Math.ceil(preHeatPercent * 2 + fireHeatPercent * (TemperatureBarColor.length - 2)));
    }

    public boolean isTemperatureBarVisible(ItemStack itemStack) {
        return getTemperature(itemStack) > 0;
    }

    public int[] getHeatedRenderArea(){
        return new int[]{
            9, 7, 2, 1,
            10, 6, 2, 1,
            11, 5, 2, 1,
            12, 4, 2, 1,
            13, 3, 2, 1,
            14, 2, 2, 1,

            13, 0, 1, 2,
            12, 1, 1, 2,
            11, 2, 1, 2,
            10, 3, 1, 2,
            9, 4, 1, 2,
            8, 5, 1, 2,
        };
    }

    public int[] getLaserRenderArea(){
        return new int[]{
            14, 0, 2, 2,
            13, 2, 1, 1,
            12, 3, 1, 1,
            11, 4, 1, 1,
            10, 5, 1, 1,
            9, 6, 1, 1,
            8, 7, 1, 1,
        };
    }

    public int getHeatedAlpha(ItemStack itemStack) {
        return (int) Math.min((Math.max(getTemperature(itemStack) - preheatTemperature, 0) / (getHeatResistance(itemStack) - preheatTemperature)) * 95, 95);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal(this.toString());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
        ItemStack itemStack = player.getMainHandItem();
        return new ArtilleryContainer(windowId, inventory, itemStack, player);
    }

    public int getSlotNum(ItemStack itemStack) {
        return 4;
//        return this.getAllModules(itemStack).stream().map((module) -> module.getEffectLevel(itemStack, slotNumber)).reduce(0, Integer::sum);
    }

}
