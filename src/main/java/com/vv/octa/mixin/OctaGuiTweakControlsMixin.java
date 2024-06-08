package com.vv.octa.mixin;

import com.vv.octa.client.gui.tweakColorController.GuiSynchronousDataBase;
import com.vv.octa.client.gui.tweakColorController.GuiTweakColorPreviewer;
import com.vv.octa.client.gui.tweakColorController.GuiTweakColorSlider;
import com.vv.octa.modular.ModularLaserArtilleryItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiButton;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.tetra.blocks.workbench.gui.GuiTweakControls;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.module.ItemModule;
import se.mickelus.tetra.module.data.TweakData;

import static com.vv.octa.effect.gui.EffectGuiStats.*;

@Mixin(GuiTweakControls.class)
public class OctaGuiTweakControlsMixin{
    @Final
    @Shadow(remap = false)
    private GuiElement tweakControls;

    @Final
    @Shadow(remap = false)
    private GuiButton applyButton;

    @Final
    @Shadow(remap = false)
    private GuiString untweakableLabel;

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lse/mickelus/mutil/gui/GuiElement;clearChildren()V", shift = At.Shift.AFTER), remap = false, cancellable = true)
    public void updateMixin(ItemModule module, ItemStack itemStack, CallbackInfo ci) {
        if (module != null && module.isTweakable(itemStack) && module.getSlot().equals(ModularLaserArtilleryItem.windowKey)) {
            TweakData[] data = module.getTweaks(itemStack);
            final int rgbHeight = 14;
            GuiSynchronousDataBase guiSynchronousDataBase = new GuiSynchronousDataBase();

            if (module.getKey().contains("rgb")) {
                this.tweakControls.setHeight(data.length * rgbHeight);
                guiSynchronousDataBase.putCache("R", 256 + ((ModularItem) itemStack.getItem()).getEffectLevel(itemStack, RValue));
                guiSynchronousDataBase.putCache("G", 256 + ((ModularItem) itemStack.getItem()).getEffectLevel(itemStack, GValue));
                guiSynchronousDataBase.putCache("B", 256 + ((ModularItem) itemStack.getItem()).getEffectLevel(itemStack, BValue));

                for(int i = 0; i < data.length; ++i) {
                    TweakData tweak = data[i];
                    GuiTweakColorSlider slider = new GuiTweakColorSlider(guiSynchronousDataBase, -20, i * rgbHeight - 4, 160, tweak, (step) -> this.applyTweak(tweak.key, step), tweak.key);
                    slider.setAttachment(GuiAttachment.topCenter);
                    slider.setValue(module.getTweakStep(itemStack, tweak));
                    this.tweakControls.addChild(slider);
                }

            } else if (module.getKey().contains("hs")) {
                this.tweakControls.setHeight(data.length * 22);
                guiSynchronousDataBase.putCache("H", 360 + ((ModularItem) itemStack.getItem()).getEffectLevel(itemStack, HValue));
                guiSynchronousDataBase.putCache("S", 100 + ((ModularItem) itemStack.getItem()).getEffectLevel(itemStack, SValue));

                for(int i = 0; i < data.length; ++i) {
                    TweakData tweak = data[i];
                    GuiTweakColorSlider slider = new GuiTweakColorSlider(guiSynchronousDataBase, -20, i * 22, 160, tweak, (step) -> this.applyTweak(tweak.key, step), tweak.key);
                    slider.setAttachment(GuiAttachment.topCenter);
                    slider.setValue(module.getTweakStep(itemStack, tweak));
                    this.tweakControls.addChild(slider);
                }
            }

            GuiTweakColorPreviewer previewer = new GuiTweakColorPreviewer(guiSynchronousDataBase, 85, 2, 38);
            previewer.setAttachment(GuiAttachment.topCenter);
            this.tweakControls.addChild(previewer);

            this.applyButton.setVisible(true);
            this.untweakableLabel.setVisible(false);
            ci.cancel();
        }
    }

    @Shadow(remap = false)
    private void applyTweak(String key, int step) {
    }
}