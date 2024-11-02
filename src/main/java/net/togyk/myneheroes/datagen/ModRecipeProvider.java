package net.togyk.myneheroes.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.ModItems;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.block.ModBlocks;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        List<ItemConvertible> VIBRANIUM_SMELTABLES = List.of(ModItems.RAW_VIBRANIUM, ModBlocks.VIBRANIUM_ORE,
                ModBlocks.DEEPSLATE_VIBRANIUM_ORE);

        List<ItemConvertible> TITANIUM_SMELTABLES = List.of(ModItems.RAW_TITANIUM, ModBlocks.TITANIUM_ORE,
                ModBlocks.DEEPSLATE_TITANIUM_ORE);

        List<ItemConvertible> GOLD_TITANIUM_SMELTABLES = List.of(ModItems.RAW_GOLD_TITANIUM);

        offerSmelting(exporter, VIBRANIUM_SMELTABLES, RecipeCategory.MISC, ModItems.VIBRANIUM_INGOT, 0.25f, 200, "vibranium");
        offerBlasting(exporter, VIBRANIUM_SMELTABLES, RecipeCategory.MISC, ModItems.VIBRANIUM_INGOT, 0.25f, 100, "vibranium");

        offerSmelting(exporter, TITANIUM_SMELTABLES, RecipeCategory.MISC, ModItems.TITANIUM_INGOT, 0.25f, 200, "titanium");
        offerBlasting(exporter, TITANIUM_SMELTABLES, RecipeCategory.MISC, ModItems.TITANIUM_INGOT, 0.25f, 100, "titanium");

        offerSmelting(exporter, GOLD_TITANIUM_SMELTABLES, RecipeCategory.MISC, ModItems.GOLD_TITANIUM_INGOT, 0.25f, 200, "gold_titanium");
        offerBlasting(exporter, GOLD_TITANIUM_SMELTABLES, RecipeCategory.MISC, ModItems.GOLD_TITANIUM_INGOT, 0.25f, 100, "gold_titanium");



        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, ModItems.VIBRANIUM_INGOT, RecipeCategory.DECORATIONS, ModBlocks.VIBRANIUM_BLOCK);
        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, ModItems.RAW_VIBRANIUM, RecipeCategory.DECORATIONS, ModBlocks.RAW_VIBRANIUM_BLOCK);

        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, ModItems.TITANIUM_INGOT, RecipeCategory.DECORATIONS, ModBlocks.TITANIUM_BLOCK);
        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, ModItems.RAW_TITANIUM, RecipeCategory.DECORATIONS, ModBlocks.RAW_TITANIUM_BLOCK);

        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, ModItems.GOLD_TITANIUM_INGOT, RecipeCategory.DECORATIONS, ModBlocks.GOLD_TITANIUM_BLOCK);
        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, ModItems.RAW_GOLD_TITANIUM, RecipeCategory.DECORATIONS, ModBlocks.RAW_GOLD_TITANIUM_BLOCK);


        ShapelessRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModItems.RAW_GOLD_TITANIUM, 2)
                .input(ModItems.RAW_TITANIUM)
                .input(Items.RAW_GOLD)
                .criterion(hasItem(ModItems.RAW_TITANIUM), conditionsFromItem(ModItems.RAW_TITANIUM))
                .offerTo(exporter,"gold_titanium_alloying");




        //simple armor recipes
        //vibranium
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModItems.VIBRANIUM_HELMET)
                .pattern("###")
                .pattern("# #")
                .input('#', ModItems.VIBRANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.VIBRANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.VIBRANIUM_INGOT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModItems.VIBRANIUM_CHESTPLATE)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .input('#', ModItems.VIBRANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.VIBRANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.VIBRANIUM_INGOT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModItems.VIBRANIUM_LEGGINGS)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .input('#', ModItems.VIBRANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.VIBRANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.VIBRANIUM_INGOT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModItems.VIBRANIUM_BOOTS)
                .pattern("# #")
                .pattern("# #")
                .input('#', ModItems.VIBRANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.VIBRANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.VIBRANIUM_INGOT))
                .offerTo(exporter);



        //Titanium
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModItems.TITANIUM_HELMET)
                .pattern("###")
                .pattern("# #")
                .input('#', ModItems.TITANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TITANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.TITANIUM_INGOT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModItems.TITANIUM_CHESTPLATE)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .input('#', ModItems.TITANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TITANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.TITANIUM_INGOT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModItems.TITANIUM_LEGGINGS)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .input('#', ModItems.TITANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TITANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.TITANIUM_INGOT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModItems.TITANIUM_BOOTS)
                .pattern("# #")
                .pattern("# #")
                .input('#', ModItems.TITANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TITANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.TITANIUM_INGOT))
                .offerTo(exporter);



        //vibranium
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModItems.GOLD_TITANIUM_HELMET)
                .pattern("###")
                .pattern("# #")
                .input('#', ModItems.GOLD_TITANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.GOLD_TITANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.GOLD_TITANIUM_INGOT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModItems.GOLD_TITANIUM_CHESTPLATE)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .input('#', ModItems.GOLD_TITANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.GOLD_TITANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.GOLD_TITANIUM_INGOT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModItems.GOLD_TITANIUM_LEGGINGS)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .input('#', ModItems.GOLD_TITANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.GOLD_TITANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.GOLD_TITANIUM_INGOT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModItems.GOLD_TITANIUM_BOOTS)
                .pattern("# #")
                .pattern("# #")
                .input('#', ModItems.GOLD_TITANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.GOLD_TITANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.GOLD_TITANIUM_INGOT))
                .offerTo(exporter);




        offerSmithingTrimRecipe(exporter, ModItems.IRON_SUIT_TEMPLATE, Identifier.of(MyneHeroes.MOD_ID, "iron_suit"));
    }}
