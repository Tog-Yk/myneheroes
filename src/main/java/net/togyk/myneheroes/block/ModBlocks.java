package net.togyk.myneheroes.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.block.custom.ArmorDyeingBlockEntityBlock;
import net.togyk.myneheroes.block.custom.ArmorLightLevelerBlockEntityBlock;

public class ModBlocks {
    public static final Block VIBRANIUM_BLOCK = registerBlock("vibranium_block",
            new Block(AbstractBlock.Settings.create().strength(4f)
                    .requiresTool().sounds(BlockSoundGroup.METAL)));

    public static final Block RAW_VIBRANIUM_BLOCK = registerBlock("raw_vibranium_block",
            new Block(AbstractBlock.Settings.create().strength(3f)
                    .requiresTool()));
    public static final Block VIBRANIUM_ORE = registerBlock("vibranium_ore",
            new Block(AbstractBlock.Settings.create().strength(3f)
                    .requiresTool().sounds(BlockSoundGroup.STONE)));
    public static final Block DEEPSLATE_VIBRANIUM_ORE = registerBlock("deepslate_vibranium_ore",
            new Block(AbstractBlock.Settings.create().strength(3f)
                    .requiresTool().sounds(BlockSoundGroup.DEEPSLATE)));

    public static final Block TITANIUM_BLOCK = registerBlock("titanium_block",
            new Block(AbstractBlock.Settings.create().strength(4f)
                    .requiresTool().sounds(BlockSoundGroup.METAL)));

    public static final Block RAW_TITANIUM_BLOCK = registerBlock("raw_titanium_block",
            new Block(AbstractBlock.Settings.create().strength(3f)
                    .requiresTool()));
    public static final Block TITANIUM_ORE = registerBlock("titanium_ore",
            new Block(AbstractBlock.Settings.create().strength(3f)
                    .requiresTool().sounds(BlockSoundGroup.STONE)));
    public static final Block DEEPSLATE_TITANIUM_ORE = registerBlock("deepslate_titanium_ore",
            new Block(AbstractBlock.Settings.create().strength(3f)
                    .requiresTool().sounds(BlockSoundGroup.DEEPSLATE)));

    public static final Block GOLD_TITANIUM_BLOCK = registerBlock("gold_titanium_block",
            new Block(AbstractBlock.Settings.create().strength(4f)
                    .requiresTool().sounds(BlockSoundGroup.METAL)));
    public static final Block RAW_GOLD_TITANIUM_BLOCK = registerBlock("raw_gold_titanium_block",
            new Block(AbstractBlock.Settings.create().strength(4f)
                    .requiresTool()));

    public static final Block ARMOR_DYEING_STATION = registerBlock("armor_dyeing_station",
            new ArmorDyeingBlockEntityBlock(AbstractBlock.Settings.create().strength(4f)
                    .requiresTool()));
    public static final Block ARMOR_LIGHT_LEVELING_STATION = registerBlock("armor_light_leveling_station",
            new ArmorLightLevelerBlockEntityBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.WOOD)));



    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(MyneHeroes.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(MyneHeroes.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        MyneHeroes.LOGGER.info("Registering Mod Blocks for " + MyneHeroes.MOD_ID);

    }
}