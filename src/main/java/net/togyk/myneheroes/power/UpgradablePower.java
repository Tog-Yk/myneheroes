package net.togyk.myneheroes.power;

import net.togyk.myneheroes.upgrade.Upgrade;

import java.util.ArrayList;
import java.util.List;

public interface UpgradablePower {
    default boolean canBeUpgraded() {
        return true;
    }
    default boolean canUpgrade(Upgrade upgrade) {
        return true;
    }
    default List<Upgrade> getUpgrades() {
        return new ArrayList<>();
    }
    default void setUpgrades(List<Upgrade> upgrades) {}
    default void saveUpgrade( Upgrade upgrade) {}
}
