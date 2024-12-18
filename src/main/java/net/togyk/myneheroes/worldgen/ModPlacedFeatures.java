package net.togyk.myneheroes.worldgen;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.*;
import net.togyk.myneheroes.MyneHeroes;

import java.util.List;

public class ModPlacedFeatures {
    public static final RegistryKey<PlacedFeature> VIBRANIUM_ORE_KEY = registerKey("vibranium_ore_placed");
    public static final RegistryKey<PlacedFeature> TITANIUM_ORE_KEY = registerKey("titanium_ore_placed");

    public static void bootstrap(Registerable<PlacedFeature> context) {
        RegistryEntryLookup<ConfiguredFeature<?, ?>> registryLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        register(context, VIBRANIUM_ORE_KEY, registryLookup.getOrThrow(ModConfiguredFeatures.VIBRANIUM_ORE_KEY),
                modifiersCount(6,
                        //at what y value does it generate
                        HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(24), YOffset.fixed(24))));

        register(context, TITANIUM_ORE_KEY, registryLookup.getOrThrow(ModConfiguredFeatures.TITANIUM_ORE_KEY),
                modifiersCount(12,
                        //at what y value does it generate
                        HeightRangePlacementModifier.uniform(YOffset.aboveBottom(40), YOffset.fixed(64))));
    }

    private static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MyneHeroes.MOD_ID, name));
    }

    private static void register(Registerable<PlacedFeature> context,
                                 RegistryKey<PlacedFeature> key,
                                 RegistryEntry<ConfiguredFeature<?, ?>> config,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(config, List.copyOf(modifiers)));
    }


    public static List<PlacementModifier> modifiers(PlacementModifier countModifier, PlacementModifier heightModifier) {
        return List.of(countModifier, SquarePlacementModifier.of(), heightModifier, BiomePlacementModifier.of());
    }

    public static List<PlacementModifier> modifiersCount(int count, PlacementModifier heightModifier) {
        return modifiers(CountPlacementModifier.of(count), heightModifier);
    }

    public static List<PlacementModifier> modifiersRarity(int chance, PlacementModifier heightModifier) {
        return modifiers(RarityFilterPlacementModifier.of(chance), heightModifier);
    }
}