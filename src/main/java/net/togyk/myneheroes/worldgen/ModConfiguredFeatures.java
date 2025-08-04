package net.togyk.myneheroes.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.*;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.block.ModBlocks;

import java.util.List;

public class ModConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> VIBRANIUM_ORE_KEY = registerKey("vibranium_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TITANIUM_ORE_KEY = registerKey("titanium_ore");

    public static final RegistryKey<ConfiguredFeature<?, ?>> METEOR_KEY = registerKey("meteor");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {

        RegistryEntryLookup<PlacedFeature> registryLookup = context.getRegistryLookup(RegistryKeys.PLACED_FEATURE);

        // all the blocks that will be replaced
        RuleTest stoneOreReplaceables = new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateOreReplaceables = new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        //lists of the blocks that we are going to place
        List<OreFeatureConfig.Target> vibraniumTargets = List.of(
                OreFeatureConfig.createTarget(stoneOreReplaceables, ModBlocks.VIBRANIUM_ORE.getDefaultState()),
                OreFeatureConfig.createTarget(deepslateOreReplaceables, ModBlocks.DEEPSLATE_VIBRANIUM_ORE.getDefaultState()));
        List<OreFeatureConfig.Target> titaniumTargets = List.of(
                OreFeatureConfig.createTarget(stoneOreReplaceables, ModBlocks.TITANIUM_ORE.getDefaultState()),
                OreFeatureConfig.createTarget(deepslateOreReplaceables, ModBlocks.DEEPSLATE_TITANIUM_ORE.getDefaultState()));


        // registering every ore's generation
        register(context, VIBRANIUM_ORE_KEY, Feature.ORE, new OreFeatureConfig(vibraniumTargets, 4));
        register(context, TITANIUM_ORE_KEY, Feature.ORE, new OreFeatureConfig(titaniumTargets, 9));

        register(context, METEOR_KEY, ModFeatures.METEOR_FEATURE, new DefaultFeatureConfig());
    }

    private static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(MyneHeroes.MOD_ID, name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
                                                                                   RegistryKey<ConfiguredFeature<?, ?>> key,
                                                                                   F feature,
                                                                                   FC featureConfig) {
        context.register(key, new ConfiguredFeature<>(feature, featureConfig));
    }
}