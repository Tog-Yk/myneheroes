package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.ItemStack;
import net.togyk.myneheroes.ability.Ability;

import java.util.ArrayList;
import java.util.List;

public interface AbilityHolding {
    default List<Ability> getAbilities(ItemStack stack) {return new ArrayList<>();}
    default List<Ability> getArmorAbilities(ItemStack stack) {return new ArrayList<>();}
    default List<Ability> getHotbarAbilities(ItemStack stack) {return new ArrayList<>();}
    default List<Ability> getMainHandAbilities(ItemStack stack) {return new ArrayList<>();}
    default List<Ability> getOffHandAbilities(ItemStack stack) {return new ArrayList<>();}
    default List<Ability> getAccessoriesAbilities(ItemStack stack) {return new ArrayList<>();}
    void saveAbility(ItemStack stack, Ability ability);
}
