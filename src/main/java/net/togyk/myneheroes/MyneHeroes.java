package net.togyk.myneheroes;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.togyk.myneheroes.Item.ModArmorMaterials;
import net.togyk.myneheroes.Item.ModGeckoItems;
import net.togyk.myneheroes.Item.ModItemGroups;
import net.togyk.myneheroes.Item.ModItems;
import net.togyk.myneheroes.Item.custom.DyeableAdvancedGeckoArmorItem;
import net.togyk.myneheroes.Item.custom.ReactorItem;
import net.togyk.myneheroes.ability.Abilities;
import net.togyk.myneheroes.block.ModBlockEntityTypes;
import net.togyk.myneheroes.block.ModBlocks;
import net.togyk.myneheroes.block.entity.ArmorDyeingBlockEntity;
import net.togyk.myneheroes.block.entity.ArmorLightLevelerBlockEntity;
import net.togyk.myneheroes.block.screen.ModScreenHandlerTypes;
import net.togyk.myneheroes.component.ModDataComponentTypes;
import net.togyk.myneheroes.entity.ModEntities;
import net.togyk.myneheroes.event.ModEvents;
import net.togyk.myneheroes.networking.ModMessages;
import net.togyk.myneheroes.power.Powers;
import net.togyk.myneheroes.util.ModLootTableModifiers;
import net.togyk.myneheroes.worldgen.ModBiomeModifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MyneHeroes implements ModInitializer {
    public static final String MOD_ID = "myneheroes";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to Use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        Abilities.registerAbilities();
        Powers.registerPowers();

        ModItems.registerModItems();
        if (FabricLoader.getInstance().isModLoaded("geckolib")) {
            ModGeckoItems.registerModItems();
        }

        ModBlocks.registerModBlocks();
        ModItemGroups.registerItemGroups();

        ModBiomeModifications.registerModBiomeModifications();
        ModEntities.registerModEntities();

        ModMessages.registerServerMessages();
        ModEvents.registerEvents();

        ModDataComponentTypes.registerDataComponentTypes();

        ModBlockEntityTypes.registerModBlockEntityTypes();
        ModScreenHandlerTypes.registerModScreenHandlerTypes();

        ModLootTableModifiers.modifyLootTables();

        ItemStorage.SIDED.registerForBlockEntity(ArmorDyeingBlockEntity::getInventoryProvider, ModBlockEntityTypes.ARMOR_DYEING_BLOCK_ENTITY);
        ItemStorage.SIDED.registerForBlockEntity(ArmorLightLevelerBlockEntity::getInventoryProvider, ModBlockEntityTypes.ARMOR_LIGHT_LEVELER_BLOCK_ENTITY);
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