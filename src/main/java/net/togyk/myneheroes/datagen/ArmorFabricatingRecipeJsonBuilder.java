package net.togyk.myneheroes.datagen;

import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.recipe.ArmorFabricatingRecipe;

public record ArmorFabricatingRecipeJsonBuilder(Item ingredient, Item result) {

    public static ArmorFabricatingRecipeJsonBuilder create(Item ingredient, Item result) {
        return new ArmorFabricatingRecipeJsonBuilder(ingredient, result);
    }

    public void offerTo(RecipeExporter exporter, Identifier recipeId) {
        exporter.accept(recipeId, new ArmorFabricatingRecipe("armor_fabricating", Ingredient.ofItems(ingredient), result.getDefaultStack()), null);
    }

    public void offerTo(RecipeExporter exporter) {
        exporter.accept(Registries.ITEM.getId(result), new ArmorFabricatingRecipe("armor_fabricating", Ingredient.ofItems(ingredient), result.getDefaultStack()), null);
    }
}