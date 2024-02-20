package com.vv.vvaddon;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

public class VVAddonConfigEn {
        private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        
        public static ForgeConfigSpec spec;

        public static ForgeConfigSpec.ConfigValue<? extends String> Charge_itemToUse;
        public static ForgeConfigSpec.DoubleValue Charge_bonus;
        public static ForgeConfigSpec.DoubleValue Sniper_coefficient_1;
        public static ForgeConfigSpec.DoubleValue Height_coefficient_2;
        public static ForgeConfigSpec.DoubleValue HitAway_hor_coefficient;
        public static ForgeConfigSpec.DoubleValue HitAway_ver_coefficient_2;
        public static ForgeConfigSpec.DoubleValue Fullblood_bonus;
        public static ForgeConfigSpec.DoubleValue Lowblood_bonus_2;
        public static ForgeConfigSpec.DoubleValue Fullblood_threshold;
        public static ForgeConfigSpec.DoubleValue Lowblood_threshold_2;
        public static ForgeConfigSpec.DoubleValue Combo_bonus;
        public static ForgeConfigSpec.IntValue Combo_max;
        public static ForgeConfigSpec.BooleanValue Combo_hurt;

        static {
            builder.push("Charge");

            	Charge_itemToUse = builder
                    .comment("The item Charge Feature consumes, Default: \"minecraft:bedrock\"")
                    .define("item to consume: ", "minecraft:bedrock");

            	Charge_bonus = builder
                    .comment("Charge Feature damage bonus, 0.1 represents 10% bonus damage, Default: 0.1")
                    .defineInRange("Bonus: ", 0.1 , 0 ,10);
        
            builder.pop();

            builder.push("Sniper");

            	Sniper_coefficient_1 = builder
                    .comment("Sniper Feature damage coefficient, 1.0 represents final damage * 1, Default: 1.0")
                    .defineInRange("Coefficient: ", 1.0 , 0 ,10);
			builder.pop();

			builder.push("Height");
            	Height_coefficient_2 = builder
                    .comment("Height Feature damage coefficient, 1.0 represents final damage * 1, Default: 1.0")
                    .defineInRange("Coefficient: ", 1.0 , 0 ,10);
					
			builder.pop();

			builder.push("HitAway");

            	HitAway_hor_coefficient = builder
                    .comment("HitAway Feature horizontal repulsion coefficient, 1.0 represents final horizontal repulsion distance * 1, Default: 1.0")
                    .defineInRange("Coefficient: ", 1.0 , 0 ,10);

			builder.pop();

			builder.push("HitAway2");

            	HitAway_ver_coefficient_2 = builder
                    .comment("HitAway Feature vertical repulsion coefficient, 1.0 represents final vertical repulsion distance * 1, Default: 1.0")
                    .defineInRange("Coefficient: ", 1.0 , 0 ,10);

            	Lowblood_bonus_2 = builder
                    .comment("Lowblood Feature damage bonus, 0.2 represents 20% bonus damage, Default: 0.2")
                    .defineInRange("Bonus: ", 0.2 , 0 ,10);

            	Lowblood_threshold_2 = builder
                    .comment("Lowblood Feature threshold of health percentage, 0.3 represents below 30% of max health, Default: 0.3")
                    .defineInRange("Threshold: ", 0.3 , 0 ,1);

            	Fullblood_bonus = builder
                    .comment("Fullblood Feature damage bonus, 0.2 represents 20% bonus damage, Default: 0.2")
                    .defineInRange("Bonus: ", 0.2 , 0 ,10);

            	Fullblood_threshold = builder
                    .comment("Fullblood Feature threshold of health percentage, 0.8 represents above 80% of max health, Default: 0.8")
                    .defineInRange("Threshold: ", 0.8 , 0 ,1);

            Combo_bonus = builder
                    .comment("Combo Feature damage bonus, 0.03 represents 3% bonus damage per level, Default: 0.03")
                    .defineInRange("Bonus: ", 0.03 , 0 ,1);

            Combo_max = builder
                    .comment("Combo Feature combo level, 5 represents 5 is the max level, Default: 5")
                    .defineInRange("Level: ", 5 ,1,100);

            Combo_hurt = builder
                    .comment("If Combo Feature level clear when hurted, true represents clear, Default: true")
                    .define("T/F: ", true);
        
            builder.pop();


            spec = builder.build();
        }
    
    public static void setup() {
                final CommentedFileConfig configData = CommentedFileConfig.builder(FMLPaths.CONFIGDIR.get().resolve("vvaddon.toml"))
                        .sync()
                        .autosave()
                        .preserveInsertionOrder()
                        .writingMode(WritingMode.REPLACE)
                        .build();

                configData.load();
                spec.setConfig(configData);

        }
}