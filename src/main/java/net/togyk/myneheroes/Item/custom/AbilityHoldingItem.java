package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.Item;
import net.togyk.myneheroes.ability.Ability;

import java.util.List;

public class AbilityHoldingItem extends Item {
    private List<Ability> abilities;

    public AbilityHoldingItem(Settings settings, List<Ability> abilities) {
        super(settings);
        this.abilities = abilities;
    }

    public List<Ability> getAbilities() {
        return this.abilities;
    }
}
