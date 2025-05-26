package net.togyk.myneheroes.Item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.*;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Abilities;

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

    public static final Item POWER_INJECTION = registerItem("power_injection", new PowerInjectionItem(new Item.Settings().maxCount(1)));

    public static final Item VIBRANIUM_SHIELD = registerItem("vibranium_shield",
            new ThrowableShieldItem(2.0F,new Item.Settings().maxDamage(336)));

    public static final Item A_SYMBOLS_SHIELD = registerItem("a_symbols_shield",
            new ThrowableShieldItem(2.0F,new Item.Settings().maxDamage(336)));

    public static final Item COSMIC_SHIELD = registerItem("cosmic_shield",
            new ThrowableShieldItem(2.0F,new Item.Settings().maxDamage(336)));

    public static final Item CARTERS_SHIELD = registerItem("carters_shield",
            new ThrowableShieldItem(2.0F,new Item.Settings().maxDamage(336)));

    public static final Item CIRCUIT_BOARD = registerItem("circuit_board",
            new Item(new Item.Settings()));

    public static final Item HUD_UPGRADE = registerItem("hud_upgrade", new UpgradeItem(Abilities.TOGGLE_HUD, 4, ArmorItem.Type.HELMET, new Item.Settings()));
    public static final Item LAZAR_UPGRADE = registerItem("lazar_upgrade", new UpgradeItem(Abilities.SHOOT_LAZAR, 6, ArmorItem.Type.CHESTPLATE, new Item.Settings()));
    public static final Item FlY_UPGRADE = registerItem("fly_upgrade", new UpgradeItem(Abilities.ALLOW_FLYING, 6, ArmorItem.Type.BOOTS, new Item.Settings()));
    public static final Item KINETIC_ENERGY_STORAGE_UPGRADE = registerItem("kinetic_energy_storage_upgrade", new UpgradeItem(Abilities.RELEASE_KINETIC_ENERGY, 3, null, new Item.Settings()));
    public static final Item TAKE_OFF_SUIT_UPGRADE = registerItem("take_off_suit_upgrade", new UpgradeItem(Abilities.TAKE_OFF_SUIT, 4, null, new Item.Settings()));
    public static final Item TOOLBELT_3_UPGRADE = registerItem("toolbelt_3_upgrade", new UpgradeWithTooltipItem(Abilities.TOOLBELT_3, 4, ArmorItem.Type.LEGGINGS, Text.translatable("item.myneheroes.toolbelt_3_upgrade.size", 3), new Item.Settings()));

    //armors
    //vibranium armors
    public static final Item VIBRANIUM_HELMET = registerItem("vibranium_helmet",
            new ArmorItem(ModArmorMaterials.VIBRANIUM_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings()
                    .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(35))));
    public static final Item VIBRANIUM_CHESTPLATE = registerItem("vibranium_chestplate",
            new ArmorItem(ModArmorMaterials.VIBRANIUM_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()
                    .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(35))));
    public static final Item VIBRANIUM_LEGGINGS = registerItem("vibranium_leggings",
            new ArmorItem(ModArmorMaterials.VIBRANIUM_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(35))));
    public static final Item VIBRANIUM_BOOTS = registerItem("vibranium_boots",
            new ArmorItem(ModArmorMaterials.VIBRANIUM_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(35))));

    //mark6 armors
    public static Item MARK6_VIBRANIUM_HELMET = registerItem("mark6_vibranium_helmet",
            new DyeableAdvancedArmorItem(List.of(-12696503, -11241349, -0x574545, -1443585), List.of(-1, -1, -1, 15), null, ModArmorMaterials.VIBRANIUM_IRONMAN_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings()
                    .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(37))));
    public static Item MARK6_VIBRANIUM_CHESTPLATE = registerItem("mark6_vibranium_chestplate",
            new DyeableAdvancedArmorItem(List.of(-12696503, -11241349, -0x574545, -1443585), List.of(-1, -1, -1, 15), null, ModArmorMaterials.VIBRANIUM_IRONMAN_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()
                    .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(37))));
    public static Item MARK6_VIBRANIUM_LEGGINGS = registerItem("mark6_vibranium_leggings",
            new DyeableAdvancedArmorItem(List.of(-12696503, -11241349, -0x574545, -1443585), List.of(-1, -1, -1, 15), null, ModArmorMaterials.VIBRANIUM_IRONMAN_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(37))));
    public static Item MARK6_VIBRANIUM_BOOTS = registerItem("mark6_vibranium_boots",
            new DyeableAdvancedArmorItem(List.of(-12696503, -11241349, -0x574545, -1443585), List.of(-1, -1, -1, 15), null, ModArmorMaterials.VIBRANIUM_IRONMAN_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(37))));


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


    //gold titanium armors
    public static final Item GOLD_TITANIUM_HELMET = registerItem("gold_titanium_helmet",
            new ArmorItem(ModArmorMaterials.GOLD_TITANIUM_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings()
                    .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(29))));
    public static final Item GOLD_TITANIUM_CHESTPLATE = registerItem("gold_titanium_chestplate",
            new ArmorItem(ModArmorMaterials.GOLD_TITANIUM_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()
                    .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(29))));
    public static final Item GOLD_TITANIUM_LEGGINGS = registerItem("gold_titanium_leggings",
            new ArmorItem(ModArmorMaterials.GOLD_TITANIUM_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(29))));
    public static final Item GOLD_TITANIUM_BOOTS = registerItem("gold_titanium_boots",
            new ArmorItem(ModArmorMaterials.GOLD_TITANIUM_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(29))));

    //mark6 armors
    public static Item MARK3_GOLD_TITANIUM_HELMET = registerItem("mark3_gold_titanium_helmet",
            new DyeableAdvancedArmorItem(List.of(0xFF976A27, 0xffd0a75e, 0xff6e5959, -1443585), List.of(-1, -1, -1, 15), null, ModArmorMaterials.GOLD_TITANIUM_IRONMAN_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings()
                    .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(31))));
    public static Item MARK3_GOLD_TITANIUM_CHESTPLATE = registerItem("mark3_gold_titanium_chestplate",
            new DyeableAdvancedArmorItem(List.of(0xFF976A27, 0xffd0a75e, 0xff6e5959, -1443585), List.of(-1, -1, -1, 15), null, ModArmorMaterials.GOLD_TITANIUM_IRONMAN_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()
                    .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(31))));
    public static Item MARK3_GOLD_TITANIUM_LEGGINGS = registerItem("mark3_gold_titanium_leggings",
            new DyeableAdvancedArmorItem(List.of(0xFF976A27, 0xffd0a75e, 0xff6e5959, -1443585), List.of(-1, -1, -1, 15), null, ModArmorMaterials.GOLD_TITANIUM_IRONMAN_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(31))));
    public static Item MARK3_GOLD_TITANIUM_BOOTS = registerItem("mark3_gold_titanium_boots",
            new DyeableAdvancedArmorItem(List.of(0xFF976A27, 0xffd0a75e, 0xff6e5959, -1443585), List.of(-1, -1, -1, 15), null, ModArmorMaterials.GOLD_TITANIUM_IRONMAN_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(31))));
    //mark45 armors
    public static Item MARK45_NETHERITE_HELMET = registerItem("mark45_netherite_helmet",
            new DyeableAdvancedArmorItem(List.of(0xFF49393f, 0xff5d565d, 0xff6e5959, -1443585), List.of(-1, -1, -1, 15), null, ModArmorMaterials.NETHERITE_IRONMAN_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings()
                    .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(39)).fireproof()));
    public static Item MARK45_NETHERITE_CHESTPLATE = registerItem("mark45_netherite_chestplate",
            new DyeableAdvancedArmorItem(List.of(0xFF49393f, 0xff5d565d, 0xff6e5959, -1443585), List.of(-1, -1, -1, 15), null, ModArmorMaterials.NETHERITE_IRONMAN_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()
                    .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(39)).fireproof()));
    public static Item MARK45_NETHERITE_LEGGINGS = registerItem("mark45_netherite_leggings",
            new DyeableAdvancedArmorItem(List.of(0xFF49393f, 0xff5d565d, 0xff6e5959, -1443585), List.of(-1, -1, -1, 15), null, ModArmorMaterials.NETHERITE_IRONMAN_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(39)).fireproof()));
    public static Item MARK45_NETHERITE_BOOTS = registerItem("mark45_netherite_boots",
            new DyeableAdvancedArmorItem(List.of(0xFF49393f, 0xff5d565d, 0xff6e5959, -1443585), List.of(-1, -1, -1, 15), null, ModArmorMaterials.NETHERITE_IRONMAN_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(39)).fireproof()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(MyneHeroes.MOD_ID, name), item);
    }

    public static void registerModItems() {
        MyneHeroes.LOGGER.info("Registering Mod Items for " + MyneHeroes.MOD_ID);
    }
}