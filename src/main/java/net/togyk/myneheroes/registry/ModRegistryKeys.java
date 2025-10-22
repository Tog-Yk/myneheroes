package net.togyk.myneheroes.registry;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.upgrade.Upgrade;

public class ModRegistryKeys {
    public static final RegistryKey<Registry<Ability>> ABILITY = of(Identifier.of(MyneHeroes.MOD_ID, "ability"));
    public static final RegistryKey<Registry<Power>> POWER = of(Identifier.of(MyneHeroes.MOD_ID, "power"));
    public static final RegistryKey<Registry<Upgrade>> UPGRADE = of(Identifier.of(MyneHeroes.MOD_ID, "upgrade"));

    private static <T> RegistryKey<Registry<T>> of(Identifier id) {
        return RegistryKey.ofRegistry(id);
    }

    public static String getTagPath(RegistryKey<? extends Registry<?>> registryRef) {
        return "tags/" + registryRef.getValue().getPath();
    }
}
