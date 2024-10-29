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
    public static final ItemGroup MYNEHEROES_ITEMS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(MyneHeroes.MOD_ID, "myneheroes"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.VIBRANIUM_INGOT))
                    .displayName(Text.translatable("itemgroup.myneheroes.myneheroes"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.VIBRANIUM_INGOT);
                        entries.add(ModItems.RAW_VIBRANIUM);

                        entries.add(ModItems.IRON_SUIT_TEMPLATE);

                        entries.add(ModBlocks.VIBRANIUM_BLOCK);
                        entries.add(ModBlocks.RAW_VIBRANIUM_BLOCK);
                        entries.add(ModBlocks.VIBRANIUM_ORE);
                        entries.add(ModBlocks.DEEPSLATE_VIBRANIUM_ORE);

                    }).build());
    public static void registerItemGroups() {
        MyneHeroes.LOGGER.info("Registering Item Groups for " + MyneHeroes.MOD_ID);
    }
}