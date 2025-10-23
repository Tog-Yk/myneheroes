package net.togyk.myneheroes.compat.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.entity.EntityType;
import net.minecraft.recipe.RecipeEntry;
import net.togyk.myneheroes.recipe.EntityInteractionRecipe;

import java.util.List;

public class EntityInteractionDisplay extends BasicDisplay {
    private final EntityType<?> entityType;

    public EntityInteractionDisplay(RecipeEntry<EntityInteractionRecipe> recipe) {
        super(List.of(EntryIngredients.ofIngredient(recipe.value().getIngredients().get(0))),
                List.of(EntryIngredient.of(EntryStacks.of(recipe.value().getResult(null)))));
        this.entityType = recipe.value().entityType();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return EntityInteractionCategory.ENTITY_INTERACTION;
    }

    public EntityType<?> getEntityType() {
        return this.entityType;
    }
}