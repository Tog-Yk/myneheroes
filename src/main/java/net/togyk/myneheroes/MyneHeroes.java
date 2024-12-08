package net.togyk.myneheroes;

import net.fabricmc.api.ModInitializer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.togyk.myneheroes.Item.ModItemGroups;
import net.togyk.myneheroes.Item.ModItems;
import net.togyk.myneheroes.Item.custom.ReactorItem;
import net.togyk.myneheroes.block.ModBlocks;
import net.togyk.myneheroes.networking.ModMessages;
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

        ModMessages.registerServerMessages();
    }
    /**
     * Searches the player's inventory for the first matching item.
     *
     * @param player The player to search in.
     * @return The matching ItemStack, or an empty ItemStack if not found.
     */
    public static ItemStack getReactorItemClass(PlayerEntity player) {
        PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.getItem() instanceof ReactorItem) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}