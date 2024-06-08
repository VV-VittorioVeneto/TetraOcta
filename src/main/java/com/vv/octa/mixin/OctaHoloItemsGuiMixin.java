package com.vv.octa.mixin;

import java.util.function.Consumer;
import java.util.function.BiConsumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.vv.octa.modular.ModularBoomerangItem;
import com.vv.octa.modular.ModularLongDoubleItem;
import com.vv.octa.modular.ModularLongSingleItem;

import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.injection.At;

import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloItemGui;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloItemsGui;

@Mixin(HoloItemsGui.class)
public abstract class OctaHoloItemsGuiMixin extends GuiElement{

    public OctaHoloItemsGuiMixin(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    public void OctaInitMixin(int x, int y, int width, int height, BiConsumer<IModularItem, ItemStack> onItemSelect, Consumer<String> onSlotSelect, Runnable onMaterialsClick, CallbackInfo ci){
        ((HoloItemsGui)(Object)this).addChild((new HoloItemGui(-119, 0, ModularBoomerangItem.instance, 8,
            () -> onItemSelect.accept(ModularBoomerangItem.instance, ModularBoomerangItem.instance.getDefaultStack()),
            onSlotSelect)).setAttachment(GuiAttachment.topCenter));

        ((HoloItemsGui)(Object)this).addChild((new HoloItemGui(-39, -80, ModularLongSingleItem.instance, 9,
            () -> onItemSelect.accept(ModularLongSingleItem.instance, ModularLongSingleItem.instance.getDefaultStack()),
            onSlotSelect)).setAttachment(GuiAttachment.topCenter));

        ((HoloItemsGui)(Object)this).addChild((new HoloItemGui(41, -80, ModularLongDoubleItem.instance, 10,
            () -> onItemSelect.accept(ModularLongDoubleItem.instance, ModularLongDoubleItem.instance.getDefaultStack()),
            onSlotSelect)).setAttachment(GuiAttachment.topCenter));
    }

}
