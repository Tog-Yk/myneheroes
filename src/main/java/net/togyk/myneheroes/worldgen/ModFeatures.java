package net.togyk.myneheroes.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.togyk.myneheroes.MyneHeroes;

public class ModFeatures {
    public static void registerModFeatures() {
        MyneHeroes.LOGGER.info("Registering Features for " + MyneHeroes.MOD_ID);
    }


    public static final Feature<DefaultFeatureConfig> METEOR_FEATURE = Registry.register(Registries.FEATURE, Identifier.of(MyneHeroes.MOD_ID, "meteor_feature"), new MeteorFeature(Codec.unit(DefaultFeatureConfig.INSTANCE)));
}
