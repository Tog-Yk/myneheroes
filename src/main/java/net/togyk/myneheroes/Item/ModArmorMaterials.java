package net.togyk.myneheroes.Item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.togyk.myneheroes.MyneHeroes;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ModArmorMaterials {
    //titanium armor
    public static final RegistryEntry<ArmorMaterial> TITANIUM_MATERIAL = registerArmorMaterial("titanium",
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 2);
                map.put(ArmorItem.Type.LEGGINGS, 5);
                map.put(ArmorItem.Type.CHESTPLATE, 6);
                map.put(ArmorItem.Type.HELMET, 2);
                map.put(ArmorItem.Type.BODY, 4);
            }), 20, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, () -> Ingredient.ofItems(ModItems.TITANIUM_INGOT),
                    List.of(new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID, "titanium"))), 0,0));


    //vibranium armors
    public static final RegistryEntry<ArmorMaterial> VIBRANIUM_MATERIAL = registerArmorMaterial("vibranium",
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 3);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.HELMET, 4);
                map.put(ArmorItem.Type.BODY, 7);
            }), 20, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, () -> Ingredient.ofItems(ModItems.VIBRANIUM_INGOT),
                    List.of(new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID, "vibranium"))), 2.0F,0.05F));

        //Ironman pattern
    public static final RegistryEntry<ArmorMaterial> VIBRANIUM_IRONMAN_MATERIAL = registerArmorMaterial("vibranium_ironman",
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 3);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.HELMET, 4);
                map.put(ArmorItem.Type.BODY, 7);
            }), 20, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, () -> Ingredient.ofItems(ModItems.VIBRANIUM_INGOT),
                    List.of(
                            new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID,"mark6"), "", true),
                            new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID,"mark6_details"), "", true),
                            new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID,"mark6_details_2"), "", true),
                            new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID,"mark6_glow"), "", true)
                    ),0,0));

    //Speedster pattern
    public static final RegistryEntry<ArmorMaterial> VIBRANIUM_SPEEDSTER_MATERIAL = registerArmorMaterial("vibranium_speedster",
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 3);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.HELMET, 4);
                map.put(ArmorItem.Type.BODY, 7);
            }), 20, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, () -> Ingredient.ofItems(ModItems.VIBRANIUM_INGOT),
                    List.of(
                            new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID,"speedster_vibranium"), "", true),
                            new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID,"speedster_vibranium_details"), "", true),
                            new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID,"speedster_vibranium_details_2"), "", true),
                            new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID,"speedster_vibranium_glow"), "", true)
                    ),0,0));

    /*
     * Spiderman pattern
     * Kryptonian pattern
     * Speedster pattern
     * panther pattern
     */


    //gold titanium armors
    public static final RegistryEntry<ArmorMaterial> GOLD_TITANIUM_MATERIAL = registerArmorMaterial("gold_titanium",
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 2);
                map.put(ArmorItem.Type.LEGGINGS, 5);
                map.put(ArmorItem.Type.CHESTPLATE, 7);
                map.put(ArmorItem.Type.HELMET, 3);
                map.put(ArmorItem.Type.BODY, 5);
            }), 20, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, () -> Ingredient.ofItems(ModItems.GOLD_TITANIUM_INGOT),
                    List.of(new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID, "gold_titanium"))), 0,0));

    //Ironman pattern
    public static final RegistryEntry<ArmorMaterial> GOLD_TITANIUM_IRONMAN_MATERIAL = registerArmorMaterial("gold_titanium_ironman",
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 2);
                map.put(ArmorItem.Type.LEGGINGS, 5);
                map.put(ArmorItem.Type.CHESTPLATE, 7);
                map.put(ArmorItem.Type.HELMET, 3);
                map.put(ArmorItem.Type.BODY, 5);
            }), 20, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, () -> Ingredient.ofItems(ModItems.GOLD_TITANIUM_INGOT),
                    List.of(
                            new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID,"mark3"), "", true),
                            new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID,"mark3_details"), "", true),
                            new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID,"mark3_details_2"), "", true),
                            new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID,"mark3_glow"), "", true)
                    ),0,0));

    //Speedster pattern
    public static final RegistryEntry<ArmorMaterial> GOLD_TITANIUM_SPEEDSTER_MATERIAL = registerArmorMaterial("gold_titanium_speedster",
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 2);
                map.put(ArmorItem.Type.LEGGINGS, 5);
                map.put(ArmorItem.Type.CHESTPLATE, 7);
                map.put(ArmorItem.Type.HELMET, 3);
                map.put(ArmorItem.Type.BODY, 5);
            }), 20, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, () -> Ingredient.ofItems(ModItems.GOLD_TITANIUM_INGOT),
                    List.of(
                            new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID,"speedster_gold_titanium"), "", true),
                            new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID,"speedster_gold_titanium_details"), "", true),
                            new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID,"speedster_gold_titanium_details_2"), "", true),
                            new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID,"speedster_gold_titanium_glow"), "", true)
                    ),0,0));
    /*
     * Spiderman pattern
     * Kryptonian pattern
     * panther pattern
     */


    //iron armors
    /*
     * Ironman pattern
     * Spiderman pattern
     * Kryptonian pattern
     * Speedster pattern
     * panther pattern
     */


    //netherite armors

    //Ironman pattern
    public static final RegistryEntry<ArmorMaterial> NETHERITE_IRONMAN_MATERIAL = registerArmorMaterial("netherite_ironman",
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 3);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.HELMET, 3);
                map.put(ArmorItem.Type.BODY, 11);
            }), 20, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT),
                    List.of(
                            new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID,"mark45"), "", true),
                            new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID,"mark45_details"), "", true),
                            new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID,"mark45_details_2"), "", true),
                            new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID,"mark45_glow"), "", true)
                    ),0,0));
    /*
     * Spiderman pattern
     * Kryptonian pattern
     * Speedster pattern
     * panther pattern
     */


    public static RegistryEntry<ArmorMaterial> registerArmorMaterial(String name, Supplier<ArmorMaterial> material) {
        return Registry.registerReference(Registries.ARMOR_MATERIAL, Identifier.of(MyneHeroes.MOD_ID, name), material.get());
    }
}