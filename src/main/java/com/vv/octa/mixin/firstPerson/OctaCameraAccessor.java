package com.vv.octa.mixin.firstPerson;

import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.class)
public interface OctaCameraAccessor {
    @Accessor
    void setDetached(boolean value);
}
