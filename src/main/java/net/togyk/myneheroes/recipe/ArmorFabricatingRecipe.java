package net.togyk.myneheroes.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.togyk.myneheroes.block.ModBlocks;

public record ArmorFabricatingRecipe(String group, Ingredient ingredient, ItemStack result) implements Recipe<SingleStackRecipeInput> {

    public RecipeType<?> getType() {
        return ModRecipes.ARMOR_FABRICATING_TYPE;
    }

    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ARMOR_FABRICAING_SERIALIZER;
    }

    public String getGroup() {
        return this.group;
    }

    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return this.result;
    }

    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> defaultedList = DefaultedList.of();
        defaultedList.add(this.ingredient);
        return defaultedList;
    }

    public boolean fits(int width, int height) {
        return true;
    }

    public ItemStack craft(SingleStackRecipeInput singleStackRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
        return this.result.copy();
    }

    public static class Serializer implements RecipeSerializer<ArmorFabricatingRecipe> {
        public static final MapCodec<ArmorFabricatingRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(ArmorFabricatingRecipe::getGroup),
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(ArmorFabricatingRecipe::ingredient),
                ItemStack.CODEC.fieldOf("result").forGetter(ArmorFabricatingRecipe::result)
        ).apply(inst, ArmorFabricatingRecipe::new));

        public static final PacketCodec<RegistryByteBuf, ArmorFabricatingRecipe> STREAM_CODEC =
                PacketCodec.tuple(
                        PacketCodecs.STRING, ArmorFabricatingRecipe::group,
                        Ingredient.PACKET_CODEC, ArmorFabricatingRecipe::ingredient,
                        ItemStack.PACKET_CODEC, ArmorFabricatingRecipe::result,
                        ArmorFabricatingRecipe::new);

        public MapCodec<ArmorFabricatingRecipe> codec() {
            return CODEC;
        }

        public PacketCodec<RegistryByteBuf, ArmorFabricatingRecipe> packetCodec() {
            return STREAM_CODEC;
        }
    }

    public boolean matches(SingleStackRecipeInput singleStackRecipeInput, World world) {
        return this.ingredient.test(singleStackRecipeInput.item());
    }

    public ItemStack createIcon() {
        return new ItemStack(ModBlocks.ARMOR_FABRICATOR);
    }
}