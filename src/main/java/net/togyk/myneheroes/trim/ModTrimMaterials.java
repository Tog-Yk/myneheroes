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

    public static final RegistryKey<ArmorTrimMaterial> TITANIUM = RegistryKey.of(RegistryKeys.TRIM_MATERIAL,
            Identifier.of(MyneHeroes.MOD_ID, "titanium"));

    public static final RegistryKey<ArmorTrimMaterial> GOLD_TITANIUM = RegistryKey.of(RegistryKeys.TRIM_MATERIAL,
            Identifier.of(MyneHeroes.MOD_ID, "gold_titanium"));

    public static final RegistryKey<ArmorTrimMaterial> ADAMANTIUM = RegistryKey.of(RegistryKeys.TRIM_MATERIAL,
            Identifier.of(MyneHeroes.MOD_ID, "adamantium"));

    //register more Keys here

    public static void bootstrap(Registerable<ArmorTrimMaterial> registerable) {
        register(registerable, VIBRANIUM, Registries.ITEM.getEntry(ModItems.VIBRANIUM_INGOT),
                Style.EMPTY.withColor(TextColor.parse("#3e616e").getOrThrow()), 0.9f);

        register(registerable, TITANIUM, Registries.ITEM.getEntry(ModItems.TITANIUM_INGOT),
                Style.EMPTY.withColor(TextColor.parse("#b6bbaf").getOrThrow()), 0.1f);

        register(registerable, GOLD_TITANIUM, Registries.ITEM.getEntry(ModItems.GOLD_TITANIUM_INGOT),
                Style.EMPTY.withColor(TextColor.parse("#976a27").getOrThrow()), 0.6f);

        register(registerable, ADAMANTIUM, Registries.ITEM.getEntry(ModItems.ADAMANTIUM_INGOT),
                Style.EMPTY.withColor(TextColor.parse("#b5abc5").getOrThrow()), 1.0f);

        //register more bootstraps here
    }


    /*

    @param itemModelIndex = lightLevel displayed on the item based on a float and the item model
     */
    private static void register(Registerable<ArmorTrimMaterial> registerable, RegistryKey<ArmorTrimMaterial> armorTrimKey,
                                 RegistryEntry<Item> item, Style style, float itemModelIndex) {
        ArmorTrimMaterial trimMaterial = new ArmorTrimMaterial(armorTrimKey.getValue().getPath(), item, itemModelIndex, Map.of(),
                Text.translatable(Util.createTranslationKey("trim_material", armorTrimKey.getValue())).fillStyle(style));

        registerable.register(armorTrimKey, trimMaterial);
    }
}