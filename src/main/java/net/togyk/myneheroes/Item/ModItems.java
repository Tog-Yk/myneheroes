package net.togyk.myneheroes.Item;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.togyk.myneheroes.Item.custom.*;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Abilities;
import net.togyk.myneheroes.upgrade.Upgrades;

import java.lang.reflect.Constructor;
import java.util.List;

public class ModItems {
    public static final Item VIBRANIUM_INGOT = registerItem("vibranium_ingot", new Item(new Item.Settings()));
    public static final Item RAW_VIBRANIUM = registerItem("raw_vibranium", new Item(new Item.Settings()));

    public static final Item TITANIUM_INGOT = registerItem("titanium_ingot", new Item(new Item.Settings()));
    public static final Item RAW_TITANIUM = registerItem("raw_titanium", new Item(new Item.Settings()));

    public static final Item GOLD_TITANIUM_INGOT = registerItem("gold_titanium_ingot", new Item(new Item.Settings()));
    public static final Item RAW_GOLD_TITANIUM = registerItem("raw_gold_titanium", new Item(new Item.Settings()));

    public static final Item URANIUM_INGOT = registerItem("uranium_ingot", new RadiationItem(new Item.Settings()));
    public static final Item RAW_URANIUM = registerItem("raw_uranium", new RadiationItem(new Item.Settings()));

    public static final Item IRON_SUIT_TEMPLATE = registerItem("iron_suit_template", new ModSmithingTemplateItem(true, Identifier.of(MyneHeroes.MOD_ID, "iron_suit"), new Item.Settings()));
    public static final Item SPEEDSTER_SUIT_TEMPLATE = registerItem("speedster_suit_template", new ModSmithingTemplateItem(true, Identifier.of(MyneHeroes.MOD_ID, "speedster_suit"), new Item.Settings()));

    public static final Item ARC_REACTOR = registerItem("arc_reactor",new ReactorItem(4000, 3200, 1600, new Item.Settings().maxCount(1)));

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

    public static final Item WEB_FLUID = registerItem("web_fluid",
            new Item(new Item.Settings()));

    public static final Item BOTTLE_OF_SPIDER_VENOM = registerItem("bottle_of_spider_venom",
            new PoisonDrinkItem(new Item.Settings().maxCount(1).recipeRemainder(Items.GLASS_BOTTLE)));

    public static final Item BOTTLE_OF_RADIOACTIVE_SPIDER_VENOM = registerItem("bottle_of_radioactive_spider_venom",
            new SpiderManPowerDrink(List.of(StatusEffects.NAUSEA), new Item.Settings().maxCount(1).recipeRemainder(Items.GLASS_BOTTLE)));

    public static final Item MECHANICAL_HUD_UPGRADE = registerItem("mechanical_hud_upgrade", new UpgradeWithTooltipItem(Upgrades.MECHANICAL_HUD, Text.literal("mechanical").setStyle(Style.EMPTY.withColor(0xC428EEFF)), new Item.Settings()));
    public static final Item SPEEDSTER_HUD_UPGRADE = registerItem("speedster_hud_upgrade", new UpgradeWithTooltipItem(Upgrades.SPEEDSTER_HUD, Text.literal("speedster").setStyle(Style.EMPTY.withColor(0xC4FFEB28)), new Item.Settings()));
    public static final Item LASER_UPGRADE = registerItem("laser_upgrade", new UpgradeItem(Upgrades.LASER, new Item.Settings()));
    public static final Item FlY_UPGRADE = registerItem("fly_upgrade", new UpgradeItem(Upgrades.FlY, new Item.Settings()));
    public static final Item KINETIC_ENERGY_STORAGE_UPGRADE = registerItem("kinetic_energy_storage_upgrade", new UpgradeItem(Upgrades.KINETIC_ENERGY_STORAGE, new Item.Settings()));
    public static final Item TAKE_OFF_SUIT_UPGRADE = registerItem("take_off_suit_upgrade", new UpgradeItem(Upgrades.TAKE_OFF_SUIT, new Item.Settings()));
    public static final Item TOOLBELT_3_UPGRADE = registerItem("toolbelt_3_upgrade", new UpgradeWithTooltipItem(Upgrades.TOOLBELT_3, Text.translatable("item.myneheroes.toolbelt_3_upgrade.size", 3), new Item.Settings().maxCount(1)));

    public static final Item WEB_SHOOTER_UPGRADE = registerItem("web_shooter_upgrade", new AbilityHoldingUpgradeItem(Upgrades.WEB_SHOOTER, new Item.Settings().maxCount(1)));

