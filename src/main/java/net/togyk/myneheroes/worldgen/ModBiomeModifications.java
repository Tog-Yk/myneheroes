package net.togyk.myneheroes.worldgen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;
import net.togyk.myneheroes.MyneHeroes;

public class ModBiomeModifications {
    public static void registerModBiomeModifications(){
        MyneHeroes.LOGGER.info("Registering Biome Modifacations for " + MyneHeroes.MOD_ID);
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.VIBRANIUM_ORE_KEY
        );
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.TITANIUM_ORE_KEY
        );
        //add more placed features here
    }
}
