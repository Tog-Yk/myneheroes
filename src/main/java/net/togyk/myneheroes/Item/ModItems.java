package net.togyk.myneheroes.Item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.togyk.myneheroes.Item.custom.*;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Abilities;
import net.togyk.myneheroes.power.detailed.AdamantiumUpgradablePower;
import net.togyk.myneheroes.upgrade.Upgrades;

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

    public static final Item MJOLNIR = registerItem("mjolnir",
            new ControlableThrowableItem(ModToolMaterial.MJOLNIR, new Item.Settings()));

    public static final Item CIRCUIT_BOARD = registerItem("circuit_board",
            new Item(new Item.Settings()));

    public static final Item WEB_FLUID = registerItem("web_fluid",
            new Item(new Item.Settings()));

    public static final Item BOTTLE_OF_SPIDER_VENOM = registerItem("bottle_of_spider_venom",
            new PoisonDrinkItem(new Item.Settings().maxCount(1).recipeRemainder(Items.GLASS_BOTTLE)));

    public static final Item BOTTLE_OF_RADIOACTIVE_SPIDER_VENOM = registerItem("bottle_of_radioactive_spider_venom",
            new SpiderManPowerDrink(List.of(StatusEffects.NAUSEA), new Item.Settings().maxCount(1).recipeRemainder(Items.GLASS_BOTTLE)));

    public static final Item MECHANICAL_HUD_UPGRADE = registerItem("mechanical_hud_upgrade", new UpgradeItem(Upgrades.MECHANICAL_HUD, Text.literal("mechanical").setStyle(Style.EMPTY.withColor(0xC428EEFF)), new Item.Settings()));
    public static final Item SPEEDSTER_HUD_UPGRADE = registerItem("speedster_hud_upgrade", new UpgradeItem(Upgrades.SPEEDSTER_HUD, Text.literal("speedster").setStyle(Style.EMPTY.withColor(0xC4FFEB28)), new Item.Settings()));
    public static final Item LASER_UPGRADE = registerItem("laser_upgrade", new UpgradeItem(Upgrades.LASER, new Item.Settings()));
    public static final Item FlY_UPGRADE = registerItem("fly_upgrade", new UpgradeItem(Upgrades.FlY, new Item.Settings()));
    public static final Item KINETIC_ENERGY_STORAGE_UPGRADE = registerItem("kinetic_energy_storage_upgrade", new UpgradeItem(Upgrades.KINETIC_ENERGY_STORAGE, new Item.Settings()));
    public static final Item TAKE_OFF_SUIT_UPGRADE = registerItem("take_off_suit_upgrade", new UpgradeItem(Upgrades.TAKE_OFF_SUIT, new Item.Settings()));
    public static final Item TOOLBELT = registerItem("toolbelt", new EquipableUpgradeItem(Upgrades.TOOLBELT_3, EquipmentSlot.LEGS, Text.translatable("item.myneheroes.toolbelt_upgrade.size", 3), new Item.Settings().maxCount(1)));
    public static final Item IRON_TOOLBELT = registerItem("iron_toolbelt", new EquipableUpgradeItem(Upgrades.TOOLBELT_4, EquipmentSlot.LEGS, Text.translatable("item.myneheroes.toolbelt_upgrade.size", 4), new Item.Settings().maxCount(1)));
    public static final Item DIAMOND_TOOLBELT = registerItem("diamond_toolbelt", new EquipableUpgradeItem(Upgrades.TOOLBELT_6, EquipmentSlot.LEGS, Text.translatable("item.myneheroes.toolbelt_upgrade.size", 6), new Item.Settings().maxCount(1)));
    public static final Item NETHERITE_TOOLBELT = registerItem("netherite_toolbelt", new EquipableUpgradeItem(Upgrades.TOOLBELT_8, EquipmentSlot.LEGS, Text.translatable("item.myneheroes.toolbelt_upgrade.size", 8), new Item.Settings().maxCount(1)));

    public static final Item WEB_SHOOTER_UPGRADE = registerItem("web_shooter_upgrade", new AbilityHoldingUpgradeItem(Upgrades.WEB_SHOOTER, new Item.Settings().maxCount(1)));

    public static final Item COLORING_COMPOUND = registerItem("coloring_compound",
            new SimpleDyeableItem(-1, new Item.Settings()));

    public static final Item HEART_SHAPED_HERB_MIX = registerItem("heart_shaped_herb_mix",
            new ReversablePowerDrinkItem(Identifier.of(MyneHeroes.MOD_ID, "avatar_of_bast"), List.of(), new Item.Settings().recipeRemainder(Items.BOWL).maxCount(1)));

    public static final Item BONE_CLAWS = registerItem("bone_claws",
            new TemporaryWeapon(new Item.Settings().maxCount(1).attributeModifiers(TemporaryWeapon.createAttributeModifiers(4, -1.2F))));
    public static final Item ADAMANTIUM_CLAWS = registerItem("adamantium_claws",
            new TemporaryWeapon(new Item.Settings().fireproof().maxCount(1).attributeModifiers(TemporaryWeapon.createAttributeModifiers(4.5F, -1.2F))));

    public static final Item ADAMANTIUM_UPGRADE = registerItem("adamantium_upgrade_smithing_template",
            new ModSmithingTemplateItem(false, Identifier.of(MyneHeroes.MOD_ID, "adamantium_upgrade"), new Item.Settings()));

    public static final Item ADAMANTIUM_INGOT = registerItem("adamantium_ingot",
            new Item(new Item.Settings().fireproof()));

    public static final Item WOODEN_KATANA = registerItem("wooden_katana",
            new KatanaItem(ToolMaterials.WOOD, new Item.Settings().fireproof().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.WOOD, 3, -1.4F))));
    public static final Item STONE_KATANA = registerItem("stone_katana",
            new KatanaItem(ToolMaterials.STONE, new Item.Settings().fireproof().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.STONE, 3, -1.4F))));
    public static final Item IRON_KATANA = registerItem("iron_katana",
            new KatanaItem(ToolMaterials.IRON, new Item.Settings().fireproof().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.IRON, 3, -1.4F))));
    public static final Item GOLD_KATANA = registerItem("gold_katana",
            new KatanaItem(ToolMaterials.GOLD, new Item.Settings().fireproof().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.GOLD, 3, -1.4F))));
    public static final Item DIAMOND_KATANA = registerItem("diamond_katana",
            new KatanaItem(ToolMaterials.DIAMOND, new Item.Settings().fireproof().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.DIAMOND, 3, -1.4F))));
    public static final Item NETHERITE_KATANA = registerItem("netherite_katana",
            new KatanaItem(ToolMaterials.NETHERITE, new Item.Settings().fireproof().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.NETHERITE, 3, -1.4F))));


    public static final Item ADAMANTIUM_KATANA = registerItem("adamantium_katana",
            new KatanaItem(ModToolMaterial.ADAMANTIUM, new Item.Settings().fireproof().attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterial.ADAMANTIUM, 3, -1.4F))));

    public static final Item ADAMANTIUM_SWORD = registerItem("adamantium_sword",
            new SwordItem(ModToolMaterial.ADAMANTIUM, new Item.Settings().fireproof().attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterial.ADAMANTIUM, 3, -2.4F))));
    public static final Item ADAMANTIUM_SHOVEL = registerItem("adamantium_shovel",
            new ShovelItem(ModToolMaterial.ADAMANTIUM, new Item.Settings().fireproof().attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterial.ADAMANTIUM, 1, -3.0F))));
    public static final Item ADAMANTIUM_PICKAXE = registerItem("adamantium_pickaxe",
            new PickaxeItem(ModToolMaterial.ADAMANTIUM, new Item.Settings().fireproof().attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterial.ADAMANTIUM, 3, -2.8F))));
    public static final Item ADAMANTIUM_AXE = registerItem("adamantium_axe",
            new AxeItem(ModToolMaterial.ADAMANTIUM, new Item.Settings().fireproof().attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterial.ADAMANTIUM, 5, -3.0F))));
    public static final Item ADAMANTIUM_HOE = registerItem("adamantium_hoe",
            new HoeItem(ModToolMaterial.ADAMANTIUM, new Item.Settings().fireproof().attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterial.ADAMANTIUM, -4, 0.0F))));

    public static final Item ADAMANTIUM_INJECTION = registerItem("adamantium_injection",
            new PowerUpgradeItem<>(AdamantiumUpgradablePower.class,
                    (power, stack) -> !power.hasAdamantium(),
                    (power, stack) -> {
                        power.setAdamantium(true);
                        return true;
                    },
                    new Item.Settings().recipeRemainder(ModItems.POWER_INJECTION)
            )
    );

    public static final Item ANTI_RADIATION_COATING = registerItem("anti_radiation_coating",
            new UpgradeItem(Upgrades.ANTI_RADIATION, new Item.Settings())
    );

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

    //adamantium armor
    public static final Item ADAMANTIUM_HELMET = registerItem("adamantium_helmet",
            new ArmorItem(ModArmorMaterials.ADAMANTIUM_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings().fireproof()
                    .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(29))));
    public static final Item ADAMANTIUM_CHESTPLATE = registerItem("adamantium_chestplate",
            new ArmorItem(ModArmorMaterials.ADAMANTIUM_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings().fireproof()
                    .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(29))));
    public static final Item ADAMANTIUM_LEGGINGS = registerItem("adamantium_leggings",
            new ArmorItem(ModArmorMaterials.ADAMANTIUM_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings().fireproof()
                    .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(29))));
    public static final Item ADAMANTIUM_BOOTS = registerItem("adamantium_boots",
            new ArmorItem(ModArmorMaterials.ADAMANTIUM_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings().fireproof()
                    .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(29))));


    //mark6 armors
    // mark6 armors
    public static final Item MARK6_VIBRANIUM_HELMET = registerItem("mark6_vibranium_helmet",
            new DyeableAdvancedArmorWithFaceplateItem(
                    List.of(-12696503, -11241349, -0x574545, -1443585, 0xFFEC8EFF), 15,
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "mark6_vibranium")))
                            .setStyle(Style.EMPTY.withColor(-11241349)),
                    ModArmorMaterials.VIBRANIUM_IRONMAN_MATERIAL,
                    ArmorItem.Type.HELMET,
                    new Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(37))
            ));

    public static final Item MARK6_VIBRANIUM_CHESTPLATE = registerItem("mark6_vibranium_chestplate",
            new DyeableAdvancedArmorItem(
                    List.of(-12696503, -11241349, -0x574545, -1443585, 0xFFEC8EFF),
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "mark6_vibranium")))
                            .setStyle(Style.EMPTY.withColor(-11241349)),
                    ModArmorMaterials.VIBRANIUM_IRONMAN_MATERIAL,
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(37))
            ));

    public static final Item MARK6_VIBRANIUM_LEGGINGS = registerItem("mark6_vibranium_leggings",
            new DyeableAdvancedArmorItem(
                    List.of(-12696503, -11241349, -0x574545, -1443585, 0xFFEC8EFF),
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "mark6_vibranium")))
                            .setStyle(Style.EMPTY.withColor(-11241349)),
                    ModArmorMaterials.VIBRANIUM_IRONMAN_MATERIAL,
                    ArmorItem.Type.LEGGINGS,
                    new Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(37))
            ));

    public static final Item MARK6_VIBRANIUM_BOOTS = registerItem("mark6_vibranium_boots",
            new DyeableAdvancedArmorItem(
                    List.of(-12696503, -11241349, -0x574545, -1443585, 0xFFEC8EFF),
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "mark6_vibranium")))
                            .setStyle(Style.EMPTY.withColor(-11241349)),
                    ModArmorMaterials.VIBRANIUM_IRONMAN_MATERIAL,
                    ArmorItem.Type.BOOTS,
                    new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(37))
            ));

    // mark3 armors
    public static final Item MARK3_GOLD_TITANIUM_HELMET = registerItem("mark3_gold_titanium_helmet",
            new DyeableAdvancedArmorWithFaceplateItem(
                    List.of(0xFF976A27, 0xffd0a75e, 0xff6e5959, -1443585), 15,
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "mark3_gold_titanium")))
                            .formatted(Formatting.GOLD),
                    ModArmorMaterials.GOLD_TITANIUM_IRONMAN_MATERIAL,
                    ArmorItem.Type.HELMET,
                    new Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(31))
            ));

    public static final Item MARK3_GOLD_TITANIUM_CHESTPLATE = registerItem("mark3_gold_titanium_chestplate",
            new DyeableAdvancedArmorItem(
                    List.of(0xFF976A27, 0xffd0a75e, 0xff6e5959, -1443585),
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "mark3_gold_titanium")))
                            .formatted(Formatting.GOLD),
                    ModArmorMaterials.GOLD_TITANIUM_IRONMAN_MATERIAL,
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(31))
            ));

    public static final Item MARK3_GOLD_TITANIUM_LEGGINGS = registerItem("mark3_gold_titanium_leggings",
            new DyeableAdvancedArmorItem(
                    List.of(0xFF976A27, 0xffd0a75e, 0xff6e5959, -1443585),
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "mark3_gold_titanium")))
                            .formatted(Formatting.GOLD),
                    ModArmorMaterials.GOLD_TITANIUM_IRONMAN_MATERIAL,
                    ArmorItem.Type.LEGGINGS,
                    new Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(31))
            ));

    public static final Item MARK3_GOLD_TITANIUM_BOOTS = registerItem("mark3_gold_titanium_boots",
            new DyeableAdvancedArmorItem(
                    List.of(0xFF976A27, 0xffd0a75e, 0xff6e5959, -1443585),
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "mark3_gold_titanium")))
                            .formatted(Formatting.GOLD),
                    ModArmorMaterials.GOLD_TITANIUM_IRONMAN_MATERIAL,
                    ArmorItem.Type.BOOTS,
                    new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(31))
            ));

    // mark45 armors
    public static final Item MARK45_NETHERITE_HELMET = registerItem("mark45_netherite_helmet",
            new DyeableAdvancedArmorWithFaceplateItem(
                    List.of(0xFF49393f, 0xff5d565d, 0xff6e5959, -1443585), 15,
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "mark45_netherite")))
                            .setStyle(Style.EMPTY.withColor(0xFF49393f)),
                    ModArmorMaterials.NETHERITE_IRONMAN_MATERIAL,
                    ArmorItem.Type.HELMET,
                    new Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(39)).fireproof()
            ));

    public static final Item MARK45_NETHERITE_CHESTPLATE = registerItem("mark45_netherite_chestplate",
            new DyeableAdvancedArmorItem(
                    List.of(0xFF49393f, 0xff5d565d, 0xff6e5959, -1443585),
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "mark45_netherite")))
                            .setStyle(Style.EMPTY.withColor(0xFF49393f)),
                    ModArmorMaterials.NETHERITE_IRONMAN_MATERIAL,
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(39)).fireproof()
            ));

    public static final Item MARK45_NETHERITE_LEGGINGS = registerItem("mark45_netherite_leggings",
            new DyeableAdvancedArmorItem(
                    List.of(0xFF49393f, 0xff5d565d, 0xff6e5959, -1443585),
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "mark45_netherite")))
                            .setStyle(Style.EMPTY.withColor(0xFF49393f)),
                    ModArmorMaterials.NETHERITE_IRONMAN_MATERIAL,
                    ArmorItem.Type.LEGGINGS,
                    new Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(39)).fireproof()
            ));

    public static final Item MARK45_NETHERITE_BOOTS = registerItem("mark45_netherite_boots",
            new DyeableAdvancedArmorItem(
                    List.of(0xFF49393f, 0xff5d565d, 0xff6e5959, -1443585),
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "mark45_netherite")))
                            .setStyle(Style.EMPTY.withColor(0xFF49393f)),
                    ModArmorMaterials.NETHERITE_IRONMAN_MATERIAL,
                    ArmorItem.Type.BOOTS,
                    new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(39)).fireproof()
            ));

    // speedster — vibranium
    public static final Item SPEEDSTER_VIBRANIUM_HELMET = registerItem("speedster_vibranium_helmet",
            new DyeableAdvancedArmorItem(
                    List.of(-12696503, -11241349, -11241349, 0x77FFDE3A, 0xFFEC8EFF),
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "speedster_vibranium")))
                            .setStyle(Style.EMPTY.withColor(-11241349)),
                    ModArmorMaterials.VIBRANIUM_SPEEDSTER_MATERIAL,
                    ArmorItem.Type.HELMET,
                    new Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(37))
            ));

    public static final Item SPEEDSTER_VIBRANIUM_CHESTPLATE = registerItem("speedster_vibranium_chestplate",
            new DyeableAdvancedArmorItem(
                    List.of(-12696503, -11241349, -11241349, 0x77FFDE3A, 0xFFEC8EFF),
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "speedster_vibranium")))
                            .setStyle(Style.EMPTY.withColor(-11241349)),
                    ModArmorMaterials.VIBRANIUM_SPEEDSTER_MATERIAL,
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(37))
            ));

    public static final Item SPEEDSTER_VIBRANIUM_LEGGINGS = registerItem("speedster_vibranium_leggings",
            new DyeableAdvancedArmorItem(
                    List.of(-12696503, -11241349, -11241349, 0x77FFDE3A, 0xFFEC8EFF),
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "speedster_vibranium")))
                            .setStyle(Style.EMPTY.withColor(-11241349)),
                    ModArmorMaterials.VIBRANIUM_SPEEDSTER_MATERIAL,
                    ArmorItem.Type.LEGGINGS,
                    new Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(37))
            ));

    public static final Item SPEEDSTER_VIBRANIUM_BOOTS = registerItem("speedster_vibranium_boots",
            new DyeableAdvancedArmorItem(
                    List.of(-12696503, -11241349, -11241349, 0x77FFDE3A, 0xFFEC8EFF),
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "speedster_vibranium")))
                            .setStyle(Style.EMPTY.withColor(-11241349)),
                    ModArmorMaterials.VIBRANIUM_SPEEDSTER_MATERIAL,
                    ArmorItem.Type.BOOTS,
                    new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(37))
            ));

    // speedster — gold titanium
    public static final Item SPEEDSTER_GOLD_TITANIUM_HELMET = registerItem("speedster_gold_titanium_helmet",
            new DyeableAdvancedArmorItem(
                    List.of(0xFF976A27, 0xffd0a75e, 0xffd0a75e, 0x77FFDE3A),
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "speedster_gold_titanium")))
                            .formatted(Formatting.GOLD),
                    ModArmorMaterials.GOLD_TITANIUM_SPEEDSTER_MATERIAL,
                    ArmorItem.Type.HELMET,
                    new Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(31))
            ));

    public static final Item SPEEDSTER_GOLD_TITANIUM_CHESTPLATE = registerItem("speedster_gold_titanium_chestplate",
            new DyeableAdvancedArmorItem(
                    List.of(0xFF976A27, 0xffd0a75e, 0xffd0a75e, 0x77FFDE3A),
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "speedster_gold_titanium")))
                            .formatted(Formatting.GOLD),
                    ModArmorMaterials.GOLD_TITANIUM_SPEEDSTER_MATERIAL,
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(31))
            ));

    public static final Item SPEEDSTER_GOLD_TITANIUM_LEGGINGS = registerItem("speedster_gold_titanium_leggings",
            new DyeableAdvancedArmorItem(
                    List.of(0xFF976A27, 0xffd0a75e, 0xffd0a75e, 0x77FFDE3A),
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "speedster_gold_titanium")))
                            .formatted(Formatting.GOLD),
                    ModArmorMaterials.GOLD_TITANIUM_SPEEDSTER_MATERIAL,
                    ArmorItem.Type.LEGGINGS,
                    new Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(31))
            ));

    public static final Item SPEEDSTER_GOLD_TITANIUM_BOOTS = registerItem("speedster_gold_titanium_boots",
            new DyeableAdvancedArmorItem(
                    List.of(0xFF976A27, 0xffd0a75e, 0xffd0a75e, 0x77FFDE3A),
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "speedster_gold_titanium")))
                            .formatted(Formatting.GOLD),
                    ModArmorMaterials.GOLD_TITANIUM_SPEEDSTER_MATERIAL,
                    ArmorItem.Type.BOOTS,
                    new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(31))
            ));

    // speedster — netherite
    public static final Item SPEEDSTER_NETHERITE_HELMET = registerItem("speedster_netherite_helmet",
            new DyeableAdvancedArmorItem(
                    List.of(0xFF49393f, 0xff5d565d, 0xff6e5959, 0x77FFDE3A),
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "speedster_netherite")))
                            .setStyle(Style.EMPTY.withColor(0xFF49393f)),
                    ModArmorMaterials.NETHERITE_SPEEDSTER_MATERIAL,
                    ArmorItem.Type.HELMET,
                    new Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(39)).fireproof()
            ));

    public static final Item SPEEDSTER_NETHERITE_CHESTPLATE = registerItem("speedster_netherite_chestplate",
            new DyeableAdvancedArmorItem(
                    List.of(0xFF49393f, 0xff5d565d, 0xff6e5959, 0x77FFDE3A),
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "speedster_netherite")))
                            .setStyle(Style.EMPTY.withColor(0xFF49393f)),
                    ModArmorMaterials.NETHERITE_SPEEDSTER_MATERIAL,
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(39)).fireproof()
            ));

    public static final Item SPEEDSTER_NETHERITE_LEGGINGS = registerItem("speedster_netherite_leggings",
            new DyeableAdvancedArmorItem(
                    List.of(0xFF49393f, 0xff5d565d, 0xff6e5959, 0x77FFDE3A),
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "speedster_netherite")))
                            .setStyle(Style.EMPTY.withColor(0xFF49393f)),
                    ModArmorMaterials.NETHERITE_SPEEDSTER_MATERIAL,
                    ArmorItem.Type.LEGGINGS,
                    new Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(39)).fireproof()
            ));

    public static final Item SPEEDSTER_NETHERITE_BOOTS = registerItem("speedster_netherite_boots",
            new DyeableAdvancedArmorItem(
                    List.of(0xFF49393f, 0xff5d565d, 0xff6e5959, 0x77FFDE3A),
                    List.of(-1, -1, -1, 15),
                    Text.translatable(Util.createTranslationKey("armor_pattern",
                                    Identifier.of(MyneHeroes.MOD_ID, "speedster_netherite")))
                            .setStyle(Style.EMPTY.withColor(0xFF49393f)),
                    ModArmorMaterials.NETHERITE_SPEEDSTER_MATERIAL,
                    ArmorItem.Type.BOOTS,
                    new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(39)).fireproof()
            ));


    //tutorial items
    public static Item CLARK_KENT_GLASSES = registerItem("clark_kent_glasses",
            new TutorialItem(new Item.Settings().maxCount(1), Abilities.TUTOR_HUD.copy()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(MyneHeroes.MOD_ID, name), item);
    }

    public static void registerModItems() {
        MyneHeroes.LOGGER.info("Registering Mod Items for " + MyneHeroes.MOD_ID);
    }
}