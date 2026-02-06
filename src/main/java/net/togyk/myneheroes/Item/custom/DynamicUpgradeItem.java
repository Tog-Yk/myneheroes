package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.togyk.myneheroes.upgrade.Upgrade;

import java.util.function.Function;

public class DynamicUpgradeItem extends UpgradeItem {
    private final Function<ArmorItem.Type, Upgrade> upgradeSupplier;

    public DynamicUpgradeItem(Function<ArmorItem.Type, Upgrade> upgradeSupplier, Settings settings) {
        super(null, settings);
        this.upgradeSupplier = upgradeSupplier;
    }

    public DynamicUpgradeItem(Function<ArmorItem.Type, Upgrade> upgradeSupplier, Text tooltip, Settings settings) {
        super(null, tooltip, settings);
        this.upgradeSupplier = upgradeSupplier;
    }

    @Override
    public Upgrade getUpgrade(ItemStack stack, ItemStack upgradeableStack) {
        if (upgradeableStack.getItem() instanceof ArmorItem armorItem) {
            return this.upgradeSupplier.apply(armorItem.getType());
        } else if (upgradeableStack.getItem() instanceof PowerInjectionItem) {
            return this.upgradeSupplier.apply(null);
        }

        return null;
    }
}
