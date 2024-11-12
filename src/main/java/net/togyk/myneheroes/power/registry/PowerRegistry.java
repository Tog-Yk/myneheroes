package net.togyk.myneheroes.power.registry;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.SpiderTobyPower;

public class PowerRegistry {
    public static final RegistryKey<Registry<Power>> POWER_KEY = RegistryKey.ofRegistry(Identifier.of(MyneHeroes.MOD_ID, "powers"));

    public static final Power SPIDER = DeferredPowerRegistry.registerPower("name",
            new SpiderTobyPower(3,5));

    public static void register() {
        DeferredPowerRegistry.register();
    }
}