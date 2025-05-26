package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.togyk.myneheroes.ability.Ability;

import java.util.List;

public class UpgradeWithTooltipItem extends UpgradeItem {
    private final Text tooltip;

    public UpgradeWithTooltipItem(Ability ability, int levelCost, ArmorItem.Type slot, Text tooltip, Settings settings) {
        super(ability, levelCost, slot, settings);
        this.tooltip = tooltip;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(this.tooltip);
        super.appendTooltip(stack, context, tooltip, type);
    }
}
