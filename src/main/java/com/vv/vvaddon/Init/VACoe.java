package com.vv.vvaddon.Init;

import com.vv.vvaddon.Config.VVAddonConfig;

public class VACoe {
    public static double charge_bonus;
    public static double sniper_coe;
    public static double height_coe;
    public static double hitaway_hor_coe;
    public static double hitaway_ver_coe;
    public static double fullblood_bonus;
    public static double lowblood_bonus;
    public static double fullblood_threshold;
    public static double lowblood_threshold;
    public static double combo_bonus;
    public static double homology_speed;
    public static double exp_bonus_1;
    public static double exp_bonus_2;
    public static double exp_bonus_3;
    public static double phantom_pr_damage;
    public static double phantom_persuit_damage;
    public static double phantom_rain_damage;
    public static double rush_damage;
    
    public static int evil_damage;
    public static int honor_damage;
    public static int combo_max;
    public static int homology_maxnum;
    public static int evil_level;
    public static int evil_time;
    public static int honor_level;
    public static int honor_time;
    public static int phantom_persuit_num;
    public static int phantom_persuit_count;
    public static int phantom_rain_num;
    public static int phantom_rain_count;
    public static int phantom_pr_num;
    public static int phantom_pr_count;

    public VACoe(){
        charge_bonus = VVAddonConfig.Charge_bonus.get();
        sniper_coe = VVAddonConfig.Sniper_coefficient.get();
        height_coe = VVAddonConfig.Height_coefficient.get();
        hitaway_hor_coe = VVAddonConfig.HitAway_hor_coefficient.get();
        hitaway_ver_coe = VVAddonConfig.HitAway_ver_coefficient.get();
        fullblood_bonus = VVAddonConfig.Fullblood_bonus.get();
        lowblood_bonus = VVAddonConfig.Lowblood_bonus.get();
        fullblood_threshold = VVAddonConfig.Fullblood_threshold.get();
        lowblood_threshold = VVAddonConfig.Lowblood_threshold.get();
        combo_bonus = VVAddonConfig.Combo_bonus.get();
        homology_speed = VVAddonConfig.Homology_speed.get();
        exp_bonus_1 = VVAddonConfig.Exp_bonus_1.get();
        exp_bonus_2 = VVAddonConfig.Exp_bonus_2.get();
        exp_bonus_3 = VVAddonConfig.Exp_bonus_3.get();
        phantom_persuit_damage = VVAddonConfig.Phantom_persuit_damage.get();
        phantom_pr_damage = VVAddonConfig.Phantom_pr_damage.get();
        phantom_rain_damage = VVAddonConfig.Phantom_rain_damage.get();
        rush_damage = VVAddonConfig.Rush_damage.get();
        
        evil_damage = VVAddonConfig.Evil_damage.get();
        honor_damage = VVAddonConfig.Honor_damage.get();
        combo_max = VVAddonConfig.Combo_max.get();
        homology_maxnum = VVAddonConfig.Homology_maxnum.get();
        evil_level = VVAddonConfig.Evil_level.get();
        evil_time = VVAddonConfig.Evil_time.get();
        honor_level = VVAddonConfig.Honor_level.get();
        honor_time = VVAddonConfig.Honor_time.get();
        phantom_persuit_num = VVAddonConfig.Phantom_persuit_num.get();
        phantom_persuit_count = VVAddonConfig.Phantom_persuit_count.get();
        phantom_rain_num = VVAddonConfig.Phantom_rain_num.get();
        phantom_pr_num = VVAddonConfig.Phantom_pr_num.get();
        phantom_pr_count = VVAddonConfig.Phantom_pr_count.get();    
    } 
}
