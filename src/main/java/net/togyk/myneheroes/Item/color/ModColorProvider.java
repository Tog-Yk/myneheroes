package net.togyk.myneheroes.Item.color;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.item.Item;
import net.togyk.myneheroes.Item.ModItems;
import net.togyk.myneheroes.Item.custom.DyeableAdvancedArmorItem;

public class ModColorProvider {
    private static void registerDyeableArmorItem(Item item){
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (stack.getItem() instanceof DyeableAdvancedArmorItem dyeableAdvancedArmorItem) { // Tint only layer 1
                return dyeableAdvancedArmorItem.getColor(stack, tintIndex);
            }
            return 0xFFFFFF; // Default white for non-tinted layers
        }, item);
    }

    public static void registerColorProviders(){
        registerDyeableArmorItem(ModItems.MARK6_VIBRANIUM_HELMET);
        registerDyeableArmorItem(ModItems.MARK6_VIBRANIUM_CHESTPLATE);
        registerDyeableArmorItem(ModItems.MARK6_VIBRANIUM_LEGGINGS);
        registerDyeableArmorItem(ModItems.MARK6_VIBRANIUM_BOOTS);
    }
}
