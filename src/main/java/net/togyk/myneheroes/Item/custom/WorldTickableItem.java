package net.togyk.myneheroes.Item.custom;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface WorldTickableItem {
    void worldTick(ItemStack stack, World world, ItemEntity entity);
}
