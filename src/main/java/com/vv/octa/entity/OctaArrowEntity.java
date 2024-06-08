package com.vv.octa.entity;

import com.google.common.collect.Lists;
import com.vv.octa.Octa;
import com.vv.octa.init.OctaAttributesRegistry;
import com.vv.octa.init.OctaEntitiesRegistry;
import com.vv.octa.init.OctaItemsRegistry;
import com.vv.octa.modular.ModularArrowItem;
import com.vv.octa.util.OctaUtils;
import com.vv.octa.util.UUIDHelper;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import se.mickelus.tetra.effect.ItemEffect;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static com.vv.octa.effect.gui.EffectGuiStats.accelerator;
import static com.vv.octa.effect.gui.EffectGuiStats.weightless;
import static com.vv.octa.effect.gui.EffectGuiStats.cluster;
import static com.vv.octa.effect.gui.EffectGuiStats.seeker;

import java.util.List;
import java.util.UUID;

public class OctaArrowEntity extends AbstractArrow{

    private static final EntityDataAccessor<ItemStack> OCTA_ARROW_BOW_ITEM_STACK = SynchedEntityData.defineId(OctaArrowEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<ItemStack> OCTA_ARROW_ITEM_STACK = SynchedEntityData.defineId(OctaArrowEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Float> OCTA_ARROW_BASE_DAMAGE = SynchedEntityData.defineId(OctaArrowEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> OCTA_ARROW_REAL_VELOCITY = SynchedEntityData.defineId(OctaArrowEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> OCTA_ARROW_TRIGGER = SynchedEntityData.defineId(OctaArrowEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> OCTA_ARROW_TICK = SynchedEntityData.defineId(OctaArrowEntity.class, EntityDataSerializers.INT);

    
	private static final String ARROW_ITEM_STACK = "arrow_item";
	private static final String BOW_ITEM_STACK = "bow_item";
	private static final String BASE_DAMAGE = "base_damage";
	private static final String REAL_VELOCITY = "real_velocity";
    private static final String TICK = "tick_count";
    private static final String TRIGGER = "trigger";

    private int knockBack;
    private LivingEntity target;
	private UUID targetId;
    private final SoundEvent soundEvent = this.getDefaultHitGroundSoundEvent();
    @Nullable
    private IntOpenHashSet piercingIgnoreEntityIds;
    @Nullable
    private List<Entity> piercedAndKilledEntities;

    public OctaArrowEntity(LivingEntity livingEntity, Level worldIn, ItemStack itemStack) {
        super(OctaEntitiesRegistry.OCTA_ARROW.get(), livingEntity, worldIn);
        this.setArrowStack(itemStack);
        this.setBowStack(livingEntity.getMainHandItem());
        this.setOctaBaseDamage((float) ((ModularArrowItem)itemStack.getItem()).getAttributeValue(itemStack, OctaAttributesRegistry.ARROW_DAMAGE.get()));
        if(this.getArrowItemEffect(weightless) > 0 || this.getArrowItemEffect(accelerator) > 0){
            this.setNoGravity(true);
        }
        this.knockBack = 1;
    }

    public OctaArrowEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    //NBT
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OCTA_ARROW_BOW_ITEM_STACK,  ItemStack.EMPTY);
        this.entityData.define(OCTA_ARROW_ITEM_STACK,  ItemStack.EMPTY);
        this.entityData.define(OCTA_ARROW_BASE_DAMAGE, 2.0F);
        this.entityData.define(OCTA_ARROW_REAL_VELOCITY, 1.0F);
        this.entityData.define(OCTA_ARROW_TICK, 0);
        this.entityData.define(OCTA_ARROW_TRIGGER, true);
    }

    protected Item getDefaultItem() {
        return OctaItemsRegistry.ARROW_INSTANCE.get();
    }
    public ItemStack getBowStack() {
        ItemStack itemstack = entityData.get(OCTA_ARROW_BOW_ITEM_STACK);
        return itemstack.isEmpty() ? new ItemStack(getDefaultItem()) : itemstack;
    }
    public void setBowStack(ItemStack stack) {
        if (!stack.is(getDefaultItem()) || stack.hasTag() || stack.getCount() > 0) {
            entityData.set(OCTA_ARROW_BOW_ITEM_STACK, stack.copy());
        }
    }

    public ItemStack getArrowStack() {
        ItemStack itemstack = entityData.get(OCTA_ARROW_ITEM_STACK);
        return itemstack.isEmpty() ? new ItemStack(getDefaultItem()) : itemstack;
    }
    public void setArrowStack(ItemStack stack) {
        if (!stack.is(getDefaultItem()) || stack.hasTag() || stack.getCount() > 0) {
            ItemStack itemStack = stack.copy();
            itemStack.setCount(1);
            entityData.set(OCTA_ARROW_ITEM_STACK, itemStack);
        }
    }

    public float getOctaBaseDamage(){
        return entityData.get(OCTA_ARROW_BASE_DAMAGE);
    }
    public void setOctaBaseDamage(float f){
        entityData.set(OCTA_ARROW_BASE_DAMAGE, f);
    }
    
    public float getRealVelocity(){
        return this.entityData.get(OCTA_ARROW_REAL_VELOCITY);
    }
    public void setRealVelocity(float f){
        this.entityData.set(OCTA_ARROW_REAL_VELOCITY, f);
    }

    public boolean getTrigger(){
        return this.entityData.get(OCTA_ARROW_TRIGGER);
    }
    public void setTrigger(boolean b){
        this.entityData.set(OCTA_ARROW_TRIGGER, b);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setArrowStack(ItemStack.of(tag.getCompound(ARROW_ITEM_STACK)));
        setBowStack(ItemStack.of(tag.getCompound(BOW_ITEM_STACK)));
        setOctaBaseDamage(tag.getFloat(BASE_DAMAGE));
        setRealVelocity(tag.getFloat(REAL_VELOCITY));
        tickCount = (tag.getInt(TICK));
        setTrigger(tag.getBoolean(TRIGGER));
		this.targetId = UUIDHelper.deserializeLivingEntity(tag);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put(ARROW_ITEM_STACK, getArrowStack().save(new CompoundTag()));
        tag.put(BOW_ITEM_STACK, getBowStack().save(new CompoundTag()));
        tag.putFloat(BASE_DAMAGE, getOctaBaseDamage());
        tag.putFloat(REAL_VELOCITY, getRealVelocity());
        tag.putInt(TICK, tickCount);
        tag.putBoolean(TRIGGER, getTrigger());
        UUIDHelper.serializeLivingEntity(tag, targetId);
    }

    @NotNull
    @Override
    protected ItemStack getPickupItem() {
        return this.getArrowStack();
    }

    public void tick() {
        super.tick();
        if (this.inGround) {
            return;
        }
        tickCount++;
        if (tickCount == 1) {
            this.setRealVelocity((float)this.getDeltaMovement().length());
            if(this.getArrowItemEffect(accelerator) > 0){
                this.setDeltaMovement(this.getDeltaMovement().normalize().scale(128));
            }
        }
        if (tickCount == 10) this.addClusterArrow(this.getArrowItemEffect(cluster), this);
        if (this.getArrowItemEffect(seeker) > 0) {
            if (canChase(target)) {
                this.chaseTarget();
            } else {
                this.getTarget();
            }
        }
    }

    private int getArrowItemEffect(ItemEffect itemEffect){
        return ((ModularArrowItem)this.getArrowStack().getItem()).getEffectLevel(this.getArrowStack(), itemEffect);
    }

    protected boolean canChase(LivingEntity livingEntity){
        return livingEntity != null && livingEntity.isAlive() && this.level().dimension().equals(livingEntity.level().dimension());
    }

    protected boolean canLock(LivingEntity livingEntity, LivingEntity owner){
        return canChase(livingEntity) && livingEntity.hasLineOfSight(this) && inViewScreen(livingEntity, owner);
    }

    protected boolean inViewScreen(LivingEntity livingEntity, LivingEntity owner){
        final double rayDis = OctaUtils.disToRay(livingEntity.getEyePosition(), owner.getEyePosition(), owner.getViewVector(1.0F));
        final double dis = livingEntity.distanceTo(owner);
        return rayDis < dis * 0.7;
    }

    protected void addClusterArrow(int clusterNum, OctaArrowEntity origin){
        if(origin.getTrigger() && origin.getOwner() instanceof LivingEntity owner && clusterNum > 0){
            final Level worldIn = origin.level();
            final Vec3 originVec3 = origin.getDeltaMovement();
            final double originLength = originVec3.length();
            final Vec3 base1 = originVec3.cross(new Vec3(-originVec3.z, 0, originVec3.x)).normalize();
            final Vec3 base2 = originVec3.cross(base1).normalize();
            for(int i = 0 ; i < clusterNum ; i++){
                OctaArrowEntity tempCluster = new OctaArrowEntity(owner, worldIn, origin.getArrowStack());
                final double coe1 = worldIn.random.nextDouble() * (worldIn.random.nextBoolean() ? 1.0D : -1.0);
                final double coe2 = Math.sqrt(1 - Math.pow(coe1, 2)) * (worldIn.random.nextBoolean() ? 1.0D : -1.0);
                final double randomLength = (0.05 + worldIn.random.nextDouble() * 0.1);
                final Vec3 randomOffsetVec3 = (base1.scale(coe1).add(base2.scale(coe2))).scale(randomLength);
                tempCluster.setBowStack(origin.getBowStack());
                tempCluster.setTrigger(false);
                tempCluster.setDeltaMovement((originVec3.normalize().add(randomOffsetVec3)).scale(originLength));
                tempCluster.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                tempCluster.setPos(origin.getPosition(1.0F).add(randomOffsetVec3.normalize().scale(worldIn.random.nextDouble() * 0.8D)));
                worldIn.addFreshEntity(tempCluster);
            }
        }
    }

    protected void chaseTarget(){
        final LivingEntity currentTarget = UUIDHelper.getAndCacheLivingEntity(this.level(), this.target, targetId);

        final double beta1 = OctaUtils.inverse(this.distanceTo(currentTarget), 0.99F, 8);
        final double beta1_r = Math.sqrt(1 - Math.pow(beta1, 2.0D));
        final double beta2 = OctaUtils.inverse(this.distanceTo(currentTarget), 0.99F, 3);
        final double beta2_r = Math.sqrt(1 - Math.pow(beta2, 2.0D));

        final Vec3 prevVec3 = this.getDeltaMovement();
        final double length = prevVec3.length();
        final Vec3 targetVec3 = currentTarget.getEyePosition(1.0F).subtract(this.getPosition(1.0F));
        final Vec3 nesterovTargetVec3 = currentTarget.getEyePosition(1.0F).subtract(this.getPosition(1.0F).add(prevVec3));

        final Vec3 nesterovNewVec3 = (prevVec3.scale(beta1_r).add(nesterovTargetVec3.normalize().scale(beta1 * prevVec3.length()))).normalize().scale(length);
        final Vec3 newVec3 = (prevVec3.scale(beta1_r).add(targetVec3.normalize().scale(beta1 * prevVec3.length()))).normalize().scale(length);
        final Vec3 mergeNewVec3 = (nesterovNewVec3.scale(beta2_r).add(newVec3.scale(beta2))).normalize().scale(length);

        this.setDeltaMovement(mergeNewVec3);
    }

    protected void getTarget(){
        if(this.targetId != null) {
            this.target = UUIDHelper.getAndCacheLivingEntity(this.level(), this.target, targetId);
        }
        if(this.target == null || !this.target.isAlive() || !this.level().dimension().equals(this.target.level().dimension())){
            scanForTarget();
        }
    }

    protected void scanForTarget(){
        final Entity entityOwner = this.getOwner();
        boolean[] find = {false};
        LivingEntity[] target = new LivingEntity[1];
        double[] Priority = {12800, 0};    //min,temp
        if (entityOwner instanceof LivingEntity owner) {
            final TargetingConditions allCondition = TargetingConditions.forCombat().ignoreLineOfSight().selector(e -> true);
            owner.level().getNearbyEntities(LivingEntity.class, allCondition, owner, this.getBoundingBox().inflate(32.0D)).forEach(entityNear -> {
                if(!canLock(entityNear, owner))return;
                if(entityNear instanceof Monster monster){
                    if(monster.getTarget() == owner){
                        Priority[1] = 1;
                    }else{
                        Priority[1] = 4;
                    }
                } else {
                    Priority[1] = 10;
                }
                Priority[1] *= entityNear.distanceTo(this);
                if(Priority[1] < Priority[0]){
                    find[0] = true;
                    Priority[0] = Priority[1];
                    target[0] = entityNear;
                }
            });
        }
        if(find[0]){
            this.target = target[0];
            this.targetId = target[0].getUUID();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        final Level worldIn = this.level();
        Entity entity = entityHitResult.getEntity();
        float f = this.getRealVelocity();
        int i = Mth.ceil(Mth.clamp((double)f * this.getOctaBaseDamage(), 0.0D, Integer.MAX_VALUE));
        if (this.getPierceLevel() > 0) {
            if (this.piercingIgnoreEntityIds == null) {
                this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
            }

            if (this.piercedAndKilledEntities == null) {
                this.piercedAndKilledEntities = Lists.newArrayListWithCapacity(5);
            }

            if (this.piercingIgnoreEntityIds.size() >= this.getPierceLevel() + 1) {
                this.discard();
                return;
            }

            this.piercingIgnoreEntityIds.add(entity.getId());
        }

        if (this.isCritArrow()) {
            long j = this.random.nextInt(i / 2 + 2);
            i = (int)Math.min(j + (long)i, 2147483647L);
        }

        Entity owner = this.getOwner();
        DamageSource damagesource;
        ItemStack prev = ItemStack.EMPTY;
        if (owner == null) {
            damagesource = this.damageSources().arrow(this, this);
        } else {
            damagesource = this.damageSources().arrow(this, owner);
            if (owner instanceof LivingEntity livingEntity) {
                livingEntity.setLastHurtMob(entity);
                prev = livingEntity.getMainHandItem();
                livingEntity.setItemInHand(InteractionHand.MAIN_HAND, getBowStack());
            }
        }

        boolean flag = entity.getType() == EntityType.ENDERMAN;
        int k = entity.getRemainingFireTicks();
        if (this.isOnFire() && !flag) {
            entity.setSecondsOnFire(5);
        }

        Octa.debug(""+i);
        if (entity.hurt(damagesource, (float)i)) {
            entity.invulnerableTime = 0;
            if (flag) {
                return;
            }

            if (entity instanceof LivingEntity livingentity) {
                if (!worldIn.isClientSide && this.getPierceLevel() <= 0) {
                    livingentity.setArrowCount(livingentity.getArrowCount() + 1);
                }

                if (this.knockBack > 0) {
                    double d0 = Math.max(0.0D, 1.0D - livingentity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    Vec3 vec3 = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double)this.knockBack * 0.6D * d0);
                    if (vec3.lengthSqr() > 0.0D) {
                        livingentity.push(vec3.x, 0.1D, vec3.z);
                    }
                }

                if (!worldIn.isClientSide && owner instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity, owner);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)owner, livingentity);
                }

                this.doPostHurtEffects(livingentity);
                if (livingentity != owner && livingentity instanceof Player && owner instanceof ServerPlayer && !this.isSilent()) {
                    ((ServerPlayer)owner).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                }

                if (!entity.isAlive() && this.piercedAndKilledEntities != null) {
                    this.piercedAndKilledEntities.add(livingentity);
                }

                if (!worldIn.isClientSide && owner instanceof ServerPlayer serverplayer) {
                    if (this.piercedAndKilledEntities != null && this.shotFromCrossbow()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayer, this.piercedAndKilledEntities);
                    } else if (!entity.isAlive() && this.shotFromCrossbow()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayer, List.of(entity));
                    }
                }
            }

            this.playSound(this.soundEvent, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            if (this.getPierceLevel() <= 0) {
                this.discard();
            }
        } else {
            entity.setRemainingFireTicks(k);
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
            this.setYRot(this.getYRot() + 180.0F);
            this.yRotO += 180.0F;
            if (!worldIn.isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
                if (this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            }
        }
        if (owner instanceof LivingEntity livingEntity && !prev.isEmpty()) {
            livingEntity.setItemInHand(InteractionHand.MAIN_HAND, prev);
        }

    }


}
