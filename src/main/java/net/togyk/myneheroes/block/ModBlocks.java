package net.togyk.myneheroes.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.block.custom.*;

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


    public static final Block URANIUM_BLOCK = registerBlock("uranium_block",
            new Block(AbstractBlock.Settings.create().strength(4f)
                    .requiresTool().sounds(BlockSoundGroup.METAL)));

    public static final Block RAW_URANIUM_BLOCK = registerBlock("raw_uranium_block",
            new Block(AbstractBlock.Settings.create().strength(3f)
                    .requiresTool()));
    public static final Block URANIUM_ORE = registerBlock("uranium_ore",
            new Block(AbstractBlock.Settings.create().strength(3f)
                    .requiresTool().sounds(BlockSoundGroup.STONE)));
    public static final Block DEEPSLATE_URANIUM_ORE = registerBlock("deepslate_uranium_ore",
            new Block(AbstractBlock.Settings.create().strength(3f)
                    .requiresTool().sounds(BlockSoundGroup.DEEPSLATE)));

    public static final Block ARMOR_DYEING_STATION = registerBlock("armor_dyeing_station",
            new ArmorDyeingBlockEntityBlock(AbstractBlock.Settings.create().instrument(NoteBlockInstrument.BASS).strength(2.5F).sounds(BlockSoundGroup.WOOD).burnable()));
    public static final Block ARMOR_LIGHT_LEVELING_STATION = registerBlock("armor_light_leveling_station",
            new ArmorLightLevelerBlockEntityBlock(AbstractBlock.Settings.create().instrument(NoteBlockInstrument.BASS).strength(2.5F).sounds(BlockSoundGroup.WOOD).burnable()));

    public static final Block KRYPTONITE_BlOCK = registerBlock("kryptonite_block",
            new KryptoniteBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.AMETHYST_BLOCK).mapColor(MapColor.LIME).strength(2.5f)));

    public static final Block KRYPTONITE_CLUSTER = registerBlock("kryptonite_cluster",
            new KryptoniteClusterBlock(AbstractBlock.Settings.create().mapColor(MapColor.LIME).solid().nonOpaque().sounds(BlockSoundGroup.AMETHYST_CLUSTER).strength(1.5F).luminance((state) -> 5).pistonBehavior(PistonBehavior.DESTROY)));


    public static final Block METEOR_RADAR = registerBlock("meteor_radar",
            new MeteorRadarBlockEntityBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.METAL).mapColor(MapColor.LIME).strength(2.5f)));

    public static final Block UPGRADE_STATION = registerBlock("upgrade_station",
            new UpgradeStationBlockEntityBlock(AbstractBlock.Settings.create().mapColor(MapColor.IRON_GRAY).requiresTool().strength(5.0F, 600.0F).sounds(BlockSoundGroup.METAL).pistonBehavior(PistonBehavior.BLOCK)));

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