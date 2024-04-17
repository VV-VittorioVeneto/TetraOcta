package com.vv.vvaddon.TestE;

import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.items.modular.ModularItem;

public class DamagedProcedure {
    public void executemin(ModularItem mitem , int fix , int pro , ItemStack item){
        if(mitem.getDamage(item) > fix){
            mitem.setDamage(item , mitem.getDamage(item)+fix);
        }
    }
}
