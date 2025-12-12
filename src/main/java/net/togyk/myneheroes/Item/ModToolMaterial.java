package net.togyk.myneheroes.Item;

import com.google.common.base.Suppliers;
import net.minecraft.block.Block;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.TagKey;
import net.togyk.myneheroes.util.ModTags;

import java.util.function.Supplier;

public enum ModToolMaterial implements ToolMaterial {
    ADAMANTIUM (ModTags.Blocks.INCORRECT_FOR_ADAMANTIUM_TOOL, 2131, 9.0F, 4.5F, 14, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)),
    MJOLNIR (ModTags.Blocks.INCORRECT_FOR_ADAMANTIUM_TOOL, 2131, 9.0F, 4.5F, 14, () -> Ingredient.ofItems(Items.NETHERITE_INGOT));

    private final TagKey<Block> inverseTag;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    ModToolMaterial(TagKey<Block> inverseTag, int itemDurability, float miningSpeed, float attackDamage,
                    int enchantability, Supplier<Ingredient> repairIngredient) {
        this.inverseTag = inverseTag;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = Suppliers.memoize(repairIngredient::get);
    }

    @Override
    public int getDurability() {
        return this.itemDurability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public TagKey<Block> getInverseTag() {
        return this.inverseTag;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}