package net.togyk.myneheroes.trim;

import net.minecraft.item.Item;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.togyk.myneheroes.Item.ModItems;
import net.togyk.myneheroes.MyneHeroes;

public class ModTrimPatterns {
    public static final RegistryKey<ArmorTrimPattern> IRON_SUIT = RegistryKey.of(RegistryKeys.TRIM_PATTERN,
            Identifier.of(MyneHeroes.MOD_ID, "iron_suit"));
    public static void bootstrap(Registerable<ArmorTrimPattern> context) {
        register(context, ModItems.IRON_SUIT_TEMPLATE, IRON_SUIT);
    }
    private static void register(Registerable<ArmorTrimPattern> context, Item item, RegistryKey<ArmorTrimPattern> key) {
        ArmorTrimPattern trimPattern = new ArmorTrimPattern(key.getValue(), Registries.ITEM.getEntry(item),
                Text.translatable(Util.createTranslationKey("trim_pattern", key.getValue())), false);
        context.register(key, trimPattern);
    }
}