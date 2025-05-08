package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface DyeableItem {

    List<Integer> getColors(ItemStack stack);

    int getDefaultColor(int index);
    void setDefaultColor(ItemStack stack, int index);

    int getColor(ItemStack stack, int index);
    void setColor(ItemStack stack, int index, Integer color);
    void setLayerIsDyed(ItemStack stack, int index, boolean bool);
    boolean layerIsDyeable(int index);
    boolean layerIsDyed(ItemStack stack, int index);
}
