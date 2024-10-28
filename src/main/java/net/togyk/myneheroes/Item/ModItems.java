package net.togyk.myneheroes.Item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;

public class ModItems {
    public static final Item PINK_GARNET = registerItem("vibranium_ingot", new Item(new Item.Settings()));
    public static final Item RAW_PINK_GARNET = registerItem("raw_vibranium", new Item(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(MyneHeroes.MOD_ID, name), item);
    }

    public static void registerModItems() {
        MyneHeroes.LOGGER.info("Registering Mod Items for " + MyneHeroes.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(PINK_GARNET);
            entries.add(RAW_PINK_GARNET);
        });
    }
}