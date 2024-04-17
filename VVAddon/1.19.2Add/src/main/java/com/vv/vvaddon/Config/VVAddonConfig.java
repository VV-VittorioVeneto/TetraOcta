package com.vv.vvaddon.Config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

public class VVAddonConfig {
        private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        
        public static ForgeConfigSpec spec;

        public static ForgeConfigSpec.ConfigValue<? extends String> Charge_item;
        public static ForgeConfigSpec.ConfigValue<? extends String> Homology_item;
        public static ForgeConfigSpec.DoubleValue Charge_bonus;
        public static ForgeConfigSpec.DoubleValue Sniper_coefficient;
        public static ForgeConfigSpec.DoubleValue Height_coefficient;
        public static ForgeConfigSpec.DoubleValue HitAway_hor_coefficient;
        public static ForgeConfigSpec.DoubleValue HitAway_ver_coefficient;
        public static ForgeConfigSpec.DoubleValue Fullblood_bonus;
        public static ForgeConfigSpec.DoubleValue Lowblood_bonus;
        public static ForgeConfigSpec.DoubleValue Fullblood_threshold;
        public static ForgeConfigSpec.DoubleValue Lowblood_threshold;
        public static ForgeConfigSpec.DoubleValue Combo_bonus;
        public static ForgeConfigSpec.IntValue Evil_damage;
        public static ForgeConfigSpec.IntValue Honor_damage;
        public static ForgeConfigSpec.DoubleValue Homology_speed;
        public static ForgeConfigSpec.IntValue Combo_max;
        public static ForgeConfigSpec.IntValue Homology_maxnum;
        public static ForgeConfigSpec.IntValue Evil_level;
        public static ForgeConfigSpec.IntValue Evil_time;
        public static ForgeConfigSpec.IntValue Honor_level;
        public static ForgeConfigSpec.IntValue Honor_time;
        public static ForgeConfigSpec.BooleanValue Combo_hurt;
        public static ForgeConfigSpec.BooleanValue Honor_switch;
        public static ForgeConfigSpec.BooleanValue Evil_switch;
        public static ForgeConfigSpec.DoubleValue Exp_bonus_1;
        public static ForgeConfigSpec.DoubleValue Exp_bonus_2;
        public static ForgeConfigSpec.DoubleValue Exp_bonus_3;
        public static ForgeConfigSpec.DoubleValue Phantom_persuit_damage;
        public static ForgeConfigSpec.IntValue Phantom_persuit_num;
        public static ForgeConfigSpec.IntValue Phantom_persuit_count;
        public static ForgeConfigSpec.DoubleValue Phantom_rain_damage;
        public static ForgeConfigSpec.IntValue Phantom_rain_num;
        public static ForgeConfigSpec.DoubleValue Phantom_pr_damage;
        public static ForgeConfigSpec.IntValue Phantom_pr_num;
        public static ForgeConfigSpec.IntValue Phantom_pr_count;
        public static ForgeConfigSpec.DoubleValue Rush_damage;

