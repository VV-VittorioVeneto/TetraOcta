package com.vv.octa.init;
import com.vv.octa.Octa;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class OctaAttributesRegistry {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Octa.MODID);

    public static RegistryObject<Attribute> BOOMERANG_TIME = register("boomerang_time");
    public static RegistryObject<Attribute> BOOMERANG_SPEED = register("boomerang_speed");
    public static RegistryObject<Attribute> ARROW_DAMAGE = register("arrow_damage");
    public static RegistryObject<Attribute> ARTILLERY_PUMP = register("artillery_pump");//泵浦源功率
    public static RegistryObject<Attribute> ARTILLERY_TEMPERATURE = register("artillery_temperature", 65535.0D);//谐振腔最高耐受温度
    public static RegistryObject<Attribute> ARTILLERY_FOCUS = register("artillery_focus");//聚焦效率
//    public static RegistryObject<Attribute> ARTILLERY_SPECTRAL = register("artillery_spectral");//复合光谱功率
    public static RegistryObject<Attribute> ARTILLERY_HEATING = register("artillery_heating");//升温速率
    public static RegistryObject<Attribute> ARTILLERY_COOLING = register("artillery_cooling");//降温速率

    public static void register(IEventBus bus){
        ATTRIBUTES.register(bus);
    }

    //descriptionID, defaultValue, minValue, maxValue
    private static RegistryObject<Attribute> register(String name){
        return ATTRIBUTES.register(name,()-> new RangedAttribute(Octa.MODID + ".attribute." + name + ".name", 0, 0, 2048));
    }
    private static RegistryObject<Attribute> register(String name, double maxValue){
        return ATTRIBUTES.register(name,()-> new RangedAttribute(Octa.MODID + ".attribute." + name + ".name", 0, 0, maxValue));
    }
}
