package com.vv.octa.json;

import java.util.ArrayList;

import com.vv.octa.json.JsonModules.Glyph;

@SuppressWarnings("unused")
public class JsonSchematics{
    private boolean replace;
    private ArrayList<String> slots;
    private ArrayList<String> keySuffixes;
    private int materialSlotCount;
    private String displayType;
    private Glyph glyph;
    private ArrayList<Outcomes> outcomes = new ArrayList<Outcomes>();

    public JsonSchematics(JsonModules m, int countFactor){
        this.replace = true;
        this.slots = m.getSlots();
        this.keySuffixes = m.getSlotSuffixes();
        this.materialSlotCount = 1;
        switch(m.getType()){
            case "tetra:basic_module":
                this.displayType = "minor";
                break;
            case "tetra:basic_major_module":
                this.displayType = "major";
                break;
            default:
                this.displayType = "error";
                break;
        }
        this.glyph = m.getGlyph(0);
        this.outcomes.add(new Outcomes(countFactor, m.getCatalogy() + "/" + m.getFilename(0), m.getFilename(0) + "/"));
        this.outcomes.get(0).materials = m.getMaterialsValue(0);
    }

    public class Outcomes{
        private ArrayList<String> materials;
        private int countFactor;
        private String moduleKey;
        private String moduleVariant;

        public Outcomes(int countFactor, String moduleKey, String moduleVariant){
            this.countFactor = countFactor;
            this.moduleKey = moduleKey;
            this.moduleVariant = moduleVariant;
        }
    }

    public String getCatalogy(){
        return this.slots.get(0).split("/")[0];
    }

    public String getFilename(int index){
        return this.outcomes.get(index).moduleVariant.split("/")[0];
    }

    public JsonSchematics setReplaceValue(boolean b){
        this.replace = b;
        return this;
    }
    
    public JsonSchematics setMaterialSlotCountValue(Integer i){
        this.materialSlotCount = i;
        return this;
    }

    public JsonSchematics addSlotsValue(String... strs){
        for(String str : strs)this.slots.add(str);
        return this;
    }

    public JsonSchematics addMaterialsValue(int index, String... strs){
        if(this.outcomes.get(index).materials == null)this.outcomes.get(index).materials = new ArrayList<String>();
        for(String str : strs)this.outcomes.get(index).materials.add(str);
        return this;
    }
}