package net.togyk.myneheroes.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.data.server.recipe.StonecuttingRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.ModItems;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.block.ModBlocks;
import net.togyk.myneheroes.util.ModTags;

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



        offerReversibleCompactingRecipes(exporter, RecipeCategory.MISC, ModItems.VIBRANIUM_INGOT, RecipeCategory.DECORATIONS, ModBlocks.VIBRANIUM_BLOCK);
        offerReversibleCompactingRecipes(exporter, RecipeCategory.MISC, ModItems.RAW_VIBRANIUM, RecipeCategory.DECORATIONS, ModBlocks.RAW_VIBRANIUM_BLOCK);

        offerReversibleCompactingRecipes(exporter, RecipeCategory.MISC, ModItems.TITANIUM_INGOT, RecipeCategory.DECORATIONS, ModBlocks.TITANIUM_BLOCK);
        offerReversibleCompactingRecipes(exporter, RecipeCategory.MISC, ModItems.RAW_TITANIUM, RecipeCategory.DECORATIONS, ModBlocks.RAW_TITANIUM_BLOCK);

        offerReversibleCompactingRecipes(exporter, RecipeCategory.MISC, ModItems.GOLD_TITANIUM_INGOT, RecipeCategory.DECORATIONS, ModBlocks.GOLD_TITANIUM_BLOCK);
        offerReversibleCompactingRecipes(exporter, RecipeCategory.MISC, ModItems.RAW_GOLD_TITANIUM, RecipeCategory.DECORATIONS, ModBlocks.RAW_GOLD_TITANIUM_BLOCK);


        ShapelessRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModItems.RAW_GOLD_TITANIUM, 2)
                .input(ModItems.RAW_TITANIUM)
                .input(Items.RAW_GOLD)
                .criterion(hasItem(ModItems.RAW_TITANIUM), conditionsFromItem(ModItems.RAW_TITANIUM))
                .offerTo(exporter,"gold_titanium_alloying");


        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.ARC_REACTOR)
                .pattern(" # ")
                .pattern("#%#")
                .pattern(" # ")
                .input('#', Items.IRON_INGOT)
                .input('%', ModItems.VIBRANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.VIBRANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.VIBRANIUM_INGOT))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.IRON_SUIT_TEMPLATE, 2)
                .pattern("#&#")
                .pattern("#%#")
                .pattern("###")
                .input('#', Items.DIAMOND)
                .input('&', ModItems.IRON_SUIT_TEMPLATE)
                .input('%', ModItems.GOLD_TITANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.IRON_SUIT_TEMPLATE),
                        FabricRecipeProvider.conditionsFromItem(ModItems.IRON_SUIT_TEMPLATE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.SPEEDSTER_SUIT_TEMPLATE, 2)
                .pattern("#&#")
                .pattern("#%#")
                .pattern("###")
                .input('#', Items.DIAMOND)
                .input('&', ModItems.SPEEDSTER_SUIT_TEMPLATE)
                .input('%', Items.COPPER_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.SPEEDSTER_SUIT_TEMPLATE),
                        FabricRecipeProvider.conditionsFromItem(ModItems.SPEEDSTER_SUIT_TEMPLATE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.POWER_INJECTION)
                .pattern("  $")
                .pattern(" & ")
                .pattern("#  ")
                .input('$', ItemTags.STONE_BUTTONS)
                .input('&', Items.GLASS_PANE)
                .input('#', Items.IRON_NUGGET)
                .criterion(FabricRecipeProvider.hasItem(Items.GLASS_PANE),
                        FabricRecipeProvider.conditionsFromItem(Items.GLASS_PANE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.VIBRANIUM_SHIELD)
                .pattern(" # ")
                .pattern("###")
                .pattern(" # ")
                .input('#', ModItems.VIBRANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.VIBRANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.VIBRANIUM_INGOT))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.A_SYMBOLS_SHIELD)
                .input(ModItems.VIBRANIUM_SHIELD)
                .input(Items.RED_DYE)
                .input(Items.WHITE_DYE)
                .input(Items.BLUE_DYE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.VIBRANIUM_SHIELD),
                        FabricRecipeProvider.conditionsFromItem(ModItems.VIBRANIUM_SHIELD))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.CARTERS_SHIELD)
                .input(ModItems.VIBRANIUM_SHIELD)
                .input(Items.RED_DYE)
                .input(Items.BLUE_DYE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.VIBRANIUM_SHIELD),
                        FabricRecipeProvider.conditionsFromItem(ModItems.VIBRANIUM_SHIELD))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.COSMIC_SHIELD)
                .input(ModItems.VIBRANIUM_SHIELD)
                .input(Items.GOLD_NUGGET)
                .input(Items.PURPLE_DYE)
                .input(Items.BLUE_DYE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.VIBRANIUM_SHIELD),
                        FabricRecipeProvider.conditionsFromItem(ModItems.VIBRANIUM_SHIELD))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.CIRCUIT_BOARD)
                .input(Items.COPPER_INGOT)
                .input(Items.GOLD_NUGGET)
                .input(Items.REDSTONE)
                .criterion(FabricRecipeProvider.hasItem(Items.COPPER_INGOT),
                        FabricRecipeProvider.conditionsFromItem(Items.COPPER_INGOT))
                .offerTo(exporter);


        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.COLORING_COMPOUND)
                .input(Items.CLAY)
                .input(ModTags.Items.DYES)
                .input(ModTags.Items.DYES)
                .criterion(FabricRecipeProvider.hasItem(Items.CLAY),
                        FabricRecipeProvider.conditionsFromItem(Items.CLAY))
                .offerTo(exporter);

        //blocks
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.ARMOR_LIGHT_LEVELING_STATION)
                .pattern("&%")
                .pattern("##")
                .pattern("##")
                .input('#', ItemTags.PLANKS)
                .input('&', Items.BLACK_DYE)
                .input('%', Items.GLOWSTONE_DUST)
                .criterion(FabricRecipeProvider.hasItem(Items.GLOWSTONE_DUST),
                        FabricRecipeProvider.conditionsFromItem(Items.GLOWSTONE_DUST))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.ARMOR_DYEING_STATION)
                .pattern("$$")
                .pattern("##")
                .pattern("##")
                .input('#', ItemTags.PLANKS)
                .input('$', ModItems.COLORING_COMPOUND)
                .criterion(FabricRecipeProvider.hasItem(ModItems.COLORING_COMPOUND),
                        FabricRecipeProvider.conditionsFromItem(ModItems.COLORING_COMPOUND))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.UPGRADE_STATION)
                .pattern("##")
                .pattern("&&")
                .input('#', ItemTags.PLANKS)
                .input('&', Blocks.IRON_BLOCK)
                .criterion(FabricRecipeProvider.hasItem(Blocks.IRON_BLOCK),
                        FabricRecipeProvider.conditionsFromItem(Blocks.IRON_BLOCK))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.METEOR_RADAR)
                .pattern("###")
                .pattern("#*#")
                .pattern("%^%")
                .input('#', Items.IRON_INGOT)
                .input('*', ModItems.TITANIUM_INGOT)
                .input('^', ModItems.CIRCUIT_BOARD)
                .input('%', Items.REDSTONE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.CIRCUIT_BOARD),
                        FabricRecipeProvider.conditionsFromItem(ModItems.CIRCUIT_BOARD))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.KRYPTONITE_BlOCK)
                .pattern("##")
                .pattern("##")
                .input('#', ModBlocks.KRYPTONITE_CLUSTER)
                .criterion(FabricRecipeProvider.hasItem(ModBlocks.KRYPTONITE_CLUSTER),
                        FabricRecipeProvider.conditionsFromItem(ModBlocks.KRYPTONITE_CLUSTER))
                .offerTo(exporter);


        //upgrades
        StonecuttingRecipeJsonBuilder.createStonecutting(Ingredient.ofItems(ModItems.CIRCUIT_BOARD), RecipeCategory.TOOLS, ModItems.MECHANICAL_HUD_UPGRADE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.CIRCUIT_BOARD),
                        FabricRecipeProvider.conditionsFromItem(ModItems.CIRCUIT_BOARD))
                .offerTo(exporter);
        StonecuttingRecipeJsonBuilder.createStonecutting(Ingredient.ofItems(ModItems.CIRCUIT_BOARD), RecipeCategory.TOOLS, ModItems.SPEEDSTER_HUD_UPGRADE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.CIRCUIT_BOARD),
                        FabricRecipeProvider.conditionsFromItem(ModItems.CIRCUIT_BOARD))
                .offerTo(exporter);
        StonecuttingRecipeJsonBuilder.createStonecutting(Ingredient.ofItems(ModItems.CIRCUIT_BOARD), RecipeCategory.TOOLS, ModItems.LASER_UPGRADE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.CIRCUIT_BOARD),
                        FabricRecipeProvider.conditionsFromItem(ModItems.CIRCUIT_BOARD))
                .offerTo(exporter);
        StonecuttingRecipeJsonBuilder.createStonecutting(Ingredient.ofItems(ModItems.CIRCUIT_BOARD), RecipeCategory.TOOLS, ModItems.FlY_UPGRADE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.CIRCUIT_BOARD),
                        FabricRecipeProvider.conditionsFromItem(ModItems.CIRCUIT_BOARD))
                .offerTo(exporter);
        StonecuttingRecipeJsonBuilder.createStonecutting(Ingredient.ofItems(ModItems.CIRCUIT_BOARD), RecipeCategory.TOOLS, ModItems.TAKE_OFF_SUIT_UPGRADE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.CIRCUIT_BOARD),
                        FabricRecipeProvider.conditionsFromItem(ModItems.CIRCUIT_BOARD))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.KINETIC_ENERGY_STORAGE_UPGRADE)
                .input(ModItems.CIRCUIT_BOARD)
                .input(ModItems.VIBRANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.CIRCUIT_BOARD),
                        FabricRecipeProvider.conditionsFromItem(ModItems.CIRCUIT_BOARD))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.TOOLBELT_3_UPGRADE)
                .pattern("###")
                .pattern("#&#")
                .input('#', Items.LEATHER)
                .input('&', Items.IRON_INGOT)
                .criterion(FabricRecipeProvider.hasItem(Items.LEATHER),
                        FabricRecipeProvider.conditionsFromItem(Items.LEATHER))
                .offerTo(exporter);


        //simple armor recipes
        //vibranium
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.VIBRANIUM_HELMET)
                .pattern("###")
                .pattern("# #")
                .input('#', ModItems.VIBRANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.VIBRANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.VIBRANIUM_INGOT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.VIBRANIUM_CHESTPLATE)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .input('#', ModItems.VIBRANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.VIBRANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.VIBRANIUM_INGOT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.VIBRANIUM_LEGGINGS)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .input('#', ModItems.VIBRANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.VIBRANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.VIBRANIUM_INGOT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.VIBRANIUM_BOOTS)
                .pattern("# #")
                .pattern("# #")
                .input('#', ModItems.VIBRANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.VIBRANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.VIBRANIUM_INGOT))
                .offerTo(exporter);



        //Titanium
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.TITANIUM_HELMET)
                .pattern("###")
                .pattern("# #")
                .input('#', ModItems.TITANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TITANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.TITANIUM_INGOT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.TITANIUM_CHESTPLATE)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .input('#', ModItems.TITANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TITANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.TITANIUM_INGOT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.TITANIUM_LEGGINGS)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .input('#', ModItems.TITANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TITANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.TITANIUM_INGOT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.TITANIUM_BOOTS)
                .pattern("# #")
                .pattern("# #")
                .input('#', ModItems.TITANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TITANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.TITANIUM_INGOT))
                .offerTo(exporter);



        //vibranium
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.GOLD_TITANIUM_HELMET)
                .pattern("###")
                .pattern("# #")
                .input('#', ModItems.GOLD_TITANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.GOLD_TITANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.GOLD_TITANIUM_INGOT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.GOLD_TITANIUM_CHESTPLATE)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .input('#', ModItems.GOLD_TITANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.GOLD_TITANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.GOLD_TITANIUM_INGOT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.GOLD_TITANIUM_LEGGINGS)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .input('#', ModItems.GOLD_TITANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.GOLD_TITANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.GOLD_TITANIUM_INGOT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.GOLD_TITANIUM_BOOTS)
                .pattern("# #")
                .pattern("# #")
                .input('#', ModItems.GOLD_TITANIUM_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.GOLD_TITANIUM_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.GOLD_TITANIUM_INGOT))
                .offerTo(exporter);

        //Advanced Armors
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.MARK6_VIBRANIUM_HELMET)
                .input(ModItems.VIBRANIUM_HELMET)
                .input(ModItems.IRON_SUIT_TEMPLATE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.VIBRANIUM_HELMET),
                        FabricRecipeProvider.conditionsFromItem(ModItems.VIBRANIUM_HELMET))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.MARK6_VIBRANIUM_CHESTPLATE)
                .input(ModItems.VIBRANIUM_CHESTPLATE)
                .input(ModItems.IRON_SUIT_TEMPLATE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.VIBRANIUM_CHESTPLATE),
                        FabricRecipeProvider.conditionsFromItem(ModItems.VIBRANIUM_CHESTPLATE))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.MARK6_VIBRANIUM_LEGGINGS)
                .input(ModItems.VIBRANIUM_LEGGINGS)
                .input(ModItems.IRON_SUIT_TEMPLATE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.VIBRANIUM_LEGGINGS),
                        FabricRecipeProvider.conditionsFromItem(ModItems.VIBRANIUM_LEGGINGS))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.MARK6_VIBRANIUM_BOOTS)
                .input(ModItems.VIBRANIUM_BOOTS)
                .input(ModItems.IRON_SUIT_TEMPLATE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.VIBRANIUM_BOOTS),
                        FabricRecipeProvider.conditionsFromItem(ModItems.VIBRANIUM_BOOTS))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.MARK3_GOLD_TITANIUM_HELMET)
                .input(ModItems.GOLD_TITANIUM_HELMET)
                .input(ModItems.IRON_SUIT_TEMPLATE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.GOLD_TITANIUM_HELMET),
                        FabricRecipeProvider.conditionsFromItem(ModItems.GOLD_TITANIUM_HELMET))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.MARK3_GOLD_TITANIUM_CHESTPLATE)
                .input(ModItems.GOLD_TITANIUM_CHESTPLATE)
                .input(ModItems.IRON_SUIT_TEMPLATE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.GOLD_TITANIUM_CHESTPLATE),
                        FabricRecipeProvider.conditionsFromItem(ModItems.GOLD_TITANIUM_CHESTPLATE))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.MARK3_GOLD_TITANIUM_LEGGINGS)
                .input(ModItems.GOLD_TITANIUM_LEGGINGS)
                .input(ModItems.IRON_SUIT_TEMPLATE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.GOLD_TITANIUM_LEGGINGS),
                        FabricRecipeProvider.conditionsFromItem(ModItems.GOLD_TITANIUM_LEGGINGS))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.MARK3_GOLD_TITANIUM_BOOTS)
                .input(ModItems.GOLD_TITANIUM_BOOTS)
                .input(ModItems.IRON_SUIT_TEMPLATE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.GOLD_TITANIUM_BOOTS),
                        FabricRecipeProvider.conditionsFromItem(ModItems.GOLD_TITANIUM_BOOTS))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.MARK45_NETHERITE_HELMET)
                .input(Items.NETHERITE_HELMET)
                .input(ModItems.IRON_SUIT_TEMPLATE)
                .criterion(FabricRecipeProvider.hasItem(Items.NETHERITE_HELMET),
                        FabricRecipeProvider.conditionsFromItem(Items.NETHERITE_HELMET))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.MARK45_NETHERITE_CHESTPLATE)
                .input(Items.NETHERITE_CHESTPLATE)
                .input(ModItems.IRON_SUIT_TEMPLATE)
                .criterion(FabricRecipeProvider.hasItem(Items.NETHERITE_CHESTPLATE),
                        FabricRecipeProvider.conditionsFromItem(Items.NETHERITE_CHESTPLATE))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.MARK45_NETHERITE_LEGGINGS)
                .input(Items.NETHERITE_LEGGINGS)
                .input(ModItems.IRON_SUIT_TEMPLATE)
                .criterion(FabricRecipeProvider.hasItem(Items.NETHERITE_LEGGINGS),
                        FabricRecipeProvider.conditionsFromItem(Items.NETHERITE_LEGGINGS))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.MARK45_NETHERITE_BOOTS)
                .input(Items.NETHERITE_BOOTS)
                .input(ModItems.IRON_SUIT_TEMPLATE)
                .criterion(FabricRecipeProvider.hasItem(Items.NETHERITE_BOOTS),
                        FabricRecipeProvider.conditionsFromItem(Items.NETHERITE_BOOTS))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.SPEEDSTER_GOLD_TITANIUM_HELMET)
                .input(ModItems.GOLD_TITANIUM_HELMET)
                .input(ModItems.SPEEDSTER_SUIT_TEMPLATE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.GOLD_TITANIUM_HELMET),
                        FabricRecipeProvider.conditionsFromItem(ModItems.GOLD_TITANIUM_HELMET))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.SPEEDSTER_GOLD_TITANIUM_CHESTPLATE)
                .input(ModItems.GOLD_TITANIUM_CHESTPLATE)
                .input(ModItems.SPEEDSTER_SUIT_TEMPLATE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.GOLD_TITANIUM_CHESTPLATE),
                        FabricRecipeProvider.conditionsFromItem(ModItems.GOLD_TITANIUM_CHESTPLATE))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.SPEEDSTER_GOLD_TITANIUM_LEGGINGS)
                .input(ModItems.GOLD_TITANIUM_LEGGINGS)
                .input(ModItems.SPEEDSTER_SUIT_TEMPLATE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.GOLD_TITANIUM_LEGGINGS),
                        FabricRecipeProvider.conditionsFromItem(ModItems.GOLD_TITANIUM_LEGGINGS))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.SPEEDSTER_GOLD_TITANIUM_BOOTS)
                .input(ModItems.GOLD_TITANIUM_BOOTS)
                .input(ModItems.SPEEDSTER_SUIT_TEMPLATE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.GOLD_TITANIUM_BOOTS),
                        FabricRecipeProvider.conditionsFromItem(ModItems.GOLD_TITANIUM_BOOTS))
                .offerTo(exporter);

        offerSmithingTrimRecipe(exporter, ModItems.IRON_SUIT_TEMPLATE, Identifier.of(MyneHeroes.MOD_ID, "iron_suit"));
        offerSmithingTrimRecipe(exporter, ModItems.SPEEDSTER_SUIT_TEMPLATE, Identifier.of(MyneHeroes.MOD_ID, "speedster_suit"));
    }}
