package net.togyk.myneheroes.trim;

import net.minecraft.item.Item;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.togyk.myneheroes.Item.ModItems;
import net.togyk.myneheroes.MyneHeroes;

import java.util.Map;

public class ModTrimMaterials {
    public static final RegistryKey<ArmorTrimMaterial> VIBRANIUM = RegistryKey.of(RegistryKeys.TRIM_MATERIAL,
            Identifier.of(MyneHeroes.MOD_ID, "vibranium"));
    //register more Keys here

    public static void bootstrap(Registerable<ArmorTrimMaterial> registerable) {
        register(registerable, VIBRANIUM, Registries.ITEM.getEntry(ModItems.VIBRANIUM_INGOT),
                Style.EMPTY.withColor(TextColor.parse("#3e616e").getOrThrow()), 0.9f);

        //register more bootstraps here
    }


    /*

    @param itemModelIndex = color displayed on the item based on a float and the item model
     */
    private static void register(Registerable<ArmorTrimMaterial> registerable, RegistryKey<ArmorTrimMaterial> armorTrimKey,
                                 RegistryEntry<Item> item, Style style, float itemModelIndex) {
        ArmorTrimMaterial trimMaterial = new ArmorTrimMaterial(armorTrimKey.getValue().getPath(), item, itemModelIndex, Map.of(),
                Text.translatable(Util.createTranslationKey("trim_material", armorTrimKey.getValue())).fillStyle(style));

        registerable.register(armorTrimKey, trimMaterial);
    }
}