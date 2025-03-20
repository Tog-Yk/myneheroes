package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.togyk.myneheroes.ability.Ability;

public class UpgradeItem extends Item {
    private final Ability ability;
    private final int levelCost;
    private final ArmorItem.Type equipmentSlot;

    public UpgradeItem(Ability ability, int levelCost, ArmorItem.Type slot ,Settings settings) {
        super(settings);

        this.ability = ability;
        this.levelCost = levelCost;
        this.equipmentSlot = slot;
    }

    public Ability getAbility() {
        return ability;
    }

    public int getLevelCost() {
        return levelCost;
    }

    public ArmorItem.Type getEquipmentSlot() {
        return equipmentSlot;
    }
}
