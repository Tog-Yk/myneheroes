package net.togyk.myneheroes.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.registry.ModRegistryKeys;
import net.togyk.myneheroes.upgrade.Upgrade;

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
        }
    }

    public static class ModUpgradeTagProvider extends FabricTagProvider<Upgrade> {
        public ModUpgradeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, ModRegistryKeys.UPGRADE, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        }
    }
}