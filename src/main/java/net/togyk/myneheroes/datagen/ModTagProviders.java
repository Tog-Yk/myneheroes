package net.togyk.myneheroes.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.Powers;
import net.togyk.myneheroes.registry.ModRegistryKeys;
import net.togyk.myneheroes.upgrade.Upgrade;
import net.togyk.myneheroes.upgrade.Upgrades;
import net.togyk.myneheroes.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModTagProviders {
    public static class ModAbilityTagProvider extends FabricTagProvider<Ability> {
        public ModAbilityTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, ModRegistryKeys.ABILITY, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        }
    }

    public static class ModPowerTagProvider extends FabricTagProvider<Power> {
        public ModPowerTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, ModRegistryKeys.POWER, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            getOrCreateTagBuilder(ModTags.Powers.MUTANT)
                    .addTag(ModTags.Powers.RARE_MUTANT)
                    .addTag(ModTags.Powers.COMMON_MUTANT)
                    .addTag(ModTags.Powers.OFTEN_MUTANT)
            ;

            getOrCreateTagBuilder(ModTags.Powers.RARE_MUTANT)

            ;
            getOrCreateTagBuilder(ModTags.Powers.COMMON_MUTANT)

            ;
            getOrCreateTagBuilder(ModTags.Powers.OFTEN_MUTANT)
                    .add(Powers.MUTANT_REGENERATION)
            ;

            getOrCreateTagBuilder(ModTags.Powers.RADIATION_OBTAINABLE)
                    .addTag(ModTags.Powers.RARE_RADIATION_OBTAINABLE)
                    .addTag(ModTags.Powers.COMMON_RADIATION_OBTAINABLE)
                    .addTag(ModTags.Powers.OFTEN_RADIATION_OBTAINABLE)
            ;

            getOrCreateTagBuilder(ModTags.Powers.RARE_RADIATION_OBTAINABLE)
                    .addTag(ModTags.Powers.RARE_MUTANT)
            ;

            getOrCreateTagBuilder(ModTags.Powers.COMMON_RADIATION_OBTAINABLE)
                    .addTag(ModTags.Powers.COMMON_MUTANT)
            ;

            getOrCreateTagBuilder(ModTags.Powers.OFTEN_RADIATION_OBTAINABLE)
                    .addTag(ModTags.Powers.OFTEN_MUTANT)
            ;

            getOrCreateTagBuilder(ModTags.Powers.SPIDER)
                    .add(Powers.SPIDER)
                    .add(Powers.SPIDER_ORGANIC_WEBBING)
            ;
        }
    }

    public static class ModUpgradeTagProvider extends FabricTagProvider<Upgrade> {
        public ModUpgradeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, ModRegistryKeys.UPGRADE, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            getOrCreateTagBuilder(ModTags.Upgrades.KINETIC_ENERGY)
                    .add(Upgrades.KINETIC_ENERGY_STORAGE)
            ;
        }
    }
}