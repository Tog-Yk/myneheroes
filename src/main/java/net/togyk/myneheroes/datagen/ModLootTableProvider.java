package net.togyk.myneheroes.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;
import net.togyk.myneheroes.Item.ModItems;
import net.togyk.myneheroes.block.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.VIBRANIUM_BLOCK);
        addDrop(ModBlocks.RAW_VIBRANIUM_BLOCK);
        addDrop(ModBlocks.VIBRANIUM_ORE, oreDrops(ModBlocks.VIBRANIUM_ORE, ModItems.RAW_VIBRANIUM));
        addDrop(ModBlocks.DEEPSLATE_VIBRANIUM_ORE, oreDrops(ModBlocks.DEEPSLATE_VIBRANIUM_ORE, ModItems.RAW_VIBRANIUM));

        addDrop(ModBlocks.TITANIUM_BLOCK);
        addDrop(ModBlocks.RAW_TITANIUM_BLOCK);
        addDrop(ModBlocks.TITANIUM_ORE, oreDrops(ModBlocks.TITANIUM_ORE, ModItems.RAW_TITANIUM));
        addDrop(ModBlocks.DEEPSLATE_TITANIUM_ORE, oreDrops(ModBlocks.DEEPSLATE_TITANIUM_ORE, ModItems.RAW_TITANIUM));

        addDrop(ModBlocks.GOLD_TITANIUM_BLOCK);
        addDrop(ModBlocks.RAW_GOLD_TITANIUM_BLOCK);

        addDrop(ModBlocks.URANIUM_BLOCK);
        addDrop(ModBlocks.RAW_URANIUM_BLOCK);
        addDrop(ModBlocks.URANIUM_ORE, oreDrops(ModBlocks.URANIUM_ORE, ModItems.RAW_URANIUM));
        addDrop(ModBlocks.DEEPSLATE_URANIUM_ORE, oreDrops(ModBlocks.DEEPSLATE_TITANIUM_ORE, ModItems.RAW_URANIUM));

        addDrop(ModBlocks.KRYPTONITE_CLUSTER);

        addDrop(ModBlocks.ARMOR_DYEING_STATION);
        addDrop(ModBlocks.ARMOR_LIGHT_LEVELING_STATION);
        addDrop(ModBlocks.UPGRADE_STATION);

        addDrop(ModBlocks.METEOR_RADAR);
    }
}
