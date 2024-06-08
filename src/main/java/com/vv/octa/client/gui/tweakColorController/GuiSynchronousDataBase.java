package com.vv.octa.client.gui.tweakColorController;

import com.vv.octa.effect.artillery.CompositeSpectralPowerAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GuiSynchronousDataBase {
    private final Map<String, Integer> cache;
    private final ArrayList<GuiColorSlider> memberSliders = new ArrayList<>();
    private final ArrayList<GuiColorPreviewer> memberPreviewers = new ArrayList<>();

    public GuiSynchronousDataBase() {
        cache = new HashMap<>();
    }

    public void putCache(String key, Integer value){
        cache.put(key, value);
    }

    public void updateCache(String key, Integer value){
        if(isChanged(key, value)){
            cache.put(key, value);
            updateMemberSliders();
        }
    }

    public int getCache(String key){
        return cache.get(key);
    }

    public void updateMemberSliders() {
        for (GuiColorSlider slider : memberSliders) {
            slider.updateSliderColor();
        }
        for (GuiColorPreviewer previewer : memberPreviewers) {
            previewer.updatePreviewerColor();
        }
    }

    public void join(GuiColorSlider slider) {
        memberSliders.add(slider);
    }

    public void join(GuiColorPreviewer previewer) {
        memberPreviewers.add(previewer);
    }

    private boolean isChanged (String key, Integer value) {
        return !Objects.equals(cache.get(key), value);
    }

    int getRGBColor() {
        if (cache.containsKey("H") && cache.containsKey("S")) {
            return CompositeSpectralPowerAttribute.HSToRGB(cache.get("H"), cache.get("S"));
        } else if (cache.containsKey("R") && cache.containsKey("G") && cache.containsKey("B")) {
            return (cache.get("R") << 16) + (cache.get("G") << 8) + cache.get("B");
        } else {
            return 0;
        }
    }
}
