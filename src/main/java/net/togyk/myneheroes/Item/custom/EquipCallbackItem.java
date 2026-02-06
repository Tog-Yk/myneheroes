package net.togyk.myneheroes.Item.custom;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface EquipCallbackItem {
    default void onEquipped(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {}
    default void onUnequipped(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {}
}
