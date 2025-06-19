package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.ItemStack;
import net.togyk.myneheroes.upgrade.Upgrade;

import java.util.ArrayList;
import java.util.List;

public interface UpgradableItem {
    default boolean canBeUpgraded() {
        return true;
    }
    default boolean canUpgrade(ItemStack stack, Upgrade upgradeStack) {
        return true;
    }
    default List<Upgrade> getUpgrades(ItemStack stack) {
        return new ArrayList<>();
    }
    default void setUpgrades(ItemStack stack, List<Upgrade> upgrades) {}
    default void saveUpgrade(ItemStack stack, Upgrade upgrade) {}
}
