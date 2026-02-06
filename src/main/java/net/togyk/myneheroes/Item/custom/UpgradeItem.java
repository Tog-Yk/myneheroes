package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.togyk.myneheroes.component.ModDataComponentTypes;
import net.togyk.myneheroes.upgrade.Upgrade;
import net.togyk.myneheroes.util.AbilityUtil;

import java.util.List;

public class UpgradeItem extends Item implements UpgradeHolding {
    private final Upgrade upgrade;
    private final Text tooltip;

    public UpgradeItem(Upgrade upgrade, Settings settings) {
        this(upgrade, null, settings);
    }

    public UpgradeItem(Upgrade upgrade, Text tooltip, Settings settings) {
        super(settings);

        this.upgrade = upgrade;
        this.tooltip = tooltip;
    }

    public Upgrade getUpgrade(ItemStack stack, ItemStack upgradeableStack) {
        NbtCompound nbt = stack.getOrDefault(ModDataComponentTypes.UPGRADES, new NbtCompound());

        Upgrade upgrade = this.upgrade.copy();
        if (nbt.contains("upgrade")) {
            upgrade = AbilityUtil.nbtToUpgrade(nbt.getCompound("upgrade"));
        }
        upgrade.setHolderStack(stack);
        return upgrade;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (this.tooltip != null) {
            tooltip.add(this.tooltip);
        }

        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.translatable("tooltip.myneheroes.is_upgrade").formatted(Formatting.GRAY));
    }
}
