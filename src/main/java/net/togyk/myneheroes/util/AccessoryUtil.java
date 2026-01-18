package net.togyk.myneheroes.util;

import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.slot.SlotEntryReference;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AccessoryUtil {
    public static List<ItemStack> getAccessoryItems(PlayerEntity player) {
        AccessoriesCapability capability = AccessoriesCapability.get(player);
        if (capability != null) {
            return capability.getAllEquipped().stream().map(SlotEntryReference::stack).toList();
        }
        return new ArrayList<>();
    }
}
