package net.togyk.myneheroes.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.togyk.myneheroes.block.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(ModBlocks.VIBRANIUM_BLOCK)
                .add(ModBlocks.RAW_VIBRANIUM_BLOCK)
                .add(ModBlocks.VIBRANIUM_ORE)
                .add(ModBlocks.DEEPSLATE_VIBRANIUM_ORE);
        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.VIBRANIUM_BLOCK)
                .add(ModBlocks.RAW_VIBRANIUM_BLOCK)
                .add(ModBlocks.VIBRANIUM_ORE)
                .add(ModBlocks.DEEPSLATE_VIBRANIUM_ORE);
    }
}
