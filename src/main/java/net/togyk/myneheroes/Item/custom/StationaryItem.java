package net.togyk.myneheroes.Item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public interface StationaryItem {
    default boolean tryPickup(PlayerEntity player, Hand hand) {
        return true;
    }
    default boolean hasToBeOwnerToPickup(ItemStack stack) {
        return false;
    }
}
