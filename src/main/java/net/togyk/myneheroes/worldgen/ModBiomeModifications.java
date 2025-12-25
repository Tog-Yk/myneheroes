package net.togyk.myneheroes.worldgen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.togyk.myneheroes.MyneHeroes;

public class ModBiomeModifications {
    public static final RegistryKey<PlacedFeature> VIBRANIUM_ORE_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MyneHeroes.MOD_ID, "vibranium_ore_placed"));
    public static final RegistryKey<PlacedFeature> TITANIUM_ORE_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MyneHeroes.MOD_ID, "titanium_ore_placed"));
    public static final RegistryKey<PlacedFeature> URANIUM_ORE_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MyneHeroes.MOD_ID, "uranium_ore_placed"));

    public static final RegistryKey<PlacedFeature> METEOR_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MyneHeroes.MOD_ID, "meteor_placed"));
    public static final RegistryKey<PlacedFeature> MJOLNIR_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MyneHeroes.MOD_ID, "mjolnir_placed"));

    public static void registerModBiomeModifications(){
        MyneHeroes.LOGGER.info("Registering Biome Modifacations for " + MyneHeroes.MOD_ID);
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                VIBRANIUM_ORE_KEY
        );
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                TITANIUM_ORE_KEY
        );
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                URANIUM_ORE_KEY
        );

        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_DECORATION,
                METEOR_KEY
        );
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_DECORATION,
                MJOLNIR_KEY
        );
    }
}