    public static final Item COLORING_COMPOUND = registerItem("coloring_compound",
            new SimpleDyeableItem(-1, new Item.Settings()));

    public static final Item HEART_SHAPED_HERB_MIX = registerItem("heart_shaped_herb_mix",
            new ReversablePowerDrinkItem(Identifier.of(MyneHeroes.MOD_ID, "avatar_of_bast"), List.of(), new Item.Settings().recipeRemainder(Items.BOWL).maxCount(1)));

    public static final Item BONE_CLAWS = registerItem("bone_claws",
            new TemporaryWeapon(new Item.Settings().maxCount(1).attributeModifiers(TemporaryWeapon.createAttributeModifiers(4, -1.2F))));
    public static final Item ADAMANTIUM_CLAWS = registerItem("adamantium_claws",
            new TemporaryWeapon(new Item.Settings().maxCount(1).attributeModifiers(TemporaryWeapon.createAttributeModifiers(4.5F, -1.2F))));

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
    public static Item MARK6_VIBRANIUM_HELMET = null;
    public static Item MARK6_VIBRANIUM_CHESTPLATE = null;
    public static Item MARK6_VIBRANIUM_LEGGINGS = null;
    public static Item MARK6_VIBRANIUM_BOOTS = null;
    //mark3 armors
    public static Item MARK3_GOLD_TITANIUM_HELMET = null;
    public static Item MARK3_GOLD_TITANIUM_CHESTPLATE = null;
    public static Item MARK3_GOLD_TITANIUM_LEGGINGS = null;
    public static Item MARK3_GOLD_TITANIUM_BOOTS = null;
    //mark45 armors
    public static Item MARK45_NETHERITE_HELMET = null;
    public static Item MARK45_NETHERITE_CHESTPLATE = null;
    public static Item MARK45_NETHERITE_LEGGINGS = null;
    public static Item MARK45_NETHERITE_BOOTS = null;

    //speedster armors
    //vibranium
    public static Item SPEEDSTER_VIBRANIUM_HELMET = null;
    public static Item SPEEDSTER_VIBRANIUM_CHESTPLATE = null;
    public static Item SPEEDSTER_VIBRANIUM_LEGGINGS = null;
    public static Item SPEEDSTER_VIBRANIUM_BOOTS = null;
    //gold_titanium
    public static Item SPEEDSTER_GOLD_TITANIUM_HELMET = null;
    public static Item SPEEDSTER_GOLD_TITANIUM_CHESTPLATE = null;
    public static Item SPEEDSTER_GOLD_TITANIUM_LEGGINGS = null;
    public static Item SPEEDSTER_GOLD_TITANIUM_BOOTS = null;
    //netherite
    public static Item SPEEDSTER_NETHERITE_HELMET = null;
    public static Item SPEEDSTER_NETHERITE_CHESTPLATE = null;
    public static Item SPEEDSTER_NETHERITE_LEGGINGS = null;
    public static Item SPEEDSTER_NETHERITE_BOOTS = null;

    //tutorial items
    public static Item CLARK_KENT_GLASSES = registerItem("clark_kent_glasses",
            new TutorialItem(new Item.Settings().maxCount(1), Abilities.TUTOR_HUD.copy()));

