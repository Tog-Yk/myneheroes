package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.togyk.myneheroes.ability.Ability;

import java.util.List;

public class UpgradeItem extends Item {
    private final List<Ability> abilities;
    private final int levelCost;
    private final ArmorItem.Type equipmentSlot;

    public UpgradeItem(Ability ability, int levelCost, ArmorItem.Type slot ,Settings settings) {
        super(settings);

        this.abilities = List.of(ability);
        this.levelCost = levelCost;
        this.equipmentSlot = slot;
    }

    public UpgradeItem(List<Ability> abilities, int levelCost, ArmorItem.Type slot ,Settings settings) {
        super(settings);

        this.abilities = abilities;
        this.levelCost = levelCost;
        this.equipmentSlot = slot;
    }

    public int getLevelCost() {
        return levelCost;
    }

    public ArmorItem.Type getEquipmentSlot() {
        return equipmentSlot;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }
}
