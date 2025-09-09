package net.togyk.myneheroes.Item.color;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.item.Item;
import net.togyk.myneheroes.Item.ModItems;
import net.togyk.myneheroes.Item.custom.DyeableItem;
import net.togyk.myneheroes.Item.custom.PowerInjectionItem;
import net.togyk.myneheroes.Item.custom.SimpleDyeableItem;
import net.togyk.myneheroes.power.Power;

public class ModColorProvider {
    private static void registerDyeableArmorItem(Item item){
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (stack.getItem() instanceof DyeableItem dyeableItem) { // Tint only layer 1
                return dyeableItem.getColor(stack, tintIndex);
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
            return 0xFFFFFFFF;
        }, item);
    }

    private static void registerSimpleDyeableItem(Item item){
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (stack.getItem() instanceof SimpleDyeableItem dyeableItem && tintIndex == 1) { // Tint only layer 1
                if (dyeableItem.layerIsDyed(stack, 0)) {
                    return dyeableItem.getColor(stack, 0);
                }
                return 0x00FFFFFF;
            }
            return 0xFFFFFFFF;
        }, item);
    }

    public static void registerColorProviders(){
        registerDyeableArmorItem(ModItems.MARK6_VIBRANIUM_HELMET);
        registerDyeableArmorItem(ModItems.MARK6_VIBRANIUM_CHESTPLATE);
        registerDyeableArmorItem(ModItems.MARK6_VIBRANIUM_LEGGINGS);
        registerDyeableArmorItem(ModItems.MARK6_VIBRANIUM_BOOTS);

        registerDyeableArmorItem(ModItems.MARK3_GOLD_TITANIUM_HELMET);
        registerDyeableArmorItem(ModItems.MARK3_GOLD_TITANIUM_CHESTPLATE);
        registerDyeableArmorItem(ModItems.MARK3_GOLD_TITANIUM_LEGGINGS);
        registerDyeableArmorItem(ModItems.MARK3_GOLD_TITANIUM_BOOTS);

        registerDyeableArmorItem(ModItems.MARK45_NETHERITE_HELMET);
        registerDyeableArmorItem(ModItems.MARK45_NETHERITE_CHESTPLATE);
        registerDyeableArmorItem(ModItems.MARK45_NETHERITE_LEGGINGS);
        registerDyeableArmorItem(ModItems.MARK45_NETHERITE_BOOTS);

        registerDyeableArmorItem(ModItems.SPEEDSTER_VIBRANIUM_HELMET);
        registerDyeableArmorItem(ModItems.SPEEDSTER_VIBRANIUM_CHESTPLATE);
        registerDyeableArmorItem(ModItems.SPEEDSTER_VIBRANIUM_LEGGINGS);
        registerDyeableArmorItem(ModItems.SPEEDSTER_VIBRANIUM_BOOTS);

        registerDyeableArmorItem(ModItems.SPEEDSTER_GOLD_TITANIUM_HELMET);
        registerDyeableArmorItem(ModItems.SPEEDSTER_GOLD_TITANIUM_CHESTPLATE);
        registerDyeableArmorItem(ModItems.SPEEDSTER_GOLD_TITANIUM_LEGGINGS);
        registerDyeableArmorItem(ModItems.SPEEDSTER_GOLD_TITANIUM_BOOTS);

        registerDyeableArmorItem(ModItems.SPEEDSTER_NETHERITE_HELMET);
        registerDyeableArmorItem(ModItems.SPEEDSTER_NETHERITE_CHESTPLATE);
        registerDyeableArmorItem(ModItems.SPEEDSTER_NETHERITE_LEGGINGS);
        registerDyeableArmorItem(ModItems.SPEEDSTER_NETHERITE_BOOTS);

        registerInjectionItem(ModItems.POWER_INJECTION);

        registerSimpleDyeableItem(ModItems.COLORING_COMPOUND);
    }
}
