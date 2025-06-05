package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public interface UpgradableItem {
    default boolean canBeUpgraded() {
        return true;
    }
    default boolean canUpgrade(ItemStack stack, ItemStack upgradeStack, World world) {
        return true;
    }
    default List<ItemStack> getUpgrades(ItemStack stack, World world) {
        return new ArrayList<>();
    }
    default void setUpgrades(ItemStack stack, List<ItemStack> upgrades, World world) {}
    default void saveUpgrade(ItemStack stack, ItemStack upgrade, World world) {}
}