    static {
        try {
            Constructor<?> AdvancedArmorClassConstructor = null;

            if (FabricLoader.getInstance().isModLoaded("geckolib")) {
                try {
                    Class<?> AdvancedArmorClass = Class.forName("net.togyk.myneheroes.Item.custom.DyeableAdvancedGeckoArmorItem");
                    AdvancedArmorClassConstructor = AdvancedArmorClass
                            .getConstructor(List.class, List.class, Text.class, RegistryEntry.class, ArmorItem.Type.class, Item.Settings.class);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    AdvancedArmorClassConstructor = DyeableAdvancedArmorItem.class
                            .getConstructor(List.class, List.class, Text.class, RegistryEntry.class, ArmorItem.Type.class, Item.Settings.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (AdvancedArmorClassConstructor != null) {
                //mark6 armors
                MARK6_VIBRANIUM_HELMET = registerItem("mark6_vibranium_helmet",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(-12696503, -11241349, -0x574545, -1443585), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "mark6_vibranium"))).setStyle(Style.EMPTY.withColor(-11241349)), ModArmorMaterials.VIBRANIUM_IRONMAN_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings()
                                .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(37))));
                MARK6_VIBRANIUM_CHESTPLATE = registerItem("mark6_vibranium_chestplate",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(-12696503, -11241349, -0x574545, -1443585), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "mark6_vibranium"))).setStyle(Style.EMPTY.withColor(-11241349)), ModArmorMaterials.VIBRANIUM_IRONMAN_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()
                                .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(37))));
                MARK6_VIBRANIUM_LEGGINGS = registerItem("mark6_vibranium_leggings",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(-12696503, -11241349, -0x574545, -1443585), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "mark6_vibranium"))).setStyle(Style.EMPTY.withColor(-11241349)), ModArmorMaterials.VIBRANIUM_IRONMAN_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()
                                .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(37))));
                MARK6_VIBRANIUM_BOOTS = registerItem("mark6_vibranium_boots",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(-12696503, -11241349, -0x574545, -1443585), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "mark6_vibranium"))).setStyle(Style.EMPTY.withColor(-11241349)), ModArmorMaterials.VIBRANIUM_IRONMAN_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()
                                .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(37))));
                //mark6 armors
                MARK3_GOLD_TITANIUM_HELMET = registerItem("mark3_gold_titanium_helmet",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(0xFF976A27, 0xffd0a75e, 0xff6e5959, -1443585), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "mark3_gold_titanium"))).formatted(Formatting.GOLD), ModArmorMaterials.GOLD_TITANIUM_IRONMAN_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings()
                                .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(31))));
                MARK3_GOLD_TITANIUM_CHESTPLATE = registerItem("mark3_gold_titanium_chestplate",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(0xFF976A27, 0xffd0a75e, 0xff6e5959, -1443585), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "mark3_gold_titanium"))).formatted(Formatting.GOLD), ModArmorMaterials.GOLD_TITANIUM_IRONMAN_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()
                                .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(31))));
                MARK3_GOLD_TITANIUM_LEGGINGS = registerItem("mark3_gold_titanium_leggings",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(0xFF976A27, 0xffd0a75e, 0xff6e5959, -1443585), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "mark3_gold_titanium"))).formatted(Formatting.GOLD), ModArmorMaterials.GOLD_TITANIUM_IRONMAN_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()
                                .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(31))));
                MARK3_GOLD_TITANIUM_BOOTS = registerItem("mark3_gold_titanium_boots",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(0xFF976A27, 0xffd0a75e, 0xff6e5959, -1443585), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "mark3_gold_titanium"))).formatted(Formatting.GOLD), ModArmorMaterials.GOLD_TITANIUM_IRONMAN_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()
                                .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(31))));
                //mark45 armors
                MARK45_NETHERITE_HELMET = registerItem("mark45_netherite_helmet",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(0xFF49393f, 0xff5d565d, 0xff6e5959, -1443585), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "mark45_netherite"))).setStyle(Style.EMPTY.withColor(0xFF49393f)), ModArmorMaterials.NETHERITE_IRONMAN_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings()
                                .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(39)).fireproof()));
                MARK45_NETHERITE_CHESTPLATE = registerItem("mark45_netherite_chestplate",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(0xFF49393f, 0xff5d565d, 0xff6e5959, -1443585), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "mark45_netherite"))).setStyle(Style.EMPTY.withColor(0xFF49393f)), ModArmorMaterials.NETHERITE_IRONMAN_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()
                                .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(39)).fireproof()));
                MARK45_NETHERITE_LEGGINGS = registerItem("mark45_netherite_leggings",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(0xFF49393f, 0xff5d565d, 0xff6e5959, -1443585), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "mark45_netherite"))).setStyle(Style.EMPTY.withColor(0xFF49393f)), ModArmorMaterials.NETHERITE_IRONMAN_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()
                                .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(39)).fireproof()));
                MARK45_NETHERITE_BOOTS = registerItem("mark45_netherite_boots",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(0xFF49393f, 0xff5d565d, 0xff6e5959, -1443585), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "mark45_netherite"))).setStyle(Style.EMPTY.withColor(0xFF49393f)), ModArmorMaterials.NETHERITE_IRONMAN_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()
                                .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(39)).fireproof()));

                //speedster armors
                //vibranium
                //mark6 armors
                SPEEDSTER_VIBRANIUM_HELMET = registerItem("speedster_vibranium_helmet",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(-12696503, -11241349, -11241349, 0x40FFDE3A), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "speedster_vibranium"))).setStyle(Style.EMPTY.withColor(-11241349)), ModArmorMaterials.VIBRANIUM_SPEEDSTER_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings()
                                .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(37))));
                SPEEDSTER_VIBRANIUM_CHESTPLATE = registerItem("speedster_vibranium_chestplate",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(-12696503, -11241349, -11241349, 0x40FFDE3A), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "speedster_vibranium"))).setStyle(Style.EMPTY.withColor(-11241349)), ModArmorMaterials.VIBRANIUM_SPEEDSTER_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()
                                .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(37))));
                SPEEDSTER_VIBRANIUM_LEGGINGS = registerItem("speedster_vibranium_leggings",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(-12696503, -11241349, -11241349, 0x40FFDE3A), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "speedster_vibranium"))).setStyle(Style.EMPTY.withColor(-11241349)), ModArmorMaterials.VIBRANIUM_SPEEDSTER_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()
                                .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(37))));
                SPEEDSTER_VIBRANIUM_BOOTS = registerItem("speedster_vibranium_boots",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(-12696503, -11241349, -11241349, 0x40FFDE3A), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "speedster_vibranium"))).setStyle(Style.EMPTY.withColor(-11241349)), ModArmorMaterials.VIBRANIUM_SPEEDSTER_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()
                                .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(37))));
                //gold_titanium
                SPEEDSTER_GOLD_TITANIUM_HELMET = registerItem("speedster_gold_titanium_helmet",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(0xFF976A27, 0xffd0a75e, 0xffd0a75e, 0x40FFDE3A), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "speedster_gold_titanium"))).formatted(Formatting.GOLD), ModArmorMaterials.GOLD_TITANIUM_SPEEDSTER_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings()
                                .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(31))));
                SPEEDSTER_GOLD_TITANIUM_CHESTPLATE = registerItem("speedster_gold_titanium_chestplate",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(0xFF976A27, 0xffd0a75e, 0xffd0a75e, 0x40FFDE3A), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "speedster_gold_titanium"))).formatted(Formatting.GOLD), ModArmorMaterials.GOLD_TITANIUM_SPEEDSTER_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()
                                .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(31))));
                SPEEDSTER_GOLD_TITANIUM_LEGGINGS = registerItem("speedster_gold_titanium_leggings",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(0xFF976A27, 0xffd0a75e, 0xffd0a75e, 0x40FFDE3A), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "speedster_gold_titanium"))).formatted(Formatting.GOLD), ModArmorMaterials.GOLD_TITANIUM_SPEEDSTER_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()
                                .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(31))));
                SPEEDSTER_GOLD_TITANIUM_BOOTS = registerItem("speedster_gold_titanium_boots",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(0xFF976A27, 0xffd0a75e, 0xffd0a75e, 0x40FFDE3A), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "speedster_gold_titanium"))).formatted(Formatting.GOLD), ModArmorMaterials.GOLD_TITANIUM_SPEEDSTER_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()
                                .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(31))));
                //gold_titanium
                SPEEDSTER_NETHERITE_HELMET = registerItem("speedster_netherite_helmet",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(0xFF49393f, 0xff5d565d, 0xff6e5959, 0x40FFDE3A), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "speedster_netherite"))).setStyle(Style.EMPTY.withColor(0xFF49393f)), ModArmorMaterials.NETHERITE_SPEEDSTER_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings()
                                .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(39)).fireproof()));
                SPEEDSTER_NETHERITE_CHESTPLATE = registerItem("speedster_netherite_chestplate",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(0xFF49393f, 0xff5d565d, 0xff6e5959, 0x40FFDE3A), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "speedster_netherite"))).setStyle(Style.EMPTY.withColor(0xFF49393f)), ModArmorMaterials.NETHERITE_SPEEDSTER_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()
                                .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(39)).fireproof()));
                SPEEDSTER_NETHERITE_LEGGINGS = registerItem("speedster_netherite_leggings",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(0xFF49393f, 0xff5d565d, 0xff6e5959, 0x40FFDE3A), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "speedster_netherite"))).setStyle(Style.EMPTY.withColor(0xFF49393f)), ModArmorMaterials.NETHERITE_SPEEDSTER_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()
                                .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(39)).fireproof()));
                SPEEDSTER_NETHERITE_BOOTS = registerItem("speedster_netherite_boots",
                        (Item) AdvancedArmorClassConstructor.newInstance(List.of(0xFF49393f, 0xff5d565d, 0xff6e5959, 0x40FFDE3A), List.of(-1, -1, -1, 15), Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "speedster_netherite"))).setStyle(Style.EMPTY.withColor(0xFF49393f)), ModArmorMaterials.NETHERITE_SPEEDSTER_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()
                                .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(39)).fireproof()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(MyneHeroes.MOD_ID, name), item);
    }

    public static void registerModItems() {
        MyneHeroes.LOGGER.info("Registering Mod Items for " + MyneHeroes.MOD_ID);
    }
}