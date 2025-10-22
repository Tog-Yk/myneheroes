package net.togyk.myneheroes.datagen;

import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.recipe.EntityInteractionRecipe;

public record EntityInteractionRecipeJsonBuilder(Item ingredient, EntityType<?> entityType, Item result) {
    public static EntityInteractionRecipeJsonBuilder create(Item ingredient, EntityType<?> entityType, Item result) {
        return new EntityInteractionRecipeJsonBuilder(ingredient, entityType, result);
    }

    public void offerTo(RecipeExporter exporter, Identifier recipeId) {
        exporter.accept(recipeId, new EntityInteractionRecipe("", Ingredient.ofItems(ingredient), entityType, result.getDefaultStack()), null);
    }

    public void offerTo(RecipeExporter exporter) {
        exporter.accept(Registries.ITEM.getId(result), new EntityInteractionRecipe("", Ingredient.ofItems(ingredient), entityType, result.getDefaultStack()), null);
    }
}
