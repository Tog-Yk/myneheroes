package net.togyk.myneheroes.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.togyk.myneheroes.block.ModBlocks;
import net.togyk.myneheroes.util.ModTags;

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
                .add(ModBlocks.DEEPSLATE_VIBRANIUM_ORE)

                .add(ModBlocks.TITANIUM_BLOCK)
                .add(ModBlocks.RAW_TITANIUM_BLOCK)
                .add(ModBlocks.TITANIUM_ORE)
                .add(ModBlocks.DEEPSLATE_TITANIUM_ORE)

                .add(ModBlocks.GOLD_TITANIUM_BLOCK)
                .add(ModBlocks.RAW_GOLD_TITANIUM_BLOCK)

                .add(ModBlocks.KRYPTONITE_BlOCK)
                .add(ModBlocks.KRYPTONITE_CLUSTER)

                .add(ModBlocks.METEOR_RADAR)
        ;

        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
                .add(ModBlocks.ARMOR_DYEING_STATION)
                .add(ModBlocks.ARMOR_LIGHT_LEVELING_STATION)
                .add(ModBlocks.UPGRADE_STATION)
        ;

        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.VIBRANIUM_BLOCK)
                .add(ModBlocks.RAW_VIBRANIUM_BLOCK)
                .add(ModBlocks.VIBRANIUM_ORE)
                .add(ModBlocks.DEEPSLATE_VIBRANIUM_ORE)

                .add(ModBlocks.TITANIUM_BLOCK)
                .add(ModBlocks.RAW_TITANIUM_BLOCK)
                .add(ModBlocks.TITANIUM_ORE)
                .add(ModBlocks.DEEPSLATE_TITANIUM_ORE)

                .add(ModBlocks.GOLD_TITANIUM_BLOCK)
                .add(ModBlocks.RAW_GOLD_TITANIUM_BLOCK)
        ;

        getOrCreateTagBuilder(BlockTags.BEACON_BASE_BLOCKS)
                .add(ModBlocks.VIBRANIUM_BLOCK)
                .add(ModBlocks.TITANIUM_BLOCK)
                .add(ModBlocks.GOLD_TITANIUM_BLOCK)
        ;

        getOrCreateTagBuilder(ModTags.Blocks.KRYPTONITE_METEOR_CORE_BLOCKS)
                .add(Blocks.COBBLED_DEEPSLATE)
                .add(ModBlocks.KRYPTONITE_BlOCK)
        ;

        getOrCreateTagBuilder(ModTags.Blocks.KRYPTONITE_METEOR_CRUST_BLOCKS)
                .add(Blocks.MAGMA_BLOCK)
                .add(Blocks.DEEPSLATE)
                .add(Blocks.BLACKSTONE)
                .add(Blocks.GILDED_BLACKSTONE)
        ;

        getOrCreateTagBuilder(ModTags.Blocks.METEOR_CORE_BLOCKS)
                .add(Blocks.COBBLED_DEEPSLATE)
                .add(Blocks.GILDED_BLACKSTONE)
        ;

        getOrCreateTagBuilder(ModTags.Blocks.METEOR_CRUST_BLOCKS)
                .add(Blocks.MAGMA_BLOCK)
                .add(Blocks.DEEPSLATE)
                .add(Blocks.BLACKSTONE)
        ;

        getOrCreateTagBuilder(ModTags.Blocks.SCULK_METEOR_CORE_BLOCKS)
                .add(Blocks.COBBLED_DEEPSLATE)
                .add(Blocks.SCULK_CATALYST)
        ;

        getOrCreateTagBuilder(ModTags.Blocks.SCULK_METEOR_CRUST_BLOCKS)
                .add(Blocks.SCULK)
                .add(Blocks.BONE_BLOCK)
                .add(Blocks.DEEPSLATE)
                .add(Blocks.BLACKSTONE)
        ;

        getOrCreateTagBuilder(ModTags.Blocks.VIBRANIUM_METEOR_CORE_BLOCKS)
                .add(Blocks.COBBLED_DEEPSLATE)
                .add(ModBlocks.DEEPSLATE_VIBRANIUM_ORE)
        ;

        getOrCreateTagBuilder(ModTags.Blocks.VIBRANIUM_METEOR_CRUST_BLOCKS)
                .add(Blocks.MAGMA_BLOCK)
                .add(Blocks.DEEPSLATE)
                .add(Blocks.BLACKSTONE)
        ;
    }
}
