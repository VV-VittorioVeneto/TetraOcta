package com.vv.octa.entity;

import com.google.common.collect.Multimap;
import com.vv.octa.effect.WindChargeEffect;
import com.vv.octa.effect.gui.EffectGuiStats;
import com.vv.octa.init.OctaAttributesRegistry;
import com.vv.octa.init.OctaEntitiesRegistry;
import com.vv.octa.init.OctaItemsRegistry;
import com.vv.octa.modular.ModularBoomerangItem;
import com.vv.octa.util.OctaUtils;
import com.vv.octa.util.UUIDHelper;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import se.mickelus.tetra.items.modular.ModularItem;

import java.util.Objects;
import java.util.UUID;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BoomerangEntity extends Projectile implements ItemSupplier {

    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(BoomerangEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Boolean> DATA_RETURNING = SynchedEntityData.defineId(BoomerangEntity.class, EntityDataSerializers.BOOLEAN);

    public static final float HITBOX_SIZE = 0.50F;
	private static final String TAG_RETURNING = "returning";
	private static final String TAG_LIVE_TIME = "liveTime";
	private static final String TAG_HIT_TIME = "hitTime";
	private static final String TAG_BLOCKS_BROKEN = "hitCount";
	private static final String TAG_RETURN_SLOT = "returnSlot";
	private static final String TAG_ITEM_STACK = "Item";
	private LivingEntity owner;
	private UUID ownerId;
	private int liveTime = 0;
	private int hitTime = 0;
    private int timeOut;
    private int hitOut = 2;
	private int slot;
	private int blockHitCount;
	private final float maxHardness = 20;
	private IntOpenHashSet entitiesHit;
	private final Level level;

    public BoomerangEntity(EntityType<? extends BoomerangEntity> entityType, Level level) {
        super(entityType, level);
		this.level = level;
    }

    @Override
	public boolean isPushedByFluid() {
		return false;
	}

    public static void shootFromEntity(ServerLevel level, LivingEntity shooter, ItemStack stack) {
        final double speed = ((ModularBoomerangItem)stack.getItem()).getAttributeValue(stack, OctaAttributesRegistry.BOOMERANG_SPEED.get()) +  stack.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY) * 0.1;
        final int timeout = (int)((ModularBoomerangItem)stack.getItem()).getAttributeValue(stack, OctaAttributesRegistry.BOOMERANG_TIME.get());
        OctaEntitiesRegistry.BOOMERANG.get().spawn(level, null, (entity) -> {
			entity.setTimeOut(timeout);
            entity.setItem(stack);
            entity.setPos(shooter.getEyePosition().subtract(0, HITBOX_SIZE / 2, 0));
            entity.setRot(shooter.getXRot(), shooter.getYRot());
            entity.setDeltaMovement(shooter.getLookAngle().multiply(speed, speed, speed));
            entity.ownerId = shooter.getUUID();
        }, BlockPos.ZERO, MobSpawnType.COMMAND, false, false);
    }

    @Nullable
	public LivingEntity getThrower() {
        return UUIDHelper.getAndCacheLivingEntity(this.level(), owner, ownerId);
	}

    @Nullable
	protected EntityHitResult rayCastEntities(Vec3 from, Vec3 to) {
		return ProjectileUtil.getEntityHitResult(this.level, this, from, to, getBoundingBox().expandTowards(getDeltaMovement()).inflate(1.0D), (entity) -> !entity.isSpectator()
				&& entity.isAlive()
				&& (entity.isPickable() || entity instanceof BoomerangEntity)
				&& entity != getThrower()
				&& (entitiesHit == null || !entitiesHit.contains(entity.getId())));
	}

	@Override
	public void lerpMotion(double x, double y, double z) {
		this.setDeltaMovement(x, y, z);
		if(this.xRotO == 0.0F && this.yRotO == 0.0F) {
			float f = (float) Math.sqrt(x * x + z * z);
			setYRot((float) (Mth.atan2(x, z) * (180F / (float) Math.PI)));
			setXRot((float) (Mth.atan2(y, f) * (180F / (float) Math.PI)));
			this.yRotO = this.getYRot();
			this.xRotO = this.getXRot();
		}
	}

	@NotNull
	@Override
	public SoundSource getSoundSource() {
		return SoundSource.PLAYERS;
	}

	@NotNull
	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

    protected void checkImpact() {
		if(this.level.isClientSide)
			return;

		Vec3 motion = this.getDeltaMovement();
		Vec3 position = this.position();
		Vec3 rayEnd = position.add(motion);

		boolean doEntities = true;
		int tries = 100;

		while(isAlive() && !isReturning()) {
			if(doEntities) {
				EntityHitResult result = rayCastEntities(position, rayEnd);
				if(result != null)
					onHit(result);
				else
					doEntities = false;
			} else {
				HitResult result = this.level.clip(new ClipContext(position, rayEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
				if(result.getType() == Type.MISS)
					return;

				else {
					onHit(result);
				}
			}

			if(tries-- <= 0) {
				return;
			}
		}
	}

    public void setTimeOut(int i){
        this.timeOut = i;
    }

	public void setHitOut(int i){
        this.hitOut = i;
    }

    @Override
    public void tick() {
        Vec3 pos = position();

		this.xOld = pos.x;
		this.yOld = pos.y;
		this.zOld = pos.z;
		super.tick();

		if(!isReturning())
			checkImpact();

		Vec3 ourMotion = this.getDeltaMovement();
		setPos(pos.x + ourMotion.x, pos.y + ourMotion.y, pos.z + ourMotion.z);

		float f = (float) ourMotion.horizontalDistance();
		setYRot((float) (Mth.atan2(ourMotion.x, ourMotion.z) * (180F / (float) Math.PI)));

		setXRot((float) (Mth.atan2(ourMotion.y, f) * (180F / (float) Math.PI)));
		while(this.getXRot() - this.xRotO < -180.0F)
			this.xRotO -= 360.0F;

		while(this.getXRot() - this.xRotO >= 180.0F)
			this.xRotO += 360.0F;

		while(this.getYRot() - this.yRotO < -180.0F)
			this.yRotO -= 360.0F;

		while(this.getYRot() - this.yRotO >= 180.0F)
			this.yRotO += 360.0F;

		setXRot(Mth.lerp(0.2F, this.xRotO, this.getXRot()));
		setYRot(Mth.lerp(0.2F, this.yRotO, this.getYRot()));

		float drag;
		if(this.isInWater()) {
			for(int i = 0; i < 4; ++i) {
				this.level.addParticle(ParticleTypes.BUBBLE, pos.x - ourMotion.x * 0.25D, pos.y - ourMotion.y * 0.25D, pos.z - ourMotion.z * 0.25D, ourMotion.x, ourMotion.y, ourMotion.z);
			}
			drag = 0.8F;
		} else
			drag = 0.99F;
		this.setDeltaMovement(ourMotion.scale(drag));

		pos = position();
		this.setPos(pos.x, pos.y, pos.z);

		if(!isAlive())
			return;

		ItemStack stack = getItem();

		liveTime++;
		if(blockHitCount > 0)hitTime++;

		LivingEntity owner = getThrower();
		if(owner == null || !owner.isAlive() || !(owner instanceof Player)) {
			if(!this.level.isClientSide) {
				while(isInWall())
					setPos(getX(), getY() + 1, getZ());
				spawnAtLocation(stack, 0);
				discard();
			}
			return;
		}

		if(!isReturning()) {
			if(liveTime > timeOut || hitTime > hitOut)
				setReturning(true);
			if(!this.level.getWorldBorder().isWithinBounds(getBoundingBox()))
				spark();
		} else {
			noPhysics = true;

			List<ItemEntity> items = this.level.getEntitiesOfClass(ItemEntity.class, getBoundingBox().inflate(2));
			List<ExperienceOrb> xp = this.level.getEntitiesOfClass(ExperienceOrb.class, getBoundingBox().inflate(2));

			Vec3 ourPos = position();
			for(ItemEntity item : items) {
				if(item.isPassenger())
					continue;
				item.startRiding(this);
				item.setPickUpDelay(5);
			}

			for(ExperienceOrb xpOrb : xp) {
				if(xpOrb.isPassenger())
					continue;
				xpOrb.startRiding(this);
			}

			final double returnSpeed = (1 - OctaUtils.inverse(liveTime,0.4F, 50)) * ((ModularBoomerangItem)stack.getItem()).getAttributeValue(stack, OctaAttributesRegistry.BOOMERANG_SPEED.get()) + 0.1 * getEfficiencyModifier();
			Vec3 ownerPos = owner.position().add(0, 1, 0);
			Vec3 motion = ownerPos.subtract(ourPos);
			double motionMag = 3.25 + 0.25 * getEfficiencyModifier();

			if(motion.lengthSqr() < motionMag) {
				Player player = (Player) owner;
				Inventory inventory = player.getInventory();
				ItemStack stackInSlot = inventory.getItem(slot);

				if(!this.level.isClientSide) {

					if(!stack.isEmpty())
						if(player.isAlive() && stackInSlot.isEmpty())
							inventory.setItem(slot, stack);
						else if(!player.isAlive() || !inventory.add(stack))
							player.drop(stack, false);

					if(player.isAlive()) {
						for(ItemEntity item : items)
							if(item.isAlive())
								giveItemToPlayer(player, item);

						for(ExperienceOrb xpOrb : xp)
							if(xpOrb.isAlive())
								xpOrb.playerTouch(player);

						for(Entity riding : getPassengers()) {
							if(!riding.isAlive())
								continue;

							if(riding instanceof ItemEntity)
								giveItemToPlayer(player, (ItemEntity) riding);
							else if(riding instanceof ExperienceOrb)
								riding.playerTouch(player);
						}
					}

					discard();
				}
			} else
				setDeltaMovement(motion.normalize().scale(returnSpeed));
		}
    }

	protected boolean canDestroyBlock(BlockState state) {
		return !state.is(ModularBoomerangItem.boomerangImmuneTag);
	}

	//equivalent of BlockState::getDestroyProgress
	private float getBlockDestroyProgress(BlockState state, Player player, BlockGetter levelIn, BlockPos pos) {
		float f = state.getDestroySpeed(levelIn, pos);
		if(f == -1.0F) {
			return 0.0F;
		} else {
			float i = ForgeHooks.isCorrectToolForDrops(state, player) ? 30 : 100;
			float digSpeed = getPlayerDigSpeed(player, state, pos);
			return (digSpeed / f / i);
		}
	}

	//equivalent of Player::getDigSpeed but without held item stack stuff
	private float getPlayerDigSpeed(Player player, BlockState state, @Nullable BlockPos pos) {
		float f = 1;

		if(MobEffectUtil.hasDigSpeed(player)) {
			f *= 1.0F + (MobEffectUtil.getDigSpeedAmplification(player) + 1) * 0.2F;
		}

		if(player.hasEffect(MobEffects.DIG_SLOWDOWN)) {
			float f1 = switch(Objects.requireNonNull(player.getEffect(MobEffects.DIG_SLOWDOWN)).getAmplifier()) {
			case 0 -> 0.3F;
			case 1 -> 0.09F;
			case 2 -> 0.0027F;
			default -> 8.1E-4F;
			};

			f *= f1;
		}
		if(this.isEyeInFluidType(ForgeMod.WATER_TYPE.get())) {
			f /= 5.0F;
		}
		f = ForgeEventFactory.getBreakSpeed(player, state, f, pos);
		return f;
	}

	public void spark() {
		// playSound(QuarkSounds.ENTITY_boomerang_SPARK, 1, 1);
		setReturning(true);
	}

	public void clank() {
		// playSound(QuarkSounds.ENTITY_boomerang_CLANK, 1, 1);
		setReturning(true);
	}

	public void addHit(Entity entity) {
		if(entitiesHit == null)
			entitiesHit = new IntOpenHashSet(5);
		entitiesHit.add(entity.getId());
		postHit();
	}

	public void postHit() {
		this.hitTime = 0;
		if((entitiesHit == null ? 0 : entitiesHit.size()) + blockHitCount > getPiercingModifier()){
			setReturning(true);
        }
		else if(getPiercingModifier() > 0)
			setDeltaMovement(getDeltaMovement().scale(0.8));
	}

	public void addHit() {
		blockHitCount++;
		postHit();
	}

    public int getEfficiencyModifier() {
		return getItem().getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY);
	}

	public int getPiercingModifier() {
		return getItem().getEnchantmentLevel(Enchantments.PIERCING);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onHit(@NotNull HitResult result) {
		final LivingEntity owner = getThrower();
		final int level_windCharge = ((ModularItem)getItem().getItem()).getEffectLevel(getItem(), EffectGuiStats.windCharge);
        if (!isReturning()) {
            windExplode(level_windCharge);
        }
		if(result.getType() == Type.BLOCK && result instanceof BlockHitResult blockHitResult) {
			BlockPos hit = blockHitResult.getBlockPos();
			BlockState state = this.level.getBlockState(hit);

			// TODO find replacement for BlockState#isSolid since it's deprecated. Vanilla uses it everywhere though
			
			if(getPiercingModifier() == 0 || state.isSolid())
				addHit();

			if(!(owner instanceof ServerPlayer player))
				return;

			
			final boolean interact = WindChargeEffect.hitInteractableBlock(level_windCharge, level, blockHitResult, this, owner);
			//more general way of doing it instead of just checking hardness
			float progress = getBlockDestroyProgress(state, player, this.level, hit);
			if(progress == 0)
				return;

			float equivalentHardness = (1) / (progress * 100);
			final boolean canDestroyBlock = getItem().getItem().mineBlock(getItem(), level, state, hit, owner);
			if(equivalentHardness <= maxHardness
					&& equivalentHardness >= 0
					&& canDestroyBlock(state) && !interact && canDestroyBlock) {
				ItemStack prev = player.getMainHandItem();
				player.setItemInHand(InteractionHand.MAIN_HAND, getItem());
				if(player.gameMode.destroyBlock(hit))
					this.level.levelEvent(null, LevelEvent.PARTICLES_DESTROY_BLOCK, hit, Block.getId(state));
				else
					clank();
				
				setItem(player.getMainHandItem());

				player.setItemInHand(InteractionHand.MAIN_HAND, prev);
			} else
				clank();
		} else if(result.getType() == Type.ENTITY && result instanceof EntityHitResult entityHitResult) {
			Entity hit = entityHitResult.getEntity();

			if(hit != owner) {
				addHit(hit);
				if(hit instanceof BoomerangEntity) {
					((BoomerangEntity) hit).setReturning(true);
					clank();
				} else {
					ItemStack boomerang = getItem();
					Multimap<Attribute, AttributeModifier> modifiers = boomerang.getAttributeModifiers(EquipmentSlot.MAINHAND);

					if(owner != null) {
						ItemStack prev = owner.getMainHandItem();
						owner.setItemInHand(InteractionHand.MAIN_HAND, boomerang);
						owner.getAttributes().addTransientAttributeModifiers(modifiers);

						float prevHealth = hit instanceof LivingEntity ? ((LivingEntity) hit).getHealth() : 0;
                  
						if(owner instanceof Player player){
							player.attack(hit);
							hit.hurt(player.damageSources().playerAttack(player),(float)((ModularBoomerangItem)getItem().getItem()).getAttributeValue(getItem(), Attributes.ATTACK_DAMAGE) + 1);
						}
						else{
							owner.doHurtTarget(hit);
						}

						if(hit instanceof LivingEntity && ((LivingEntity) hit).getHealth() == prevHealth)
							clank();

						setItem(owner.getMainHandItem());
						owner.setItemInHand(InteractionHand.MAIN_HAND, prev);
						owner.getAttributes().addTransientAttributeModifiers(modifiers);
					} else {
						Builder mapBuilder = new Builder();
						mapBuilder.add(Attributes.ATTACK_DAMAGE, 1);
						AttributeSupplier map = mapBuilder.build();
						AttributeMap manager = new AttributeMap(map);
						manager.addTransientAttributeModifiers(modifiers);

						ItemStack stack = getItem();
						stack.hurt(1, this.level.random, null);
						setItem(stack);
						hit.hurt(this.level.damageSources().indirectMagic(this, this),
								(float) manager.getValue(Attributes.ATTACK_DAMAGE));
					}
				}
			}
		}
	}

    private void windExplode(int level_windCharge) {
        if (this.level instanceof ServerLevel serverLevel) {
            serverLevel.playSeededSound(null, getX(), getY(), getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 0.4F, 0.8F + this.level.random.nextFloat() * 0.3F, 42L);
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, getX(), getY(), getZ(), 5, 1.5, 1.5, 1.5, 0);
        }
        WindChargeEffect.windExplode(this.getThrower(), this, level_windCharge);
        setReturning(true);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 4096;//64 blocks
    }

    @Override
	protected boolean canAddPassenger(@NotNull Entity passenger) {
		return super.canAddPassenger(passenger) || passenger instanceof ItemEntity || passenger instanceof ExperienceOrb;
	}

    @Override
    public double getPassengersRidingOffset() {
        return 0;
    }

    private void giveItemToPlayer(Player player, ItemEntity itemEntity) {
		itemEntity.setPickUpDelay(0);
		itemEntity.playerTouch(player);

		if(itemEntity.isAlive()) {
			// Player could not pick up everything
			ItemStack drop = itemEntity.getItem();

			player.drop(drop, false);
			itemEntity.discard();
		}
	}

    @Override
    protected void defineSynchedData() {
        entityData.define(DATA_ITEM_STACK, ItemStack.EMPTY);
        entityData.define(DATA_RETURNING, false);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setReturning(tag.getBoolean(TAG_RETURNING));
        liveTime = tag.getInt(TAG_LIVE_TIME);
        hitTime = tag.getInt(TAG_HIT_TIME);
		blockHitCount = tag.getInt(TAG_BLOCKS_BROKEN);
		slot = tag.getInt(TAG_RETURN_SLOT);
        setItem(ItemStack.of(tag.getCompound(TAG_ITEM_STACK)));
        this.ownerId = UUIDHelper.deserializeLivingEntity(tag);
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
		tag.putInt(TAG_LIVE_TIME, liveTime);
		tag.putInt(TAG_HIT_TIME, hitTime);
		tag.putInt(TAG_BLOCKS_BROKEN, blockHitCount);
		tag.putInt(TAG_RETURN_SLOT, slot);
        tag.put(TAG_ITEM_STACK, getItem().save(new CompoundTag()));
        // tag.put(TAG_ITEM_STACK, getItem().serializeNBT());
        tag.putBoolean(TAG_RETURNING, isReturning());
        UUIDHelper.serializeLivingEntity(tag, ownerId);
    }

    protected Item getDefaultItem() {
        return OctaItemsRegistry.BOOMERANG_INSTANCE.get();
    }

    public @NotNull ItemStack getItem() {
        ItemStack itemstack = entityData.get(DATA_ITEM_STACK);
        return itemstack.isEmpty() ? new ItemStack(getDefaultItem()) : itemstack;
    }

    public void setItem(ItemStack stack) {
        if (!stack.is(getDefaultItem()) || stack.hasTag() || stack.getCount() > 0) {
            entityData.set(DATA_ITEM_STACK, stack.copy());
        }
    }

    protected boolean isReturning() {
        return entityData.get(DATA_RETURNING);
    }

    protected void setReturning(boolean returning) {
        entityData.set(DATA_RETURNING, returning);
    }
}
