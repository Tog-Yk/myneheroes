package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.togyk.myneheroes.upgrade.Upgrade;

public class UpgradeItem extends Item {
    private final Upgrade upgrade;

    public UpgradeItem(Upgrade upgrade, Settings settings) {
        super(settings);

        this.upgrade = upgrade;
    }

    public Upgrade getUpgrade(World world) {
        return upgrade.copy();
    }
}
