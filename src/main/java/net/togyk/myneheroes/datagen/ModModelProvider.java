package net.togyk.myneheroes.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.ArmorItem;
import net.togyk.myneheroes.Item.ModItems;
import net.togyk.myneheroes.block.ModBlocks;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.VIBRANIUM_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.RAW_VIBRANIUM_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.VIBRANIUM_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.DEEPSLATE_VIBRANIUM_ORE);

        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.TITANIUM_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.RAW_TITANIUM_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.TITANIUM_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.DEEPSLATE_TITANIUM_ORE);

        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.GOLD_TITANIUM_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.RAW_GOLD_TITANIUM_BLOCK);

        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.URANIUM_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.RAW_URANIUM_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.URANIUM_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.DEEPSLATE_URANIUM_ORE);

        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.KRYPTONITE_BlOCK);
        blockStateModelGenerator.registerAmethyst(ModBlocks.KRYPTONITE_CLUSTER);

        blockStateModelGenerator.registerSimpleState(ModBlocks.ARMOR_DYEING_STATION);
        blockStateModelGenerator.registerSimpleState(ModBlocks.ARMOR_LIGHT_LEVELING_STATION);
        blockStateModelGenerator.registerSimpleState(ModBlocks.UPGRADE_STATION);
        blockStateModelGenerator.registerSimpleState(ModBlocks.ARMOR_FABRICATOR);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.VIBRANIUM_INGOT, Models.GENERATED);
        itemModelGenerator.register(ModItems.RAW_VIBRANIUM, Models.GENERATED);

        itemModelGenerator.register(ModItems.TITANIUM_INGOT, Models.GENERATED);
        itemModelGenerator.register(ModItems.RAW_TITANIUM, Models.GENERATED);

        itemModelGenerator.register(ModItems.GOLD_TITANIUM_INGOT, Models.GENERATED);
        itemModelGenerator.register(ModItems.RAW_GOLD_TITANIUM, Models.GENERATED);

        itemModelGenerator.register(ModItems.URANIUM_INGOT, Models.GENERATED);
        itemModelGenerator.register(ModItems.RAW_URANIUM, Models.GENERATED);

        itemModelGenerator.register(ModItems.IRON_SUIT_TEMPLATE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SPEEDSTER_SUIT_TEMPLATE, Models.GENERATED);

        itemModelGenerator.register(ModItems.ARC_REACTOR, Models.GENERATED);

        itemModelGenerator.register(ModItems.CIRCUIT_BOARD, Models.GENERATED);

        itemModelGenerator.register(ModItems.WEB_FLUID, Models.GENERATED);

        itemModelGenerator.register(ModItems.BOTTLE_OF_SPIDER_VENOM, Models.GENERATED);
        itemModelGenerator.register(ModItems.BOTTLE_OF_RADIOACTIVE_SPIDER_VENOM, Models.GENERATED);

        itemModelGenerator.register(ModItems.LASER_UPGRADE, Models.GENERATED);
        itemModelGenerator.register(ModItems.FlY_UPGRADE, Models.GENERATED);
        itemModelGenerator.register(ModItems.KINETIC_ENERGY_STORAGE_UPGRADE, Models.GENERATED);
        itemModelGenerator.register(ModItems.TAKE_OFF_SUIT_UPGRADE, Models.GENERATED);

        itemModelGenerator.register(ModItems.WEB_SHOOTER_UPGRADE, Models.GENERATED);

        itemModelGenerator.registerArmor(((ArmorItem) ModItems.VIBRANIUM_HELMET));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.VIBRANIUM_CHESTPLATE));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.VIBRANIUM_LEGGINGS));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.VIBRANIUM_BOOTS));


        itemModelGenerator.registerArmor(((ArmorItem) ModItems.TITANIUM_HELMET));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.TITANIUM_CHESTPLATE));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.TITANIUM_LEGGINGS));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.TITANIUM_BOOTS));


        itemModelGenerator.registerArmor(((ArmorItem) ModItems.GOLD_TITANIUM_HELMET));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.GOLD_TITANIUM_CHESTPLATE));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.GOLD_TITANIUM_LEGGINGS));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.GOLD_TITANIUM_BOOTS));
    }
}