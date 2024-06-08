package com.vv.octa.effect.artillery;

import com.vv.octa.effect.gui.StatGetterSpectral;
import com.vv.octa.effect.gui.StatGetterWaveLength;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterDecimal;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

import static com.vv.octa.effect.gui.EffectGuiStats.*;
import static se.mickelus.tetra.gui.stats.StatsHelper.barLength;

public class CompositeSpectralPowerAttribute {
    private static final float velocity = 3000.0F;
    private static final float white_lambda = 545.0F;

    @OnlyIn(Dist.CLIENT)
    public static void init() {
        final IStatGetter effectSpectralStatGetter = new StatGetterSpectral();
        final IStatGetter effectWaveStatGetter = new StatGetterWaveLength();
        final GuiStatBar effectBar = new GuiStatBar
                (0, 0, barLength, artillerySpectralName, 0, 10, false, effectSpectralStatGetter,
                        LabelGetterBasic.decimalLabel, new TooltipGetterDecimal
                        (artillerySpectralTooltip, effectWaveStatGetter));
        WorkbenchStatsGui.addBar(effectBar);
        HoloStatsGui.addBar(effectBar);
    }

    private static class HSV{
        public float H, S, V;

        public HSV(float h, float s, float v){
            H = h;
            S = s;
            V = v;
        }
    }

    private static HSV RGBToHSV(int R, int G, int B){
        final int MAX = Math.max(R, Math.max(G, B));
        final int MIN = Math.min(R, Math.min(G, B));
        final int delta = MAX - MIN;
        float H, S, V;
        if (delta == 0) {
            H = 0;
        } else if (MAX == R){
            H = (((float) (G - B) /delta) % 6) * 60;
        } else if (MAX == G){
            H = (((float) (B - R) /delta) + 2) * 60;
        } else {
            H = (((float) (R - G) /delta) + 4) * 60;
        }
        S = (MAX == 0) ? 0 : 1 - (float) MIN / MAX;
        V = MAX/255.0F;
        return new HSV(H, S, V);
    }

    public static int HSToRGB(int H, int S){
        int r ,g, b;
        int RGB_min, RGB_max;
        int RGB_Adj;

        if (H >= 360) {
            H = 0;
        }

        RGB_max = 255;
        RGB_min = Math.round(((100 - S) / 100F) * 255);

        int i = H / 60;
        int dif = H % 60; /* factorial part of h */

        /* RGB adjustment amount by hue */
        RGB_Adj = Math.round((RGB_max - RGB_min) * dif / 60.0f);

        switch (i) {
            case 0:
            r = RGB_max;
            g = (RGB_min + RGB_Adj);
            b = RGB_min;
                break;

            case 1:
            r = (RGB_max - RGB_Adj);
            g = RGB_max;
            b = RGB_min;
                break;

            case 2:
            r = RGB_min;
            g = RGB_max;
            b = (RGB_min + RGB_Adj);
                break;

            case 3:
            r = RGB_min;
            g = (RGB_max - RGB_Adj);
            b = RGB_max;
                break;

            case 4:
            r = (RGB_min + RGB_Adj);
            g = RGB_min;
            b = RGB_max;
                break;

            default:        // case 5:
            r = RGB_max;
            g = RGB_min;
            b = (RGB_max - RGB_Adj);
                break;
        }
        return (r << 16) + (g << 8) + b;
    }


    private static HSV RGBToHSV(int RGB){
        return RGBToHSV((RGB>>>16) & 0xFF, (RGB>>>8) & 0xFF, (RGB) & 0xFF);
    }

    private static float HSVToEnergy (HSV hsv) {
        final float V = hsv.V;
        return velocity/HSVToWaveLength(hsv) * V;
    }

    public static float RGBToEnergy (int R, int G, int B) {
        return HSVToEnergy(RGBToHSV(R, G, B));
    }

    private static float HSVToWaveLength (HSV hsv) {
        final float H = (hsv.H + 360) % 360;
        final float S = hsv.S;
        float lambda = 0.0F;
        if (H <= 300 && H >= 0) {
            lambda = -0.9F * H + 680;
        } else if (H >= 300 && H <= 360) {
            lambda = 4.5F * H - 940;
        }
        return lambda * S + (1 - S) * white_lambda;
    }

    public static float RGBToEnergy (int RGB) {
        return HSVToEnergy(RGBToHSV(RGB));
    }

    public static float RGBToWaveLength (int RGB) {
        return HSVToWaveLength(RGBToHSV(RGB));
    }
}
