package net.togyk.myneheroes.component;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;

import java.util.List;
import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final ComponentType<List<Integer>> COLORS =
            register("coordinates", builder -> builder.codec(Codec.list(Codec.INT)));


    private static <T>ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(MyneHeroes.MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void registerDataComponentTypes() {
        MyneHeroes.LOGGER.info("Registering Data Component Types for " + MyneHeroes.MOD_ID);
    }
}