package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.togyk.myneheroes.component.ModDataComponentTypes;
import net.togyk.myneheroes.upgrade.ColorUpgrade;
import net.togyk.myneheroes.upgrade.Upgrade;
import net.togyk.myneheroes.upgrade.Upgrades;

import java.util.List;

public class SimpleDyeableItem extends UpgradeItem implements DyeableItem {
    private final int defaultColor;

    public SimpleDyeableItem(int defaultColor, Settings settings) {
        super(Upgrades.COLOR.copy(), settings);
        this.defaultColor = defaultColor;
    }

    public int getColor(ItemStack stack, int index) {
        if (layerIsDyeable(index) && index == 0) {
            List<Integer> colors = stack.get(ModDataComponentTypes.COLORS);
            if (colors != null && !colors.isEmpty()) {
                return colors.get(index);
            } else {
                return defaultColor;
            }
        }
        return -1;
    }

    @Override
    public List<Integer> getColors(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentTypes.COLORS, List.of(defaultColor));
    }

    public int getDefaultColor(int index) {
        if (layerIsDyeable(index) && index == 0) {
            return defaultColor;
        }
        return -1;
    }


    public void setDefaultColor(ItemStack stack, int index) {
        setColor(stack, index, getDefaultColor(index));
        setLayerIsDyed(stack, index, false);
    }

    public void setColor(ItemStack stack, int index, Integer color) {
        List<Integer> colors = List.of(color);
        this.setLayerIsDyed(stack, index, true);

        //save to nbt
        stack.set(ModDataComponentTypes.COLORS, colors);
    }

    public void setLayerIsDyed(ItemStack stack, int index, boolean bool) {
        if (index != 0) {
            return;
        }

        //save to nbt
        stack.set(ModDataComponentTypes.IS_DYED, List.of(bool));
    }

    public boolean layerIsDyeable(int index) {
        return index == 0;
    }

    public boolean layerIsDyed(ItemStack stack, int index) {
        if (this.layerIsDyeable(index)) {
            List<Boolean> colors = stack.get(ModDataComponentTypes.IS_DYED);
            if (colors != null && colors.size() > index) {
                return colors.get(index);
            } else {
                return false;
            }
        }
        return false;
    }
}
