package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface LightableItem {

    List<Integer> getLightLevels(ItemStack stack);

    int getLightLevel(ItemStack stack, int index);
    int getDefaultLightLevel(int index);
    void setLightLevel(ItemStack stack, int index, int lightLevel);
    boolean layerIsLightable(int index);
    boolean layerIsLightable(ItemStack stack, int index);
}