        static {
            builder.push("充能强化");

                Charge_item = builder
                    .comment("充能强化 特性使用的物品, 默认: \"minecraft:bedrock\"")
                    .define("使用的物品", "minecraft:bedrock");

                Charge_bonus = builder
                    .comment("充能强化 特性攻击增益, 0.1代表10%额外伤害, 默认: 0.1")
                    .defineInRange("增益", 0.1 , 0 ,10);

			builder.pop();

			builder.push("远射");

                Sniper_coefficient = builder
                    .comment("远射 特性伤害系数, 1.0代表最终伤害*1, 默认: 1.0")
                    .defineInRange("系数", 1.0 , 0 ,10);

			builder.pop();

			builder.push("高地");

                Height_coefficient = builder
                    .comment("高地 特性伤害系数, 1.0代表最终伤害*1, 默认: 1.0")
                    .defineInRange("系数", 1.0 , 0 ,10);

			builder.pop();

			builder.push("冲击");

                HitAway_hor_coefficient = builder
                    .comment("冲击 特性水平击退系数, 1.0代表最终水平击退力度*1, 默认: 1.0")
                    .defineInRange("水平系数", 1.0 , 0 ,10);

                HitAway_ver_coefficient = builder
                    .comment("冲击 特性垂直击退系数, 1.0代表最终垂直击退力度*1, 默认: 1.0")
                    .defineInRange("垂直系数", 1.0 , 0 ,10);

			builder.pop();

			builder.push("饱和");

                Fullblood_bonus = builder
                    .comment("饱和 特性攻击增益, 0.2代表20%额外伤害, 默认: 0.2")
                    .defineInRange("增益", 0.2 , 0 ,10);

                Fullblood_threshold = builder
                    .comment("饱和 特性血量百分比阈值, 0.8代表80%血量以上, 默认: 0.8")
                    .defineInRange("阈值", 0.8 , 0 ,1);

			builder.pop();

			builder.push("破釜沉舟");

                Lowblood_bonus = builder
                    .comment("破釜沉舟 特性攻击增益, 0.2代表20%额外伤害, 默认: 0.2")
                    .defineInRange("增益", 0.2 , 0 ,10);

                Lowblood_threshold = builder
                    .comment("破釜沉舟 特性血量百分比阈值, 0.3代表代表30%血量以下, 默认: 0.3")
                    .defineInRange("阈值", 0.3 , 0 ,1);

			builder.pop();

			builder.push("连击");

                Combo_bonus = builder
                    .comment("连击 特性攻击增益, 0.03代表每段3%额外伤害, 默认: 0.03")
                    .defineInRange("增益", 0.03 , 0 ,1);

                Combo_max = builder
                    .comment("连击 特性攻击段数, 5代表最高五段, 默认: 5")
                    .defineInRange("段数", 5 ,1,100);

                Combo_hurt = builder
                    .comment("连击 特性是否因为受伤清零, true代表清零, 默认: true")
                    .define("是/否", true);

            builder.pop();
        
            builder.push("同调");

                Homology_item = builder
                    .comment("同调 特性生效的物品, 默认: \"minecraft:barrier\"")
                    .define("生效的物品", "minecraft:barrier");

                Homology_speed = builder
                    .comment("同调 特性修补收益, 0.025代表每个物品每秒修复0.025耐久值, 默认: 0.025")
                    .defineInRange("增益", 0.025 , 0 ,1);

                Homology_maxnum = builder
                    .comment("同调 特性生效物品数量上限, 64代表最高64个物品, 默认: 64")
                    .defineInRange("数量", 64 ,1,100);   

            builder.pop();
        
            builder.push("荣耀");

                Honor_time = builder
                    .comment("荣耀 特性给予村庄英雄的时长, 单位为分钟, 默认: 30")
                    .defineInRange("时长", 30 , 1 ,60);

                Honor_damage = builder
                    .comment("荣耀 特性给予村庄英雄时消耗的耐久, 默认: 100")
                    .defineInRange("消耗耐久", 100 , 0 ,1000);

                Honor_level = builder
                    .comment("荣耀 特性给予村庄英雄的上限等级, 默认: 5")
                    .defineInRange("等级", 5 , 1 ,255);

                Honor_switch = builder
                    .comment("荣耀 特性是否逐级提升至最高等级, 若为否则一次性给予最高等级, 默认: 是")
                    .define("是/否", true);   

            builder.pop();
        
            builder.push("邪念");

                Evil_time = builder
                    .comment("邪念 特性给予不祥之兆的时长, 单位为分钟, 默认: 5")
                    .defineInRange("时长", 5 , 1 ,60);

                Evil_damage = builder
                    .comment("邪念 特性给予不祥之兆时消耗的耐久, 默认: 100")
                    .defineInRange("消耗耐久", 100 , 0 ,1000);

                Evil_level = builder
                    .comment("邪念 特性给予不祥之兆的上限等级, 默认: 5")
                    .defineInRange("等级", 5 , 1 ,255);

                Evil_switch = builder
                    .comment("邪念 特性是否逐级提升至最高等级, 若为否则一次性给予最高等级, 默认: 是")
                    .define("是/否", true);   

            builder.pop();
    
            builder.push("经验收割(只有最高等级生效)");

                Exp_bonus_1 = builder
                    .comment("经验收割一级 特性给予的经验增益, 0.2代表20%额外经验, 默认: 0.2")
                    .defineInRange("一级增益", 0.2 , 0 , 10);

                Exp_bonus_2 = builder
                    .comment("经验收割二级 特性给予的经验增益, 0.5代表50%额外经验, 默认: 0.5")
                    .defineInRange("二级增益", 0.5 , 0 , 10);

                Exp_bonus_3 = builder
                    .comment("经验收割三级 特性给予的经验增益, 1.0代表100%额外经验, 默认: 1.0")
                    .defineInRange("三级增益", 1.0 , 0 , 10);   

            builder.pop();
    
            builder.push("幻影侵袭");

                Phantom_persuit_count = builder
                    .comment("幻影侵袭 特性追加箭矢的轮次数, 5代表5轮箭矢, 默认: 5")
                    .defineInRange("轮次数", 5 , 0 , 9);

                Phantom_persuit_damage = builder
                    .comment("幻影侵袭 特性追加箭矢的基础伤害, 8.0代表箭矢基础伤害为8.0, 默认: 8.0")
                    .defineInRange("基础伤害", 8.0 , 0 , 32767);

                Phantom_persuit_num = builder
                    .comment("幻影侵袭 特性每轮追加箭矢的箭矢数, 1代表箭矢数为1, 默认: 1")
                    .defineInRange("每轮箭矢数", 1 , 0 , 5);   

            builder.pop();
    
            builder.push("幻影狂澜");

                Phantom_pr_count = builder
                    .comment("幻影狂澜 特性追加箭矢的轮次数, 3代表3轮箭矢, 默认: 3")
                    .defineInRange("轮次数", 3 , 0 , 9);

                Phantom_pr_damage = builder
                    .comment("幻影狂澜 特性箭雨箭矢的基础伤害, 4.0代表箭矢基础伤害为4.0, 默认: 4.0")
                    .defineInRange("基础伤害", 4.0 , 0 , 32767);

                Phantom_pr_num = builder
                    .comment("幻影狂澜 特性每轮箭雨的箭矢数, 8代表箭矢数为8, 默认: 8")
                    .defineInRange("每轮箭矢数", 8 , 0 , 30);   

            builder.pop();
    
            builder.push("幻影箭雨");

                Phantom_rain_damage = builder
                    .comment("幻影箭雨 特性箭雨箭矢的基础伤害, 5.0代表箭矢基础伤害为5.0, 默认: 5.0")
                    .defineInRange("基础伤害", 5.0 , 0 , 32767);

                Phantom_rain_num = builder
                    .comment("幻影箭雨 特性每轮箭雨的箭矢数, 12代表箭矢数为12, 默认: 12")
                    .defineInRange("每轮箭矢数", 12 , 0 , 30);   

            builder.pop();
    
            builder.push("居合连斩");

            Rush_damage = builder
                    .comment("居合连斩 特性造成的伤害, 10.0代表基础伤害为10.0, 默认: 10.0")
                    .defineInRange("基础伤害", 10.0 , 0 , 32767);

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