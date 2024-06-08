package com.vv.octa.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.parse.ANTLRParser.optionValue_return;

@SuppressWarnings("unused")
public class JsonLang {
    private static HashMap<String, String> langFile = new HashMap<String, String>();
    private static HashMap<String, String> material_metal = new HashMap<String, String>();
    private static HashMap<String, String> material_wood = new HashMap<String, String>();
    private static HashMap<String, String> material_gem = new HashMap<String, String>();
    private static HashMap<String, String> material_stone = new HashMap<String, String>();
    private static HashMap<String, String> material_bone = new HashMap<String, String>();
    private static HashMap<String, String> modules_name = new HashMap<String, String>();
    private static HashMap<String, String> modules_material = new HashMap<String, String>();
    private static HashMap<String, String> schematics = new HashMap<String, String>();

    private static void init(){
        modules_init();
        material_bone_init();
        material_gem_init();
        material_metal_init();
        material_stone_init();
        material_wood_init();
        modules_name.forEach((module, module_name)->{
            String con_material = modules_material.get(module);
            if(con_material.contains("metal"))material_metal.forEach((material, material_name)->{variant_generater(module, material, material_name + " " + module_name);});
            if(con_material.contains("wood"))material_wood.forEach((material, material_name)->{variant_generater(module, material, material_name + " " + module_name);});
            if(con_material.contains("stone"))material_stone.forEach((material, material_name)->{variant_generater(module, material, material_name + " " + module_name);});
            if(con_material.contains("gem"))material_gem.forEach((material, material_name)->{variant_generater(module, material, material_name + " " + module_name);});
            if(con_material.contains("bone"))material_bone.forEach((material, material_name)->{variant_generater(module, material, material_name + " " + module_name);});
        });
    }

    private static void variant_generater(String module, String material, String name){
        langFile.put("tetra.variant." + module + "/" + material, name);
    }

    private static void modules_init(){
        modules_init_helper("binding", "绑定结", "tetra:metal/","tetra:gem/");
        modules_init_helper("basic_blade","镖刃","tetra:wood/","tetra:stone/","tetra:metal/","tetra:gem/","tetra:bone/");
        modules_init_helper("basic_stave","镖体","tetra:wood/","tetra:stone/","tetra:metal/","tetra:gem/","tetra:bone/");
    }

    private static void modules_init_helper(String part, String name, String...materials){
        modules_name.put(part, name);
        String con = "";
        for(String material : materials){
            con += material.split("tetra:")[1].split("/")[0];
        }
        modules_material.put(part, con);
    }

    private static void material_metal_init(){
        material_metal.put("iron","铁");
        material_metal.put("gold","金");
        material_metal.put("copper","铜");
        material_metal.put("netherite","下界合金");
    }

    private static void material_wood_init(){
        material_wood.put("oak","橡木");
        material_wood.put("spruce","云杉木");
        material_wood.put("birch","白桦木");
        material_wood.put("jungle","丛林木");
        material_wood.put("acacia","金合欢木");
        material_wood.put("dark_oak","深色橡木");
        material_wood.put("mangrove","红树木");
        material_wood.put("cherry","樱桃木");
        material_wood.put("crimson","绯红木");
        material_wood.put("warped","诡异木");
    }

    private static void material_gem_init(){
        material_gem.put("diamond","钻石");
        material_gem.put("emerald","绿宝石");
        material_gem.put("amethyst","紫水晶");
    }

    private static void material_stone_init(){
        material_stone.put("granite","花岗岩");
        material_stone.put("stone","石头");
        material_stone.put("flint","燧石");
        material_stone.put("blackstone","黑石");
        material_stone.put("obsidian","黑曜石");
        material_stone.put("diorite","闪长岩");
        material_stone.put("andesite","安山岩");
    }

    private static void material_bone_init(){
        material_bone.put("bone","骨");
    }

    public static HashMap<String, String> getLangFile(){
        init();
        return langFile;
    }
}