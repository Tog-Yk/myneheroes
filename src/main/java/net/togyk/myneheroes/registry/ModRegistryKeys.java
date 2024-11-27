package net.togyk.myneheroes.registry;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;

public class ModRegistryKeys extends RegistryKeys {
    public static final RegistryKey<Registry<Ability>> ABILITY = of("ability");

    private static <T> RegistryKey<Registry<T>> of(String id) {
        return RegistryKey.ofRegistry(Identifier.of(MyneHeroes.MOD_ID,id));
    }
}
