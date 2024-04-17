package com.vv.vvaddon.Renderer;

import com.vv.vvaddon.MainVVAddon;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.AbstractArrow;

public class PhantomArrowRenderer extends ArrowRenderer<AbstractArrow>{
    

    public static final ResourceLocation TEXTURE = new ResourceLocation(MainVVAddon.MODID,"textures/entity/phantom_arrow.png");

    public PhantomArrowRenderer(EntityRendererProvider.Context ctx) {

        super(ctx);
    }

    @SuppressWarnings("null")
    @Override
    public ResourceLocation getTextureLocation(AbstractArrow entity) {

        return TEXTURE;
    }

}