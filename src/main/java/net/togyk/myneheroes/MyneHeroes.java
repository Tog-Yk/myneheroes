package net.togyk.myneheroes;

import net.fabricmc.api.ModInitializer;

import net.togyk.myneheroes.Item.ModItemGroups;
import net.togyk.myneheroes.Item.ModItems;
import net.togyk.myneheroes.block.ModBlocks;
import net.togyk.myneheroes.worldgen.ModBiomeModifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyneHeroes implements ModInitializer {
    public static final String MOD_ID = "myneheroes";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModItemGroups.registerItemGroups();
        ModBiomeModifications.registerModBiomeModifications();
    }
}