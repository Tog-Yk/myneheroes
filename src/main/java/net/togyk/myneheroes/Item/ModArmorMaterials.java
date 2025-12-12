package net.togyk.myneheroes.Item;

import com.google.common.collect.Maps;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.util.ModTags;

import java.util.Map;

import static net.minecraft.item.equipment.EquipmentAssetKeys.REGISTRY_KEY;

public class ModArmorMaterials {
    public static final RegistryKey<EquipmentAsset> TITANIUM_KEY = RegistryKey.of(REGISTRY_KEY, Identifier.of(MyneHeroes.MOD_ID, "titanium"));
    public static final RegistryKey<EquipmentAsset> ADAMANTIUM_KEY = RegistryKey.of(REGISTRY_KEY, Identifier.of(MyneHeroes.MOD_ID, "adamantium"));

    public static final RegistryKey<EquipmentAsset> VIBRANIUM_KEY = RegistryKey.of(REGISTRY_KEY, Identifier.of(MyneHeroes.MOD_ID, "vibranium"));
    public static final RegistryKey<EquipmentAsset> MECHANICAL_VIBRANIUM_KEY = RegistryKey.of(REGISTRY_KEY, Identifier.of(MyneHeroes.MOD_ID, "mark6"));
    public static final RegistryKey<EquipmentAsset> SPEEDSTER_VIBRANIUM_KEY = RegistryKey.of(REGISTRY_KEY, Identifier.of(MyneHeroes.MOD_ID, "speedster_vibranium"));

    public static final RegistryKey<EquipmentAsset> GOLD_TITANIUM_KEY = RegistryKey.of(REGISTRY_KEY, Identifier.of(MyneHeroes.MOD_ID, "gold_titanium"));
    public static final RegistryKey<EquipmentAsset> MECHANICAL_GOLD_TITANIUM_KEY = RegistryKey.of(REGISTRY_KEY, Identifier.of(MyneHeroes.MOD_ID, "mark3"));
    public static final RegistryKey<EquipmentAsset> SPEEDSTER_GOLD_TITANIUM_KEY = RegistryKey.of(REGISTRY_KEY, Identifier.of(MyneHeroes.MOD_ID, "speedster_gold_titanium"));

    public static final RegistryKey<EquipmentAsset> MECHANICAL_NETHERITE_KEY = RegistryKey.of(REGISTRY_KEY, Identifier.of(MyneHeroes.MOD_ID, "mark45"));
    public static final RegistryKey<EquipmentAsset> SPEEDSTER_NETHERITE_KEY = RegistryKey.of(REGISTRY_KEY, Identifier.of(MyneHeroes.MOD_ID, "speedster_netherite"));

    //titanium armor
    public static final ArmorMaterial TITANIUM_MATERIAL = new ArmorMaterial(16, createDefenseMap(2, 5, 6, 2, 4), 20, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,0,0, ModTags.Items.CAN_REPAIR_TITANIUM, TITANIUM_KEY);

    //adamantium armor
    public static final ArmorMaterial ADAMANTIUM_MATERIAL = new ArmorMaterial(39,
            createDefenseMap(3, 7, 10, 4, 13), 20, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, ModTags.Items.CAN_REPAIR_ADAMANTIUM, ADAMANTIUM_KEY);


    //vibranium armors
    public static final ArmorMaterial VIBRANIUM_MATERIAL = new ArmorMaterial(34,
            createDefenseMap(3, 6, 8, 4, 7), 20, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F,0.05F, ModTags.Items.CAN_REPAIR_VIBRANIUM, VIBRANIUM_KEY);

    //Ironman pattern
    public static final ArmorMaterial VIBRANIUM_IRONMAN_MATERIAL = new ArmorMaterial(34,
            createDefenseMap(3, 6, 8, 4, 7), 20, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F,0.05F, ModTags.Items.CAN_REPAIR_VIBRANIUM, MECHANICAL_VIBRANIUM_KEY);

    //Speedster pattern
    public static final ArmorMaterial VIBRANIUM_SPEEDSTER_MATERIAL = new ArmorMaterial(34,
            createDefenseMap(3, 6, 8, 4, 7), 20, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F,0.05F, ModTags.Items.CAN_REPAIR_VIBRANIUM, SPEEDSTER_VIBRANIUM_KEY);

    /*
     * Spiderman pattern
     * Kryptonian pattern
     * Speedster pattern
     * panther pattern
     */


    //gold titanium armors
    public static final ArmorMaterial GOLD_TITANIUM_MATERIAL = new ArmorMaterial(25,
            createDefenseMap(2, 5, 7, 3, 5), 20, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0,0, ModTags.Items.CAN_REPAIR_GOLD_TITANIUM, GOLD_TITANIUM_KEY);

    //Ironman pattern
    public static final ArmorMaterial GOLD_TITANIUM_IRONMAN_MATERIAL = new ArmorMaterial(25,
            createDefenseMap(2, 5, 7, 3, 5), 20, SoundEvents.ITEM_ARMOR_EQUIP_GOLD,0,0, ModTags.Items.CAN_REPAIR_GOLD_TITANIUM, MECHANICAL_GOLD_TITANIUM_KEY);

    //Speedster pattern
    public static final ArmorMaterial GOLD_TITANIUM_SPEEDSTER_MATERIAL = new ArmorMaterial(25,
            createDefenseMap(2, 5, 7, 3, 5), 20, SoundEvents.ITEM_ARMOR_EQUIP_GOLD,0,0, ModTags.Items.CAN_REPAIR_GOLD_TITANIUM, SPEEDSTER_GOLD_TITANIUM_KEY);
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
    public static final ArmorMaterial NETHERITE_IRONMAN_MATERIAL = new ArmorMaterial(37,
            createDefenseMap(3, 6, 8, 3, 11), 20, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, ItemTags.REPAIRS_NETHERITE_ARMOR, MECHANICAL_NETHERITE_KEY);

    public static final ArmorMaterial NETHERITE_SPEEDSTER_MATERIAL = new ArmorMaterial(37,
            createDefenseMap(3, 6, 8, 3, 11), 20, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, ItemTags.REPAIRS_NETHERITE_ARMOR, SPEEDSTER_NETHERITE_KEY);
    /*
     * Spiderman pattern
     * Kryptonian pattern
     * Speedster pattern
     * panther pattern
     */


    private static Map<EquipmentType, Integer> createDefenseMap(int bootsDefense, int leggingsDefense, int chestplateDefense, int helmetDefense, int bodyDefense) {
        return Maps.newEnumMap(
                Map.of(
                        EquipmentType.BOOTS,
                        bootsDefense,
                        EquipmentType.LEGGINGS,
                        leggingsDefense,
                        EquipmentType.CHESTPLATE,
                        chestplateDefense,
                        EquipmentType.HELMET,
                        helmetDefense,
                        EquipmentType.BODY,
                        bodyDefense
                )
        );
    }
}