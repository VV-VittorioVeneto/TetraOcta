package com.vv.octa.mixin;

import com.vv.octa.modular.ModularLaserArtilleryItem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import static com.vv.octa.modular.ModularLaserArtilleryItem.TemperatureBarColor;

@Mixin(GuiGraphics.class)
public class OctaGuiGraphicsMixin {
    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isBarVisible()Z"), remap = false)
    public void renderItemDecorationsMixin(Font p_282005_, ItemStack itemStack, int x, int y, String p_282803_, CallbackInfo ci) {
        if (itemStack.isBarVisible()) {
            int l = itemStack.getBarWidth()/2;
            int i = itemStack.getBarColor();
            int j = x + 2;
            int k = y + 12;
            this.fill(RenderType.guiOverlay(), j, k, j + 13, k + 1, 0xFF000000);//black
            this.fill(RenderType.guiOverlay(), j, k, j + l, k + 1, Integer.parseInt("00cbc9", 16)|0xFF000000);
        }
        if (itemStack.getItem() instanceof ModularLaserArtilleryItem artilleryItem && artilleryItem.isTemperatureBarVisible(itemStack)) {
            int j = x + 13;
            int k = y + 13;
//            this.fill(RenderType.guiOverlay(), j - 1, k - 11, j + 2, k + 1, 0x7Fcdedff);//white
            if (artilleryItem.isFiring(itemStack)) {
                int[] laserRender = artilleryItem.getLaserRenderArea();
                if (laserRender.length % 4 == 0) {
                    int color = ((int) (artilleryItem.getLaserAlpha() * 255F) << 24) + artilleryItem.getLaserRGB(itemStack);
                    Octa$RenderAreaFromList(laserRender, color, x, y);
                }
            }

            int[] heatedRender = artilleryItem.getHeatedRenderArea();
            if (heatedRender.length % 4 == 0) {
                int color = (artilleryItem.getHeatedAlpha(itemStack) << 24) + 0xFF4200;
                Octa$RenderAreaFromList(heatedRender, color, x, y);
            }

            this.fill(RenderType.guiOverlay(), j, k - 10, j + 1, k, 0xFF4a4c7e);//black
            for (int i2 = 0; i2 < artilleryItem.getTemperatureBarStage(itemStack); i2++) {
                this.fill(RenderType.guiOverlay(), j, k - i2 - 1, j + 1, k - i2, Integer.parseInt(TemperatureBarColor[i2], 16) | 0xFF000000);
            }
        }
    }

    @Unique
    private void Octa$RenderAreaFromList(int[] laserRender, int color, int x, int y) {
        for (int i1 = 0; i1 < laserRender.length; i1 += 4) {
            int x1 = laserRender[i1] + x;
            int y1 = laserRender[i1 + 1] + y;
            int w1 = laserRender[i1 + 2] + x1;
            int h1 = laserRender[i1 + 3] + y1;
            this.fill(RenderType.guiOverlay(), x1, y1, w1, h1, color);
        }
    }

    @Shadow(remap = false)
    public void fill(RenderType renderType, int j, int k, int i, int i1, int i2) {
    }

}
