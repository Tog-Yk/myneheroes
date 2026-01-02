package net.togyk.myneheroes.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;
import net.togyk.myneheroes.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModEntityTagProvider extends FabricTagProvider.EntityTypeTagProvider {
    public ModEntityTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ModTags.Entity.SMALL_MONSTERS)
                .add(EntityType.ZOMBIE)
                .add(EntityType.CREEPER)
                .add(EntityType.SKELETON)
                .add(EntityType.SPIDER)
                .add(EntityType.CAVE_SPIDER)
                .add(EntityType.DROWNED)
                .add(EntityType.MAGMA_CUBE)
                .add(EntityType.ENDERMITE)
                .add(EntityType.PIGLIN_BRUTE)
                .add(EntityType.PHANTOM)
        ;
        getOrCreateTagBuilder(ModTags.Entity.MEDIUM_MONSTERS)
                .add(EntityType.PIGLIN_BRUTE)
                .add(EntityType.ELDER_GUARDIAN)
                .add(EntityType.HOGLIN)
        ;
        getOrCreateTagBuilder(ModTags.Entity.LARGE_MONSTERS)
                .add(EntityType.ENDER_DRAGON)
                .add(EntityType.WARDEN)
                .add(EntityType.WITHER)
        ;

        getOrCreateTagBuilder(ModTags.Entity.FRIENDLY)
                .add(EntityType.VILLAGER)
        ;
        getOrCreateTagBuilder(ModTags.Entity.PETS)
                .add(EntityType.WOLF)
                .add(EntityType.CAT)
                .add(EntityType.PARROT)
        ;
    }
}
