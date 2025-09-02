package net.togyk.myneheroes.recipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;

public class ModRecipes {
    public static final RecipeType<ArmorFabricatingRecipe> ARMOR_FABRICATING_TYPE = registerType("armor_fabricating");
    public static final RecipeSerializer<ArmorFabricatingRecipe> ARMOR_FABRICAING_SERIALIZER = registerSerializer("armor_fabricating", new ArmorFabricatingRecipe.Serializer());

    static <T extends Recipe<?>> RecipeType<T> registerType(final String id) {
        return Registry.register(Registries.RECIPE_TYPE, Identifier.of(MyneHeroes.MOD_ID, id), new RecipeType<T>() {
            public String toString() {
                return id;
            }
        });
    }

    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S registerSerializer(String id, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(MyneHeroes.MOD_ID, id), serializer);
    }


    public static void registerRecipes() {
        MyneHeroes.LOGGER.info("Registering Custom Recipes for " + MyneHeroes.MOD_ID);
    }
}
