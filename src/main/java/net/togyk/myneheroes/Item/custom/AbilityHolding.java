package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.togyk.myneheroes.ability.Ability;

import java.util.List;

public interface AbilityHolding {
    List<Ability> getAbilities(ItemStack stack, World world);
    void saveAbility(ItemStack stack, World world, Ability ability);
}
