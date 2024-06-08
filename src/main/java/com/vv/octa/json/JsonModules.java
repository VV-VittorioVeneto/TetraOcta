package com.vv.octa.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class JsonModules {
    private boolean replace = true;//default:false
    private ArrayList<String> slots = new ArrayList<String>();//required
    private ArrayList<String> slotSuffixes;
    private String type;
    private String renderLayer;//default:"base"
    private String tweakKey;
    private ArrayList<String> improvements;
    private ArrayList<Variant> variants = new ArrayList<Variant>();//required
    
    public JsonModules(String type, String slots, String key, String location){
        this.type = type;
        this.slots.add(slots);
        this.variants.add(new Variant(key, location));
    }

    private class Variant{
        public String key;//required
        public String category;//default:"misc"
        public Integer durability;//default:0
        public Float durabilityMultiplier;//default:1
        public Integer integrity;//default:0
        public Integer magicCapacity;//default:0
        public Map<String, Float> attributes;
        public Map<String, Integer> tools;
        public Map<String, Integer> effects;
        public ArrayList<String> materials = new ArrayList<String>();
        public Extract extract;

        public Variant(String key, String location){
            this.key = key;
            this.extract = new Extract(location);
        }
    }

    private class Extract{
        public Map<String, Float> primaryAttributes;
        public Map<String, Float> secondaryAttributes;
        public Map<String, Float> tertiaryAttributes;
        public Map<String, ArrayList<Float>> tools;
        public Float durability;//default:1
        public Integer integrity;//default:1
        public Integer magicCapacity;//default:1
        public Glyph glyph;
        public ArrayList<String> availableTextures = new ArrayList<String>();
        public ArrayList<Map<String,String>> models = new ArrayList<Map<String,String>>();

        public Extract(String location){
            Map<String,String> m = new HashMap<>();
            m.put("location", location);
            this.models.add(m);
            this.glyph = new Glyph("tetra:textures/gui/octa_glyph.png");
        }
    }

    public class Glyph{
        public String textureLocation;
        public Integer textureX;
        public Integer textureY;

        public Glyph(String textureLocation){
            this.textureLocation = textureLocation;
        }
    }

    public String getCatalogy(){
        return this.slots.get(0).split("/")[0];
    }

    public String getFilename(int index){
        return this.variants.get(index).key.split("/")[0];
    }

    public String getType(){
        return this.type;
    }

    public ArrayList<String> getSlots(){
        return this.slots;
    }

    @Nullable
    public ArrayList<String> getSlotSuffixes(){
        return this.slotSuffixes;
    }

    public ArrayList<String> getMaterialsValue(int index){
        return this.variants.get(index).materials;
    }

    public Glyph getGlyph(int index){
        return this.variants.get(index).extract.glyph;
    }

    public JsonModules setReplaceValue(boolean b){
        this.replace = b;
        return this;
    }

    public JsonModules addSlotsValue(String... strs){
        for(String str : strs)this.slots.add(str);
        return this;
    }

    public JsonModules addSlotSuffixesValue(String... strs){
        if(this.slotSuffixes == null)this.slotSuffixes = new ArrayList<String>();
        for(String str : strs)this.slotSuffixes.add(str);
        return this;
    }

    public JsonModules setRenderLayerValue(String str){
        this.renderLayer = str;
        return this;
    }

    public JsonModules addImprovementsValue(String... strs){
        if(this.improvements == null)this.improvements = new ArrayList<String>();
        for(String str : strs)this.improvements.add(str);
        return this;
    }

    public JsonModules setVariantsCategoryValue(int index, String str){
        this.variants.get(index).category = str;
        return this;
    }

    public JsonModules setVariantsDurabilityValue(int index, Integer durability){
        this.variants.get(index).durability = durability;
        return this;
    }

    public JsonModules setVariantsDurabilityMultiplierValue(int index, Float durabilityMultiplier){
        this.variants.get(index).durabilityMultiplier = durabilityMultiplier;
        return this;
    }

    public JsonModules setVariantsIntegrityValue(int index, Integer integrity){
        this.variants.get(index).integrity = integrity;
        return this;
    }

    public JsonModules setVariantsMagicCapacityValue(int index, Integer magicCapacity){
        this.variants.get(index).magicCapacity = magicCapacity;
        return this;
    }

    public JsonModules addVariantsAttributes(int index, String str, Float f){
        if(this.variants.get(index).attributes == null)this.variants.get(index).attributes = new HashMap<String,Float>();
        this.variants.get(index).attributes.put(str, f);
        return this;
    }
    
    public JsonModules addMaterialsValue(int index, String... strs){
        if(this.variants.get(index).materials == null)this.variants.get(index).materials = new ArrayList<String>();
        for(String str : strs)this.variants.get(index).materials.add(str);
        return this;
    }
    
    public JsonModules addAvailableTexturesValue(int index, String... strs){
        if(this.variants.get(index).extract.availableTextures==null)this.variants.get(index).extract.availableTextures = new ArrayList<String>();
        for(String str : strs)this.variants.get(index).extract.availableTextures.add(str);
        return this;
    }

    public JsonModules setTextureX(int index, int x){
        this.variants.get(0).extract.glyph.textureX = x;
        return this;
    }

    public JsonModules setTextureY(int index, int y){
        this.variants.get(0).extract.glyph.textureY = y;
        return this;
    }

    public JsonModules addExtractPrimaryAttributes(int index, String str, Float f){
        if(this.variants.get(index).extract.primaryAttributes == null)this.variants.get(index).extract.primaryAttributes = new HashMap<String,Float>();
        this.variants.get(index).extract.primaryAttributes.put(str, f);
        return this;
    }

    public JsonModules addExtractSecondaryAttributes(int index, String str, Float f){
        if(this.variants.get(index).extract.secondaryAttributes == null)this.variants.get(index).extract.secondaryAttributes = new HashMap<String,Float>();
        this.variants.get(index).extract.secondaryAttributes.put(str, f);
        return this;
    }

    public JsonModules addExtractTertiaryAttributes(int index, String str, Float f){
        if(this.variants.get(index).extract.tertiaryAttributes == null)this.variants.get(index).extract.tertiaryAttributes = new HashMap<String,Float>();
        this.variants.get(index).extract.tertiaryAttributes.put(str, f);
        return this;
    }

    public JsonModules setExtractIntegrityValue(int index, int i){
        this.variants.get(index).extract.integrity = i;
        return this;
    }

    public JsonModules setExtractDurabilityValue(int index, Float f){
        this.variants.get(index).extract.durability = f;
        return this;
    }

    public JsonModules setExtractMagicCapacityValue(int index, int i){
        this.variants.get(index).extract.magicCapacity = i;
        return this;
    }

    public JsonModules setTweakKeyValue(String str){
        this.tweakKey = str;
        return this;
    }

    public JsonModules addExtractTools(int index, String str, Float level, Float eff){
        if(this.variants.get(index).extract.tools == null)this.variants.get(index).extract.tools = new HashMap<String,ArrayList<Float>>();
        ArrayList<Float> arr = new ArrayList<Float>();
        arr.add(level);
        arr.add(eff);
        this.variants.get(index).extract.tools.put(str, arr);
        return this;
    }

    public JsonModules addVariantsTools(int index, String str, int i){
        if(this.variants.get(index).tools == null)this.variants.get(index).tools = new HashMap<String, Integer>();
        this.variants.get(index).tools.put(str, i);
        return this;
    }

    public JsonModules addVariantsEffects(int index, String str, int i){
        if(this.variants.get(index).effects == null)this.variants.get(index).effects = new HashMap<String, Integer>();
        this.variants.get(index).effects.put(str, i);
        return this;
    }

    public String getLocation(int index){
        return this.variants.get(index).extract.models.get(0).get("location");
    }
}