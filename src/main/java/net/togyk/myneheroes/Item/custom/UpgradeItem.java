package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.togyk.myneheroes.upgrade.Upgrade;

public class UpgradeItem extends Item {
    private final Upgrade upgrade;

    public UpgradeItem(Upgrade upgrade, Settings settings) {
        super(settings);

        this.upgrade = upgrade;
    }

    public Upgrade getUpgrade(ItemStack stack) {
        return upgrade.copy();
    }

    public void saveUpgrade(ItemStack stack, Upgrade upgrade) {
    }
}
