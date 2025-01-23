package net.togyk.myneheroes.Item.color;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.item.Item;
import net.togyk.myneheroes.Item.ModItems;
import net.togyk.myneheroes.Item.custom.DyeableAdvancedArmorItem;
import net.togyk.myneheroes.Item.custom.PowerInjectionItem;
import net.togyk.myneheroes.power.Power;

public class ModColorProvider {
    private static void registerDyeableArmorItem(Item item){
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (stack.getItem() instanceof DyeableAdvancedArmorItem dyeableAdvancedArmorItem) { // Tint only layer 1
                return dyeableAdvancedArmorItem.getColor(stack, tintIndex);
            }
            return 0xFFFFFFFF; // Default white for non-tinted layers
        }, item);
    }
    private static void registerInjectionItem(Item item){
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (stack.getItem() instanceof PowerInjectionItem injectionItem && tintIndex == 1) { // Tint only layer 1
                Power power = injectionItem.getPower(stack);
                if (power != null) {
                    return power.getColor();
                } else {
                    return 0x00FFFFFF;
                }
            }
            return 0xFFBBBBBB; // Default gray for non-tinted layers
        }, item);
    }

    public static void registerColorProviders(){
        registerDyeableArmorItem(ModItems.MARK6_VIBRANIUM_HELMET);
        registerDyeableArmorItem(ModItems.MARK6_VIBRANIUM_CHESTPLATE);
        registerDyeableArmorItem(ModItems.MARK6_VIBRANIUM_LEGGINGS);
        registerDyeableArmorItem(ModItems.MARK6_VIBRANIUM_BOOTS);

        registerInjectionItem(ModItems.POWER_INJECTION);
    }
}
