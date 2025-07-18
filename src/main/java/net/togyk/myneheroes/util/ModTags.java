package net.togyk.myneheroes.util;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> METEOR_CORE_BLOCKS = createTag("meteor_core_blocks");
        public static final TagKey<Block> METEOR_CRUST_BLOCKS = createTag("meteor_crust_blocks");
        public static final TagKey<Block> KRYPTONITE_METEOR_CRUST_BLOCKS = createTag("kryptonite_meteor_crust_blocks");
        public static final TagKey<Block> KRYPTONITE_METEOR_CORE_BLOCKS = createTag("kryptonite_meteor_core_blocks");
        public static final TagKey<Block> SCULK_METEOR_CRUST_BLOCKS = createTag("sculk_meteor_crust_blocks");
        public static final TagKey<Block> SCULK_METEOR_CORE_BLOCKS = createTag("sculk_meteor_core_blocks");
        public static final TagKey<Block> VIBRANIUM_METEOR_CRUST_BLOCKS = createTag("vibranium_meteor_crust_blocks");
        public static final TagKey<Block> VIBRANIUM_METEOR_CORE_BLOCKS = createTag("vibranium_meteor_core_blocks");

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(MyneHeroes.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> REACTOR_FUEL = createTag("reactor_fuel");
        public static final TagKey<Item> COLORING_FUEL = createTag("coloring_fuel");
        public static final TagKey<Item> DYES = createTag("dyes");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(MyneHeroes.MOD_ID, name));
        }
    }

    public static class Entity {

        private static TagKey<EntityType<?>> createTag(String name) {
            return TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(MyneHeroes.MOD_ID, name));
        }
    }
}
