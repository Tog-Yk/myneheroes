package net.togyk.myneheroes.power;

import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;

public class ModPowers {
    public static final RegistryKey<Registry<Power>> POWER_REGISTRY_KEY =
            RegistryKey.ofRegistry(Identifier.of(MyneHeroes.MOD_ID, "powers"));
    public static final Registry<Power> POWER =
            Registries.registerSimple()

    public static final Power SPIDERPOWER = registerPower("spider_power",
            new SpiderPower(3,5)
    );
    public static final Power SPIDERTOBYPOWER = registerPower("spider_toby_power",
            new SpiderTobyPower(3,5));

    public static Power registerPower(String name, Power power) {
        return Registry.register(POWER,Identifier.of(MyneHeroes.MOD_ID,name), power);
    }
}
