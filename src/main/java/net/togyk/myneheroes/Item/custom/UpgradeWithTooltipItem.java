package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.togyk.myneheroes.upgrade.Upgrade;

import java.util.List;

public class UpgradeWithTooltipItem extends UpgradeItem {
    private final Text tooltip;

    public UpgradeWithTooltipItem(Upgrade upgrade, Text tooltip, Settings settings) {
        super(upgrade, settings);
        this.tooltip = tooltip;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(this.tooltip);
        super.appendTooltip(stack, context, tooltip, type);
    }
}
