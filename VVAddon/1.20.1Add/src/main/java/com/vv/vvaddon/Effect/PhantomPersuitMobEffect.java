package com.vv.vvaddon.Effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;

@SuppressWarnings("null")
public class PhantomPersuitMobEffect extends MobEffect{
	public PhantomPersuitMobEffect() {
		super(MobEffectCategory.NEUTRAL, -6750055);
	}

	@Override
	public String getDescriptionId() {
		return "effect.vvaddon.phantom_persuit";
	}


    public void addAttributeModifiers(LivingEntity entity, AttributeMap map, int level) {
		super.addAttributeModifiers(entity, map, level);
	}

    @Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration % 20 == 0;
	}

	@Override
	public void initializeClient(java.util.function.Consumer<IClientMobEffectExtensions> consumer) {
		consumer.accept(new IClientMobEffectExtensions() {
			@Override
			public boolean isVisibleInGui(MobEffectInstance effect) {
				return false;
			}
		});
	}
    
}
