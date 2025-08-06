package net.togyk.myneheroes.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.togyk.myneheroes.Item.ModItems;
import net.togyk.myneheroes.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ItemTags.TRIM_MATERIALS)
                .add(ModItems.VIBRANIUM_INGOT)
                .add(ModItems.TITANIUM_INGOT)
                .add(ModItems.GOLD_TITANIUM_INGOT)
        ;

        getOrCreateTagBuilder(ItemTags.TRIM_TEMPLATES)
                .add(ModItems.IRON_SUIT_TEMPLATE)
                .add(ModItems.SPEEDSTER_SUIT_TEMPLATE)
        ;

        getOrCreateTagBuilder(ItemTags.HEAD_ARMOR)
                .add(ModItems.VIBRANIUM_HELMET)
                .add(ModItems.TITANIUM_HELMET)
                .add(ModItems.GOLD_TITANIUM_HELMET)
        ;

        getOrCreateTagBuilder(ItemTags.HEAD_ARMOR_ENCHANTABLE)
                .add(ModItems.MARK6_VIBRANIUM_HELMET)
                .add(ModItems.MARK3_GOLD_TITANIUM_HELMET)
                .add(ModItems.MARK45_NETHERITE_HELMET)
                .add(ModItems.SPEEDSTER_GOLD_TITANIUM_HELMET)
                .add(ModItems.SPEEDSTER_VIBRANIUM_HELMET)
        ;

        getOrCreateTagBuilder(ItemTags.CHEST_ARMOR)
                .add(ModItems.VIBRANIUM_CHESTPLATE)
                .add(ModItems.TITANIUM_CHESTPLATE)
                .add(ModItems.GOLD_TITANIUM_CHESTPLATE)
        ;

        getOrCreateTagBuilder(ItemTags.CHEST_ARMOR_ENCHANTABLE)
                .add(ModItems.MARK6_VIBRANIUM_CHESTPLATE)
                .add(ModItems.MARK3_GOLD_TITANIUM_CHESTPLATE)
                .add(ModItems.MARK45_NETHERITE_CHESTPLATE)
                .add(ModItems.SPEEDSTER_GOLD_TITANIUM_CHESTPLATE)
                .add(ModItems.SPEEDSTER_VIBRANIUM_CHESTPLATE)
        ;

        getOrCreateTagBuilder(ItemTags.LEG_ARMOR)
                .add(ModItems.VIBRANIUM_LEGGINGS)
                .add(ModItems.TITANIUM_LEGGINGS)
                .add(ModItems.GOLD_TITANIUM_LEGGINGS)
        ;

        getOrCreateTagBuilder(ItemTags.LEG_ARMOR_ENCHANTABLE)
                .add(ModItems.MARK6_VIBRANIUM_LEGGINGS)
                .add(ModItems.MARK3_GOLD_TITANIUM_LEGGINGS)
                .add(ModItems.MARK45_NETHERITE_LEGGINGS)
                .add(ModItems.SPEEDSTER_GOLD_TITANIUM_LEGGINGS)
                .add(ModItems.SPEEDSTER_VIBRANIUM_LEGGINGS)
        ;

        getOrCreateTagBuilder(ItemTags.FOOT_ARMOR)
                .add(ModItems.VIBRANIUM_BOOTS)
                .add(ModItems.TITANIUM_BOOTS)
                .add(ModItems.GOLD_TITANIUM_BOOTS)
        ;

        getOrCreateTagBuilder(ItemTags.FOOT_ARMOR_ENCHANTABLE)
                .add(ModItems.MARK6_VIBRANIUM_BOOTS)
                .add(ModItems.MARK3_GOLD_TITANIUM_BOOTS)
                .add(ModItems.MARK45_NETHERITE_BOOTS)
                .add(ModItems.SPEEDSTER_GOLD_TITANIUM_BOOTS)
                .add(ModItems.SPEEDSTER_VIBRANIUM_BOOTS)
        ;

        getOrCreateTagBuilder(ItemTags.DURABILITY_ENCHANTABLE)
                .add(ModItems.MARK6_VIBRANIUM_HELMET)
                .add(ModItems.MARK6_VIBRANIUM_CHESTPLATE)
                .add(ModItems.MARK6_VIBRANIUM_LEGGINGS)
                .add(ModItems.MARK6_VIBRANIUM_BOOTS)

                .add(ModItems.MARK3_GOLD_TITANIUM_HELMET)
                .add(ModItems.MARK3_GOLD_TITANIUM_CHESTPLATE)
                .add(ModItems.MARK3_GOLD_TITANIUM_LEGGINGS)
                .add(ModItems.MARK3_GOLD_TITANIUM_BOOTS)

                .add(ModItems.MARK45_NETHERITE_HELMET)
                .add(ModItems.MARK45_NETHERITE_CHESTPLATE)
                .add(ModItems.MARK45_NETHERITE_LEGGINGS)
                .add(ModItems.MARK45_NETHERITE_BOOTS)

                .add(ModItems.SPEEDSTER_GOLD_TITANIUM_HELMET)
                .add(ModItems.SPEEDSTER_GOLD_TITANIUM_CHESTPLATE)
                .add(ModItems.SPEEDSTER_GOLD_TITANIUM_LEGGINGS)
                .add(ModItems.SPEEDSTER_GOLD_TITANIUM_BOOTS)

                .add(ModItems.SPEEDSTER_VIBRANIUM_HELMET)
                .add(ModItems.SPEEDSTER_VIBRANIUM_CHESTPLATE)
                .add(ModItems.SPEEDSTER_VIBRANIUM_LEGGINGS)
                .add(ModItems.SPEEDSTER_VIBRANIUM_BOOTS)
        ;

        getOrCreateTagBuilder(ModTags.Items.COLORING_FUEL)
                .add(ModItems.COLORING_COMPOUND)
        ;

        getOrCreateTagBuilder(ModTags.Items.DYES)
                .add(Items.WHITE_DYE)
                .add(Items.LIGHT_GRAY_DYE)
                .add(Items.GRAY_DYE)
                .add(Items.BLACK_DYE)
                .add(Items.BROWN_DYE)
                .add(Items.RED_DYE)
                .add(Items.ORANGE_DYE)
                .add(Items.YELLOW_DYE)
                .add(Items.LIME_DYE)
                .add(Items.GREEN_DYE)
                .add(Items.CYAN_DYE)
                .add(Items.LIGHT_BLUE_DYE)
                .add(Items.BLUE_DYE)
                .add(Items.PURPLE_DYE)
                .add(Items.MAGENTA_DYE)
                .add(Items.PINK_DYE)
        ;

        getOrCreateTagBuilder(ModTags.Items.REACTOR_FUEL)
                .add(ModItems.VIBRANIUM_INGOT)
        ;
    }
}
