package net.togyk.myneheroes.Item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.block.ModBlocks;

public class ModItemGroups {
    public static final ItemGroup MYNEHEROES_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(MyneHeroes.MOD_ID, "myneheroes"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.VIBRANIUM_INGOT))
                    .displayName(Text.translatable("itemgroup.myneheroes.myneheroes"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.VIBRANIUM_INGOT);
                        entries.add(ModItems.RAW_VIBRANIUM);

                        entries.add(ModBlocks.VIBRANIUM_BLOCK);
                        entries.add(ModBlocks.RAW_VIBRANIUM_BLOCK);
                        entries.add(ModBlocks.VIBRANIUM_ORE);
                        entries.add(ModBlocks.DEEPSLATE_VIBRANIUM_ORE);

                        entries.add(ModItems.TITANIUM_INGOT);
                        entries.add(ModItems.RAW_TITANIUM);

                        entries.add(ModBlocks.TITANIUM_BLOCK);
                        entries.add(ModBlocks.RAW_TITANIUM_BLOCK);
                        entries.add(ModBlocks.TITANIUM_ORE);
                        entries.add(ModBlocks.DEEPSLATE_TITANIUM_ORE);

                        entries.add(ModItems.GOLD_TITANIUM_INGOT);;
                        entries.add(ModItems.RAW_GOLD_TITANIUM);;

                        entries.add(ModBlocks.GOLD_TITANIUM_BLOCK);
                        entries.add(ModBlocks.RAW_GOLD_TITANIUM_BLOCK);

                        entries.add(ModItems.IRON_SUIT_TEMPLATE);

                        entries.add(ModItems.ARC_REACTOR);

                        entries.add(ModItems.ABILITY_HOLDING);
                        entries.add(ModItems.LAZAR_HOLDING);

                        entries.add(ModItems.POWER_INJECTION);

                        entries.add(ModBlocks.ARMOR_DYEING_STATION);
                        entries.add(ModBlocks.ARMOR_LIGHT_LEVELING_STATION);

                        entries.add(ModItems.VIBRANIUM_SHIELD);
                        entries.add(ModItems.A_SYMBOLS_SHIELD);
                        entries.add(ModItems.COSMIC_SHIELD);

                        entries.add(ModItems.HUD_UPGRADE);
                        entries.add(ModItems.LAZAR_UPGRADE);
                    }).build());

    public static final ItemGroup MYNEHEROES_ARMOR_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(MyneHeroes.MOD_ID, "myneheroes_armor"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.VIBRANIUM_CHESTPLATE))
                    .displayName(Text.translatable("itemgroup.myneheroes.myneheroes_armor"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.MARK6_VIBRANIUM_HELMET);
                        entries.add(ModItems.MARK6_VIBRANIUM_CHESTPLATE);
                        entries.add(ModItems.MARK6_VIBRANIUM_LEGGINGS);
                        entries.add(ModItems.MARK6_VIBRANIUM_BOOTS);

                        entries.add(ModItems.VIBRANIUM_HELMET);
                        entries.add(ModItems.VIBRANIUM_CHESTPLATE);
                        entries.add(ModItems.VIBRANIUM_LEGGINGS);
                        entries.add(ModItems.VIBRANIUM_BOOTS);

                        entries.add(ModItems.TITANIUM_HELMET);
                        entries.add(ModItems.TITANIUM_CHESTPLATE);
                        entries.add(ModItems.TITANIUM_LEGGINGS);
                        entries.add(ModItems.TITANIUM_BOOTS);

                        entries.add(ModItems.GOLD_TITANIUM_HELMET);
                        entries.add(ModItems.GOLD_TITANIUM_CHESTPLATE);
                        entries.add(ModItems.GOLD_TITANIUM_LEGGINGS);
                        entries.add(ModItems.GOLD_TITANIUM_BOOTS);

                    }).build());


    public static void registerItemGroups() {
        MyneHeroes.LOGGER.info("Registering Item Groups for " + MyneHeroes.MOD_ID);
    }
}