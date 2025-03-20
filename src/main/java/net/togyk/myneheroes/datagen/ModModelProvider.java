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

        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ARMOR_DYEING_STATION);

        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ARMOR_LIGHT_LEVELING_STATION);

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.VIBRANIUM_INGOT, Models.GENERATED);
        itemModelGenerator.register(ModItems.RAW_VIBRANIUM, Models.GENERATED);

        itemModelGenerator.register(ModItems.TITANIUM_INGOT, Models.GENERATED);
        itemModelGenerator.register(ModItems.RAW_TITANIUM, Models.GENERATED);

        itemModelGenerator.register(ModItems.GOLD_TITANIUM_INGOT, Models.GENERATED);
        itemModelGenerator.register(ModItems.RAW_GOLD_TITANIUM, Models.GENERATED);

        itemModelGenerator.register(ModItems.IRON_SUIT_TEMPLATE, Models.GENERATED);

        itemModelGenerator.register(ModItems.ARC_REACTOR, Models.GENERATED);

        itemModelGenerator.register(ModItems.ABILITY_HOLDING, Models.GENERATED);
        itemModelGenerator.register(ModItems.LAZAR_HOLDING, Models.GENERATED);

        itemModelGenerator.register(ModItems.HUD_UPGRADE, Models.GENERATED);
        itemModelGenerator.register(ModItems.LAZAR_UPGRADE, Models.GENERATED);

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