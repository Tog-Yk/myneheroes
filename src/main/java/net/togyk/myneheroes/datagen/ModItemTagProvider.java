package net.togyk.myneheroes.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.togyk.myneheroes.Item.ModItems;

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
                .add(ModItems.GOLD_TITANIUM_INGOT);

        getOrCreateTagBuilder(ItemTags.TRIM_TEMPLATES)
                .add(ModItems.IRON_SUIT_TEMPLATE);

        getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.VIBRANIUM_HELMET)
                .add(ModItems.VIBRANIUM_CHESTPLATE)
                .add(ModItems.VIBRANIUM_LEGGINGS)
                .add(ModItems.VIBRANIUM_BOOTS)

                .add(ModItems.TITANIUM_HELMET)
                .add(ModItems.TITANIUM_CHESTPLATE)
                .add(ModItems.TITANIUM_LEGGINGS)
                .add(ModItems.TITANIUM_BOOTS)

                .add(ModItems.GOLD_TITANIUM_HELMET)
                .add(ModItems.GOLD_TITANIUM_CHESTPLATE)
                .add(ModItems.GOLD_TITANIUM_LEGGINGS)
                .add(ModItems.GOLD_TITANIUM_BOOTS);

        getOrCreateTagBuilder(ItemTags.ARMOR_ENCHANTABLE)
                .add(ModItems.VIBRANIUM_HELMET)
                .add(ModItems.VIBRANIUM_CHESTPLATE)
                .add(ModItems.VIBRANIUM_LEGGINGS)
                .add(ModItems.VIBRANIUM_BOOTS)

                .add(ModItems.TITANIUM_HELMET)
                .add(ModItems.TITANIUM_CHESTPLATE)
                .add(ModItems.TITANIUM_LEGGINGS)
                .add(ModItems.TITANIUM_BOOTS)

                .add(ModItems.GOLD_TITANIUM_HELMET)
                .add(ModItems.GOLD_TITANIUM_CHESTPLATE)
                .add(ModItems.GOLD_TITANIUM_LEGGINGS)
                .add(ModItems.GOLD_TITANIUM_BOOTS)

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
                .add(ModItems.MARK45_NETHERITE_BOOTS);
    }

}
