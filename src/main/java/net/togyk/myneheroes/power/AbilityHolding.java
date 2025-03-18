package net.togyk.myneheroes.power;

import net.minecraft.item.ItemStack;
import net.togyk.myneheroes.ability.Ability;

import java.util.List;

public interface AbilityHolding {
    List<Ability> getAbilities(ItemStack stack);
    void saveAbility(ItemStack stack, Ability ability);
}
