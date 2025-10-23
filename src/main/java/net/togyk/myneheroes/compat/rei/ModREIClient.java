package net.togyk.myneheroes.compat.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.togyk.myneheroes.block.ModBlocks;
import net.togyk.myneheroes.client.screen.ArmorFabricatorScreen;
import net.togyk.myneheroes.recipe.ArmorFabricatingRecipe;
import net.togyk.myneheroes.recipe.EntityInteractionRecipe;
import net.togyk.myneheroes.recipe.ModRecipes;

public class ModREIClient implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new ArmorFabricatorCategory());

        registry.addWorkstations(ArmorFabricatorCategory.ARMOR_FABRICATOR, EntryStacks.of(ModBlocks.ARMOR_FABRICATOR));

        registry.add(new EntityInteractionCategory());
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(ArmorFabricatingRecipe.class, ModRecipes.ARMOR_FABRICATING_TYPE,
                ArmorFabricatorDisplay::new);
        registry.registerRecipeFiller(EntityInteractionRecipe.class, ModRecipes.ENTITY_INTERACTION_TYPE,
                EntityInteractionDisplay::new);
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(screen -> new Rectangle(((screen.width - 176) / 2) + 78,
                        ((screen.height - 166) / 2) + 30, 20, 25), ArmorFabricatorScreen.class,
                ArmorFabricatorCategory.ARMOR_FABRICATOR);
    }
}