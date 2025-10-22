package net.togyk.myneheroes.recipe;

import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

public record EntityInteractionRecipeInput(ItemStack input, EntityType<?> entityType) implements RecipeInput {
    @Override
    public ItemStack getStackInSlot(int slot) {
        return input;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return this.input.isEmpty();
    }
}
