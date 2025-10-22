package net.togyk.myneheroes.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public record EntityInteractionRecipe(String group, Ingredient ingredient, EntityType<?> entityType, ItemStack result) implements Recipe<EntityInteractionRecipeInput> {
    @Override
    public boolean matches(EntityInteractionRecipeInput input, World world) {
        return this.ingredient.test(input.input()) && this.entityType == input.entityType();
    }

    @Override
    public ItemStack craft(EntityInteractionRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return this.result.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return this.result;
    }


    public static class Serializer implements RecipeSerializer<EntityInteractionRecipe> {

        public static final MapCodec<EntityInteractionRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(EntityInteractionRecipe::getGroup),
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(EntityInteractionRecipe::ingredient),
                Registries.ENTITY_TYPE.getCodec().fieldOf("entity").forGetter(EntityInteractionRecipe::entityType),
                ItemStack.CODEC.fieldOf("result").forGetter(EntityInteractionRecipe::result)
        ).apply(inst, EntityInteractionRecipe::new));

        public static final PacketCodec<RegistryByteBuf, EntityInteractionRecipe> STREAM_CODEC =
                PacketCodec.tuple(
                        PacketCodecs.STRING, EntityInteractionRecipe::group,
                        Ingredient.PACKET_CODEC, EntityInteractionRecipe::ingredient,
                        PacketCodecs.registryCodec(Registries.ENTITY_TYPE.getCodec()), EntityInteractionRecipe::entityType,
                        ItemStack.PACKET_CODEC, EntityInteractionRecipe::result,
                        EntityInteractionRecipe::new);

        public MapCodec<EntityInteractionRecipe> codec() {
            return CODEC;
        }

        public PacketCodec<RegistryByteBuf, EntityInteractionRecipe> packetCodec() {
            return STREAM_CODEC;
        }
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ENTITY_INTERACTION_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.ENTITY_INTERACTION_TYPE;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> defaultedList = DefaultedList.of();
        defaultedList.add(this.ingredient);
        return defaultedList;
    }

    @Override
    public ItemStack createIcon() {
        return Items.MILK_BUCKET.getDefaultStack();
    }
}
