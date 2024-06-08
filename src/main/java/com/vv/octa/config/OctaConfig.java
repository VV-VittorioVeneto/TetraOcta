package com.vv.octa.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

public class OctaConfig {
        private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        
        public static ForgeConfigSpec spec;

        public static ForgeConfigSpec.BooleanValue Debug_Mode;

        static {
            builder.push("Debug模式");
            
            Debug_Mode = builder
                .comment("仅开发者选项, 默认: 是")
                .define("Debug模式", true);   

            builder.pop();

            spec = builder.build();
        }
    
    public static void setup() {
            final CommentedFileConfig configData = CommentedFileConfig.builder(FMLPaths.CONFIGDIR.get().resolve("octa.toml"))
                .sync()
                .autosave()
                .preserveInsertionOrder()
                .writingMode(WritingMode.REPLACE)
                .build();
            configData.load();
            spec.setConfig(configData);

        }
}