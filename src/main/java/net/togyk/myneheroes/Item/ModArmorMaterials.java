package net.togyk.myneheroes.Item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
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
                map.put(ArmorItem.Type.LEGGINGS, 4);
                map.put(ArmorItem.Type.CHESTPLATE, 6);
                map.put(ArmorItem.Type.HELMET, 2);
                map.put(ArmorItem.Type.BODY, 4);
            }), 20, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, () -> Ingredient.ofItems(ModItems.TITANIUM_INGOT),
                    List.of(new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID, "titanium"))), 0,0));


    //vibranium armors
    public static final RegistryEntry<ArmorMaterial> VIBRANIUM_MATERIAL = registerArmorMaterial("vibranium",
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 2);
                map.put(ArmorItem.Type.LEGGINGS, 4);
                map.put(ArmorItem.Type.CHESTPLATE, 6);
                map.put(ArmorItem.Type.HELMET, 2);
                map.put(ArmorItem.Type.BODY, 4);
            }), 20, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, () -> Ingredient.ofItems(ModItems.VIBRANIUM_INGOT),
                    List.of(new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID, "vibranium"))), 0,0));

    /*
     * Ironman pattern
     * Spiderman pattern
     * Kryptonian pattern
     * Speedster pattern
     */


    //gold titanium armors
    public static final RegistryEntry<ArmorMaterial> GOLD_TITANIUM_MATERIAL = registerArmorMaterial("gold_titanium",
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 2);
                map.put(ArmorItem.Type.LEGGINGS, 4);
                map.put(ArmorItem.Type.CHESTPLATE, 6);
                map.put(ArmorItem.Type.HELMET, 2);
                map.put(ArmorItem.Type.BODY, 4);
            }), 20, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, () -> Ingredient.ofItems(ModItems.GOLD_TITANIUM_INGOT),
                    List.of(new ArmorMaterial.Layer(Identifier.of(MyneHeroes.MOD_ID, "gold_titanium"))), 0,0));
    /*
     * Ironman pattern
     * Spiderman pattern
     * Kryptonian pattern
     * Speedster pattern
     */


    //iron armors
    /*
     * Ironman pattern
     * Spiderman pattern
     * Kryptonian pattern
     * Speedster pattern
     */


    //netherite armors
    /*
     * Ironman pattern
     * Spiderman pattern
     * Kryptonian pattern
     * Speedster pattern
     */


    public static RegistryEntry<ArmorMaterial> registerArmorMaterial(String name, Supplier<ArmorMaterial> material) {
        return Registry.registerReference(Registries.ARMOR_MATERIAL, Identifier.of(MyneHeroes.MOD_ID, name), material.get());
    }
}