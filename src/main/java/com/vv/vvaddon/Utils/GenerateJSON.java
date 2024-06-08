package com.vv.vvaddon.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vv.vvaddon.MainVVAddon;
import com.vv.vvaddon.Init.VAName;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
 
public class GenerateJSON {

    private static ArrayList<path_file> me_modules_set = new ArrayList<>();
    private static ArrayList<path_file> re_modules_set = new ArrayList<>();
    private static void InitialSet(){
        me_modules_set.add(new path_file("double", "adze"));
        me_modules_set.add(new path_file("double", "basic_axe"));
        me_modules_set.add(new path_file("double", "basic_pickaxe"));
        me_modules_set.add(new path_file("double", "claw"));
        me_modules_set.add(new path_file("double", "hoe"));
        me_modules_set.add(new path_file("double", "sickle"));
        me_modules_set.add(new path_file("single", "basic_shovel"));
        me_modules_set.add(new path_file("single", "spearhead"));
        me_modules_set.add(new path_file("single", "trident"));
        me_modules_set.add(new path_file("sword", "basic_blade"));
        me_modules_set.add(new path_file("sword", "heavy_blade"));
        me_modules_set.add(new path_file("sword", "short_blade"));
        me_modules_set.add(new path_file("sword", "machete"));
        me_modules_set.add(new path_file("sword", "throwing_knife"));
        me_modules_set.add(new path_file("sword", "bastard_blade"));
        me_modules_set.add(new path_file("sword", "rapier_blade"));
        me_modules_set.add(new path_file("sword", "executioner_blade"));
        me_modules_set.add(new path_file("sword", "saber_blade"));

        re_modules_set.add(new path_file("bow", "straight_stave"));
        re_modules_set.add(new path_file("bow", "recurve_stave"));
        re_modules_set.add(new path_file("bow", "flatbow_stave"));
        re_modules_set.add(new path_file("bow", "long_stave"));
    }
    
    public static void main(String[] args) {
        InitialSet();
        Class<VANameMe> me_names = VANameMe.class;
        Class<VANameRe> re_names = VANameRe.class;
        Class<VAName> names = VAName.class;
        Field[] me_fields = me_names.getDeclaredFields();
        Field[] re_fields = re_names.getDeclaredFields();
        Field[] fields = names.getDeclaredFields();
        Modules re_modules = new Modules(false);
        Modules me_modules = new Modules(false);
        for(Field field : fields){
            String f_name = field.getName();
            im_jsongenerate(f_name);
        }
        for(Field field : re_fields){
            String f_name = field.getName();
            re_modules.insert(f_name);
        }
        for(Field field : me_fields){
            String f_name = field.getName();
            me_modules.insert(f_name);
        }
        for(path_file module : re_modules_set){
            mo_jsongenerate(module.path, module.file, re_modules);
        }
        for(path_file module : me_modules_set){
            mo_jsongenerate(module.path, module.file, me_modules);
        }
    }

    private static class path_file{
        private String path;
        private String file;

        path_file(String path, String file){
            this.path = path;
            this.file = file + ".json";
        }
    }

    private static void im_jsongenerate(String feature){
        String file_name = feature + ".json";
        String effect_str = MainVVAddon.MODID + ":" + feature;
        String key_str = MainVVAddon.MODID + "/" + feature;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Improvements> jout = new ArrayList<>();
        Improvements i = new Improvements(key_str,effect_str,1);
        jout.add(i);
        
        File filenew = new File("src\\main\\resources\\data\\tetra\\improvements\\vvaddon\\" + file_name);
        try (FileWriter file = new FileWriter(filenew)) {
            gson.toJson(jout, file);
            System.out.println(file_name + ".json generation succeed!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void mo_jsongenerate(String catalogy, String filename, Modules m){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        File filenew = new File("src\\main\\resources\\data\\tetra\\modules\\" + catalogy + "\\" + filename);
        try (FileWriter file = new FileWriter(filenew)) {
            gson.toJson(m, file);
            System.out.println(filename + ".json generation succeed!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Improvements{
        @SuppressWarnings("unused")
        private String key;
        private Map<String, Integer> effects = new HashMap<>();

        public Improvements(String key, String effects_key, int effects_level){
            this.key = key;
            this.effects.put(effects_key,effects_level);
        }
    }

    private static class Modules{
        @SuppressWarnings("unused")
        private boolean replace;
        private ArrayList<String> improvements = new ArrayList<>();

        public Modules(boolean replace){
            this.replace = replace;
        }

        public void insert(String new_features){
            this.improvements.add("tetra:" + MainVVAddon.MODID + "/" + new_features);
        }
    }
}