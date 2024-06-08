package com.vv.octa.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
 
public class GenerateJSON {

    private static final ArrayList<JsonModules> modules = new ArrayList<>();
    private static final ArrayList<JsonSchematics> schematics = new ArrayList<>();

    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        registerBoomerang();
        registerArrow();
        registerLongSingle();
        registerLaserArtillery();
        generator();
    }

    private static void registerBoomerang() {
        register(2, new JsonModules("tetra:basic_major_module", "boomerang/stave", "basic_stave/","tetra:item/module/boomerang/stave/basic_stave/")
                .addImprovementsValue("tetra:shared/")
                .addMaterialsValue(0,"tetra:wood/","tetra:stone/","tetra:metal/","tetra:gem/","tetra:bone/")
                .addAvailableTexturesValue(0,"metal","shiny","grainy","crude")
                .setTextureX(0,16)
                .addExtractSecondaryAttributes(0, "octa:boomerang_speed", -0.1F)
                .addExtractSecondaryAttributes(0, "octa:boomerang_time", 5F)
                .addVariantsAttributes(0, "octa:boomerang_speed", 1.2F)
                .addVariantsAttributes(0, "octa:boomerang_time", 20F)
                .addVariantsAttributes(0, "generic.attack_damage", 2F)
                .addVariantsTools(0, "pickaxe_dig", -1)
                .addExtractTools(0, "pickaxe_dig", 1F, 0.91F)
                .setVariantsIntegrityValue(0, 6)
                .setVariantsDurabilityValue(0, -19)
                .setExtractDurabilityValue(0, 0.4F)
                .addVariantsEffects(0, "unbreaking", 1)
        );
        register(2, new JsonModules("tetra:basic_major_module", "boomerang/blade", "basic_blade/","tetra:item/module/boomerang/blade/basic_blade/")
                .addImprovementsValue("tetra:shared/")
                .addMaterialsValue(0,"tetra:wood/","tetra:stone/","tetra:metal/","tetra:gem/","tetra:bone/")
                .addAvailableTexturesValue(0,"metal","shiny","grainy","crude")
                .setTextureX(0,0)
                .setRenderLayerValue("higher")
                .addExtractPrimaryAttributes(0, "generic.attack_damage", 0.7F)
                .addExtractSecondaryAttributes(0, "octa:boomerang_speed", -0.04F)
                .addExtractSecondaryAttributes(0, "octa:boomerang_time", 2F)
                .addVariantsTools(0, "axe_dig", -2)
                .addExtractTools(0, "axe_dig", 1F, 0.5F)
                .setVariantsIntegrityValue(0, -2)
                .setExtractDurabilityValue(0, 0.2F)
        );
        register(1, new JsonModules("tetra:basic_module", "boomerang/binding", "binding/","tetra:item/module/boomerang/binding/")
                .addImprovementsValue("tetra:shared/")
                .addMaterialsValue(0,"tetra:metal/","tetra:gem/")
                .addAvailableTexturesValue(0,"metal")
                .setTextureX(0,40)
                .setTextureY(0,1)
                .addExtractSecondaryAttributes(0, "octa:boomerang_speed", -0.02F)
                .addExtractSecondaryAttributes(0, "octa:boomerang_time", 8F)
                .setVariantsIntegrityValue(0, -1)
        );
    }
    private static void registerArrow(){
        register(1, new JsonModules("tetra:basic_major_module", "arrow/head", "basic_head/","tetra:item/module/arrow/head/basic_head/")
                .addImprovementsValue("tetra:shared/")
                .addMaterialsValue(0,"tetra:stone/","tetra:metal/","tetra:gem/")
                .addAvailableTexturesValue(0,"metal")
                .setTextureX(0,0)
                .setTextureY(0,32)
                .setVariantsIntegrityValue(0, -2)
                .addExtractPrimaryAttributes(0, "octa:arrow_damage", 0.2F)
                .addVariantsAttributes(0, "octa:arrow_damage", 1.0F)
        );
        register(1, new JsonModules("tetra:basic_major_module", "arrow/shaft", "basic_shaft/","tetra:item/module/arrow/shaft/basic_shaft/")
                .addImprovementsValue("tetra:shared/")
                .addMaterialsValue(0,"tetra:wood/","tetra:metal/","tetra:bone/")
                .addAvailableTexturesValue(0,"metal")
                .setTextureX(0,16)
                .setTextureY(0,32)
                .setVariantsIntegrityValue(0, 3)
        );
        register(1, new JsonModules("tetra:basic_major_module", "arrow/fletching", "basic_fletching/","tetra:item/module/arrow/fletching/basic_fletching/")
                .addImprovementsValue("tetra:shared/")
                .addMaterialsValue(0,"tetra:fabric/")
                .addAvailableTexturesValue(0,"metal")
                .setTextureX(0,32)
                .setTextureY(0,32)
                .setVariantsIntegrityValue(0, -1)
        );
    }
    private static void registerLongSingle(){
        register(1, new JsonModules("tetra:basic_major_module", "longsingle/head", "heavy_lance/","tetra:item/module/longsingle/head/heavy_lance/")
                .addImprovementsValue("tetra:shared/")
                .addMaterialsValue(0,"tetra:stone/","tetra:metal/")
                .addAvailableTexturesValue(0,"metal")
                .setTextureX(0,0)
                .setTextureY(0,16)
                .setVariantsIntegrityValue(0, 6)
                .addVariantsAttributes(0, "generic.attack_damage", 6F)
                .addVariantsAttributes(0, "generic.attack_speed", -2.4F)
                .addExtractPrimaryAttributes(0, "generic.attack_damage", 1.8F)
                .addExtractPrimaryAttributes(0, "generic.attack_speed", -0.1F)
        );
        register(1, new JsonModules("tetra:basic_major_module", "longsingle/head", "basic_lance/","tetra:item/module/longsingle/head/basic_lance/")
                .addImprovementsValue("tetra:shared/")
                .addMaterialsValue(0,"tetra:stone/","tetra:metal/")
                .addAvailableTexturesValue(0,"metal")
                .setTextureX(0,0)
                .setTextureY(0,16)
                .setVariantsIntegrityValue(0, 6)
                .addVariantsAttributes(0, "generic.attack_damage", 4F)
                .addVariantsAttributes(0, "generic.attack_speed", -2F)
                .addExtractPrimaryAttributes(0, "generic.attack_damage", 1.1F)
                .addExtractPrimaryAttributes(0, "generic.attack_speed", -0.05F)
        );
        register(1, new JsonModules("tetra:basic_major_module", "longsingle/hilt", "basic_hilt/","tetra:item/module/longsingle/hilt/basic_hilt/")
                .addImprovementsValue("tetra:shared/")
                .addMaterialsValue(0,"tetra:wood/","tetra:metal/","tetra:bone/")
                .addAvailableTexturesValue(0,"metal")
                .setTextureX(0,16)
                .setTextureY(0,16)
                .setVariantsIntegrityValue(0, -1)
        );
        register(1, new JsonModules("tetra:basic_module", "longsingle/binding", "binding/","tetra:item/module/longsingle/binding/")
                .addImprovementsValue("tetra:shared/")
                .addMaterialsValue(0,"tetra:gem/")
                .addAvailableTexturesValue(0,"metal")
                .setTextureX(0,40)
                .setTextureY(0,1)
                .setVariantsIntegrityValue(0, -1)
        );
    }
    private static void registerLaserArtillery(){
        register(1, new JsonModules("tetra:basic_major_module", "artillery/pump", "basic_pump/","tetra:item/module/artillery/pump/basic_pump/")
                .addImprovementsValue("tetra:shared/")
                .addMaterialsValue(0,"tetra:pump/","tetra:gem/")
                .setRenderLayerValue("higher")
                .addAvailableTexturesValue(0,"metal")
                .setTextureX(0,0)
                .setTextureY(0,48)
                .setVariantsIntegrityValue(0, -2)
                .addVariantsAttributes(0,"octa:artillery_pump",1.0F)
                .setExtractDurabilityValue(0, -0.1F)
        );
        register(1, new JsonModules("tetra:basic_major_module", "artillery/crystal", "basic_crystal/","tetra:item/module/artillery/crystal/basic_crystal/")
                .addImprovementsValue("tetra:shared/")
                .addMaterialsValue(0,"tetra:gem/")
                .addAvailableTexturesValue(0,"placeholder")
                .setTextureX(0,16)
                .setTextureY(0,48)
                .setVariantsIntegrityValue(0, -2)
                .setExtractDurabilityValue(0, -0.1F)
        );
        register(1, new JsonModules("tetra:basic_major_module", "artillery/body", "basic_body/","tetra:item/module/artillery/body/basic_body/")
                .addImprovementsValue("tetra:shared/")
                .addMaterialsValue(0,"tetra:metal/")
                .addAvailableTexturesValue(0,"metal")
                .setTextureX(0,96)
                .setTextureY(0,48)
                .setVariantsIntegrityValue(0, 8)
                .setVariantsDurabilityValue(0, 240)
                .setExtractDurabilityValue(0, 0.56F)
        );
//        The JSON file of window part is complex, those types shouldn't be generated by JSONHelper!!!
        register(1, new JsonModules("tetra:basic_module", "artillery/window", "basic_window/","tetra:item/module/artillery/window/basic_window/")
                .addMaterialsValue(0,"tetra:gem/")
                .addAvailableTexturesValue(0,"placeholder")
                .setTextureX(0,48)
                .setTextureY(0,48)
                .setVariantsIntegrityValue(0, -1)
                .setExtractDurabilityValue(0, 0.02F)
                .setTweakKeyValue("tetra:artillery/basic_window")
        );
        register(1, new JsonModules("tetra:basic_major_module", "artillery/cavity", "spherical_cavity/","tetra:item/module/artillery/cavity/spherical_cavity/")
                .addImprovementsValue("tetra:shared/")
                .addMaterialsValue(0,"tetra:metal/")
                .addAvailableTexturesValue(0,"metal")
                .setRenderLayerValue("higher")
                .setTextureX(0,32)
                .setTextureY(0,48)
                .setVariantsIntegrityValue(0, -2)
                .addVariantsAttributes(0,"octa:artillery_heating",10.0F)
                .addVariantsAttributes(0,"octa:artillery_cooling", 10.0F)
                .addVariantsAttributes(0,"octa:artillery_temperature", 1000.0F)
                .addVariantsAttributes(0,"octa:artillery_focus", 90F)
                .addExtractPrimaryAttributes(0,"octa:artillery_heating",1.6F)
                .addExtractPrimaryAttributes(0,"octa:artillery_cooling",0.8F)
                .addExtractPrimaryAttributes(0,"octa:artillery_temperature",300.0F)
                .addExtractSecondaryAttributes(0,"octa:artillery_heating",0.8F)
                .addExtractSecondaryAttributes(0,"octa:artillery_cooling",-0.2F)
                .addExtractSecondaryAttributes(0,"octa:artillery_temperature",500.0F)
                .addExtractTertiaryAttributes(0,"octa:artillery_heating",-0.4F)
                .addExtractTertiaryAttributes(0,"octa:artillery_cooling",1.5F)
                .addExtractTertiaryAttributes(0,"octa:artillery_temperature",-100.0F)
                .setExtractDurabilityValue(0, 0.2F)
        );
        register(1, new JsonModules("tetra:basic_major_module", "artillery/cavity", "cuboid_cavity/","tetra:item/module/artillery/cavity/cuboid_cavity/")
                .addImprovementsValue("tetra:shared/")
                .addMaterialsValue(0,"tetra:metal/")
                .addAvailableTexturesValue(0,"metal")
                .setRenderLayerValue("higher")
                .setTextureX(0,64)
                .setTextureY(0,48)
                .setVariantsIntegrityValue(0, -2)
                .addVariantsAttributes(0,"octa:artillery_heating",10.0F)
                .addVariantsAttributes(0,"octa:artillery_cooling", 10.0F)
                .addVariantsAttributes(0,"octa:artillery_temperature", 2000.0F)
                .addVariantsAttributes(0,"octa:artillery_focus", 65F)
                .addExtractPrimaryAttributes(0,"octa:artillery_heating",1.2F)
                .addExtractPrimaryAttributes(0,"octa:artillery_cooling",0.8F)
                .addExtractPrimaryAttributes(0,"octa:artillery_temperature",400.0F)
                .addExtractSecondaryAttributes(0,"octa:artillery_heating",0.6F)
                .addExtractSecondaryAttributes(0,"octa:artillery_cooling",-0.2F)
                .addExtractSecondaryAttributes(0,"octa:artillery_temperature",650.0F)
                .addExtractTertiaryAttributes(0,"octa:artillery_heating",-0.4F)
                .addExtractTertiaryAttributes(0,"octa:artillery_cooling",1.5F)
                .addExtractTertiaryAttributes(0,"octa:artillery_temperature",-100.0F)
                .setExtractDurabilityValue(0, 0.2F)
        );
        register(1, new JsonModules("tetra:basic_major_module", "artillery/cavity", "cylindrical_cavity/","tetra:item/module/artillery/cavity/cylindrical_cavity/")
                .addImprovementsValue("tetra:shared/")
                .addMaterialsValue(0,"tetra:metal/")
                .addAvailableTexturesValue(0,"metal")
                .setRenderLayerValue("higher")
                .setTextureX(0,80)
                .setTextureY(0,48)
                .setVariantsIntegrityValue(0, -2)
                .addVariantsAttributes(0,"octa:artillery_heating",10.0F)
                .addVariantsAttributes(0,"octa:artillery_cooling", 10.0F)
                .addVariantsAttributes(0,"octa:artillery_temperature", 1600.0F)
                .addVariantsAttributes(0,"octa:artillery_focus", 75F)
                .addExtractPrimaryAttributes(0,"octa:artillery_heating",1.4F)
                .addExtractPrimaryAttributes(0,"octa:artillery_cooling",0.8F)
                .addExtractPrimaryAttributes(0,"octa:artillery_temperature",350.0F)
                .addExtractSecondaryAttributes(0,"octa:artillery_heating",0.7F)
                .addExtractSecondaryAttributes(0,"octa:artillery_cooling",-0.2F)
                .addExtractSecondaryAttributes(0,"octa:artillery_temperature",580.0F)
                .addExtractTertiaryAttributes(0,"octa:artillery_heating",-0.4F)
                .addExtractTertiaryAttributes(0,"octa:artillery_cooling",1.5F)
                .addExtractTertiaryAttributes(0,"octa:artillery_temperature",-100.0F)
                .setExtractDurabilityValue(0, 0.2F)
        );
    }

    private static void register(int counterFactor, JsonModules m){
        modules.add(m);
        schematics.add(new JsonSchematics(m, counterFactor));
    }

    private static void generator(){
        for(JsonModules m : modules){
            modules_generate(m);
        }
        for(JsonSchematics s : schematics){
            schematics_generate(s);
        }
        // lang_generate();
    }

    private static void modules_generate(JsonModules m){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File filed = new File("src\\main\\resources\\data\\tetra\\modules\\" + m.getCatalogy() + "\\");
        String[] location = m.getLocation(0).split("/");
        StringBuilder Path = new StringBuilder("src\\main\\resources\\assets\\tetra\\textures\\item\\module");
        for (int i = 2; i < location.length; i++) {
            Path.append("\\").append(location[i]);
        }
        File locationFile = new File(Path.toString());
        if(!locationFile.exists()) {
            locationFile.mkdirs();
            System.out.println(Path + " Path generation succeed!");
        } else {
            System.out.println(Path + " Path generation failed!");
        }
        if(!filed.exists())filed.mkdir();
        File fileNew = new File("src\\main\\resources\\data\\tetra\\modules\\" + m.getCatalogy() + "\\" + m.getFilename(0) + ".json");
        try (FileWriter file = new FileWriter(fileNew)) {
            gson.toJson(m, file);
            System.out.println(m.getFilename(0) + ".json generation succeed!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void schematics_generate(JsonSchematics s){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        File filed = new File("src\\main\\resources\\data\\tetra\\schematics\\" + s.getCatalogy() + "\\");
        if(!filed.exists())filed.mkdir();
        File fileNew = new File("src\\main\\resources\\data\\tetra\\schematics\\" + s.getCatalogy() + "\\" + s.getFilename(0) + ".json");
        
        try (FileWriter file = new FileWriter(fileNew)) {
            gson.toJson(s, file);
            System.out.println(s.getFilename(0) + ".json generation succeed!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void lang_generate(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File fileNew = new File("src\\main\\resources\\assets\\octa_auto\\lang\\zh_cn.json");
        
        try (FileWriter file = new FileWriter(fileNew)) {
            gson.toJson(JsonLang.getLangFile(), file);
            System.out.println("zh_cn.json generation succeed!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}