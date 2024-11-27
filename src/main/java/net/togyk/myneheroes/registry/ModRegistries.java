package net.togyk.myneheroes.registry;


import com.google.common.collect.Maps;
import com.mojang.serialization.Lifecycle;
import net.minecraft.Bootstrap;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Abilities;
import net.togyk.myneheroes.ability.Ability;

import java.util.Map;
import java.util.function.Supplier;


public class ModRegistries extends Registries {
    private static final Map<Identifier, Supplier<?>> DEFAULT_ENTRIES = Maps.newLinkedHashMap();
    public static final DefaultedRegistry<Ability> ABILITY;
    public static final SimpleDefaultedRegistry MYNEHEROES;


    static  {
        MYNEHEROES = new SimpleRegistry(RegistryKey.ofRegistry(RegistryKeys.ROOT), Lifecycle.stable());
        ABILITY = createIntrusive(ModRegistryKeys.ABILITY, "shoot_lazar", (registry) -> {
            return Abilities.SHOOT_LAZAR;
        });
    }
    private static <T> DefaultedRegistry createIntrusive(RegistryKey<? extends Registry<T>> key, String defaultId, Initializer<T> initializer) {
        return (DefaultedRegistry)create(key, (MutableRegistry)(new SimpleDefaultedRegistry(defaultId, key, Lifecycle.stable(), true)), initializer);
    }

    private static <T, R extends MutableRegistry<T>> R create(RegistryKey<? extends Registry<T>> key, R registry, Initializer<T> initializer) {
        Bootstrap.ensureBootstrapped(() -> {
            return "registry " + String.valueOf(key);
        });
        Identifier identifier = key.getValue();
        DEFAULT_ENTRIES.put(identifier, () -> {
            return initializer.run(registry);
        });
        return registry;
    }
    private static void init() {
        DEFAULT_ENTRIES.forEach((id, initializer) -> {
            if (initializer.get() == null) {
                MyneHeroes.LOGGER.error("Unable to bootstrap registry '{}'", id);
            }

        });
    }

    @FunctionalInterface
    private interface Initializer<T> {
        Object run(Registry<T> registry);
    }
}