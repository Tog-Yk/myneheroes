package net.togyk.myneheroes.component;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;

import java.util.List;
import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final ComponentType<List<Integer>> COLORS =
            register("colors", builder -> builder.codec(Codec.list(Codec.INT)));

    public static final ComponentType<List<Integer>> LIGHT_LEVEL =
            register("light_level", builder -> builder.codec(Codec.list(Codec.INT)));

    public static final ComponentType<NbtCompound> ABILITIES =
            register("abilities", builder -> builder.codec(NbtCompound.CODEC));


    private static <T>ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(MyneHeroes.MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void registerDataComponentTypes() {
        MyneHeroes.LOGGER.info("Registering Data Component Types for " + MyneHeroes.MOD_ID);
    }
}