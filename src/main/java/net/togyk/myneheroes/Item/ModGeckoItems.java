package net.togyk.myneheroes.Item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.DyeableAdvancedArmorItem;
import net.togyk.myneheroes.Item.custom.DyeableAdvancedGeckoArmorItem;
import net.togyk.myneheroes.MyneHeroes;

import java.util.List;

public class ModGeckoItems {

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(MyneHeroes.MOD_ID, name), item);
    }

    public static void registerModItems() {
    }

    static {
        ModItems.MARK6_VIBRANIUM_HELMET = registerItem("mark6_vibranium_helmet",
                new DyeableAdvancedGeckoArmorItem(List.of(-12696503, -11241349, -0x574545, -1443585), List.of(-1, -1, -1, 15), null, ModArmorMaterials.VIBRANIUM_IRONMAN_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings()
                        .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(15))));
        ModItems.MARK6_VIBRANIUM_CHESTPLATE = registerItem("mark6_vibranium_chestplate",
                new DyeableAdvancedGeckoArmorItem(List.of(-12696503, -11241349, -0x574545, -1443585), List.of(-1, -1, -1, 15), null, ModArmorMaterials.VIBRANIUM_IRONMAN_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()
                        .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(15))));
        ModItems.MARK6_VIBRANIUM_LEGGINGS = registerItem("mark6_vibranium_leggings",
                new DyeableAdvancedGeckoArmorItem(List.of(-12696503, -11241349, -0x574545, -1443585), List.of(-1, -1, -1, 15), null, ModArmorMaterials.VIBRANIUM_IRONMAN_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()
                        .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(15))));
        ModItems.MARK6_VIBRANIUM_BOOTS = registerItem("mark6_vibranium_boots",
                new DyeableAdvancedGeckoArmorItem(List.of(-12696503, -11241349, -0x574545, -1443585), List.of(-1, -1, -1, 15), null, ModArmorMaterials.VIBRANIUM_IRONMAN_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()
                        .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(15))));

        ModItems.MARK3_GOLD_TITANIUM_HELMET = registerItem("mark3_gold_titanium_helmet",
                new DyeableAdvancedGeckoArmorItem(List.of(0xFF976A27, 0xffd0a75e, 0xff6e5959, -1443585), List.of(-1, -1, -1, 15), null, ModArmorMaterials.GOLD_TITANIUM_IRONMAN_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings()
                        .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(15))));
        ModItems.MARK3_GOLD_TITANIUM_CHESTPLATE = registerItem("mark3_gold_titanium_chestplate",
                new DyeableAdvancedGeckoArmorItem(List.of(0xFF976A27, 0xffd0a75e, 0xff6e5959, -1443585), List.of(-1, -1, -1, 15), null, ModArmorMaterials.GOLD_TITANIUM_IRONMAN_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()
                        .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(15))));
        ModItems.MARK3_GOLD_TITANIUM_LEGGINGS = registerItem("mark3_gold_titanium_leggings",
                new DyeableAdvancedGeckoArmorItem(List.of(0xFF976A27, 0xffd0a75e, 0xff6e5959, -1443585), List.of(-1, -1, -1, 15), null, ModArmorMaterials.GOLD_TITANIUM_IRONMAN_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()
                        .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(15))));
        ModItems.MARK3_GOLD_TITANIUM_BOOTS = registerItem("mark3_gold_titanium_boots",
                new DyeableAdvancedGeckoArmorItem(List.of(0xFF976A27, 0xffd0a75e, 0xff6e5959, -1443585), List.of(-1, -1, -1, 15), null, ModArmorMaterials.GOLD_TITANIUM_IRONMAN_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()
                        .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(15))));
    }
}