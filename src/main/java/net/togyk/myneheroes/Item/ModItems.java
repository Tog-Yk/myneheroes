package net.togyk.myneheroes.Item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.*;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.ShootLaserAbilityFromReactor;

import java.util.List;

public class ModItems {
    public static final Item VIBRANIUM_INGOT = registerItem("vibranium_ingot", new Item(new Item.Settings()));
    public static final Item RAW_VIBRANIUM = registerItem("raw_vibranium", new Item(new Item.Settings()));

    public static final Item TITANIUM_INGOT = registerItem("titanium_ingot", new Item(new Item.Settings()));
    public static final Item RAW_TITANIUM = registerItem("raw_titanium", new Item(new Item.Settings()));

    public static final Item GOLD_TITANIUM_INGOT = registerItem("gold_titanium_ingot", new Item(new Item.Settings()));
    public static final Item RAW_GOLD_TITANIUM = registerItem("raw_gold_titanium", new Item(new Item.Settings()));

    public static final Item IRON_SUIT_TEMPLATE = registerItem("iron_suit_template",new Item(new Item.Settings()));

    public static final Item ARC_REACTOR = registerItem("arc_reactor",new ReactorItem(4000, 2400, new Item.Settings().maxCount(1)));

    public static final Item ABILITY_HOLDING = registerItem("ability_holding",new AbilityHoldingItem(new Ability("ability",100), new Item.Settings().maxCount(1)));
    public static final Item LAZAR_HOLDING = registerItem("lazar_holding",new AbilityHoldingItem(new ShootLaserAbilityFromReactor("lazar",10), new Item.Settings().maxCount(1)));

    public static final Item POWER_INJECTION = registerItem("power_injection", new PowerInjectionItem(new Item.Settings().maxCount(1)));

    //armors
    //vibranium armors
    public static final Item VIBRANIUM_HELMET = registerItem("vibranium_helmet",
            new ArmorItem(ModArmorMaterials.VIBRANIUM_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings()
                    .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(15))));
    public static final Item VIBRANIUM_CHESTPLATE = registerItem("vibranium_chestplate",
            new ArmorItem(ModArmorMaterials.VIBRANIUM_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()
                    .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(15))));
    public static final Item VIBRANIUM_LEGGINGS = registerItem("vibranium_leggings",
            new ArmorItem(ModArmorMaterials.VIBRANIUM_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(15))));
    public static final Item VIBRANIUM_BOOTS = registerItem("vibranium_boots",
            new ArmorItem(ModArmorMaterials.VIBRANIUM_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(15))));

    //mark6 armors
    public static final Item MARK6_VIBRANIUM_HELMET = registerItem("mark6_vibranium_helmet",
            new DyeableAdvancedArmorItem(List.of(-12696503, -11241349, -1443585), List.of(-1, -1, 15), null, ModArmorMaterials.VIBRANIUM_IRONMAN_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings()
                    .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(15))));
    public static final Item MARK6_VIBRANIUM_CHESTPLATE = registerItem("mark6_vibranium_chestplate",
            new DyeableAdvancedArmorItem(List.of(-12696503, -11241349, -1443585), List.of(-1, -1, 15), null, ModArmorMaterials.VIBRANIUM_IRONMAN_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()
                    .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(15))));
    public static final Item MARK6_VIBRANIUM_LEGGINGS = registerItem("mark6_vibranium_leggings",
            new DyeableAdvancedArmorItem(List.of(-12696503, -11241349, -1443585), List.of(-1, -1, 15), null, ModArmorMaterials.VIBRANIUM_IRONMAN_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(15))));
    public static final Item MARK6_VIBRANIUM_BOOTS = registerItem("mark6_vibranium_boots",
            new DyeableAdvancedArmorItem(List.of(-12696503, -11241349, -1443585), List.of(-1, -1, 15), null, ModArmorMaterials.VIBRANIUM_IRONMAN_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(15))));


    //titanium armors
    public static final Item TITANIUM_HELMET = registerItem("titanium_helmet",
            new ArmorItem(ModArmorMaterials.TITANIUM_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings()
                    .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(15))));
    public static final Item TITANIUM_CHESTPLATE = registerItem("titanium_chestplate",
            new ArmorItem(ModArmorMaterials.TITANIUM_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()
                    .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(15))));
    public static final Item TITANIUM_LEGGINGS = registerItem("titanium_leggings",
            new ArmorItem(ModArmorMaterials.TITANIUM_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(15))));
    public static final Item TITANIUM_BOOTS = registerItem("titanium_boots",
            new ArmorItem(ModArmorMaterials.TITANIUM_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(15))));


    //titanium armors
    public static final Item GOLD_TITANIUM_HELMET = registerItem("gold_titanium_helmet",
            new ArmorItem(ModArmorMaterials.GOLD_TITANIUM_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings()
                    .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(15))));
    public static final Item GOLD_TITANIUM_CHESTPLATE = registerItem("gold_titanium_chestplate",
            new ArmorItem(ModArmorMaterials.GOLD_TITANIUM_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()
                    .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(15))));
    public static final Item GOLD_TITANIUM_LEGGINGS = registerItem("gold_titanium_leggings",
            new ArmorItem(ModArmorMaterials.GOLD_TITANIUM_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(15))));
    public static final Item GOLD_TITANIUM_BOOTS = registerItem("gold_titanium_boots",
            new ArmorItem(ModArmorMaterials.GOLD_TITANIUM_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(15))));


    public static final Item VIBRANIUM_SHIELD = registerItem("vibranium_shield",
            new ThrowableShieldItem(2.0F,new Item.Settings().maxDamage(336)));

    public static final Item A_SYMBOLS_SHIELD = registerItem("a_symbols_shield",
            new ThrowableShieldItem(2.0F,new Item.Settings().maxDamage(336)));

    public static final Item COSMIC_SHIELD = registerItem("cosmic_shield",
            new ThrowableShieldItem(2.0F,new Item.Settings().maxDamage(336)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(MyneHeroes.MOD_ID, name), item);
    }

    public static void registerModItems() {
        MyneHeroes.LOGGER.info("Registering Mod Items for " + MyneHeroes.MOD_ID);

    }
}