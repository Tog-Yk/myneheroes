package net.togyk.myneheroes.Item.custom;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.togyk.myneheroes.entity.StationaryItemEntity;

public interface StationaryItem {
    default boolean tryPickup(PlayerEntity player, Hand hand) {
        return true;
    }
    default boolean hasToBeOwnerToPickup(ItemStack stack) {
        return false;
    }

    default StationaryItemEntity createEntity(ItemEntity entity, ItemStack stack) {
        StationaryItemEntity newItemEntity = new StationaryItemEntity(entity.getWorld());
        newItemEntity.setPos(entity.getX(), entity.getY(), entity.getZ());
        if (entity.getOwner() != null) {
            newItemEntity.setOwner(entity.getOwner());
        }
        newItemEntity.setItem(stack);
        newItemEntity.setVelocity(entity.getVelocity());
        return newItemEntity;
    }
}
