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
        public static final TagKey<Block> COMET_CORE_BLOCKS = createTag("comet_core_blocks");
        public static final TagKey<Block> COMET_CRUST_BLOCKS = createTag("comet_crust_blocks");
        public static final TagKey<Block> KRYPTONITE_COMET_CRUST_BLOCKS = createTag("kryptonite_comet_crust_blocks");
        public static final TagKey<Block> KRYPTONITE_COMET_CORE_BLOCKS = createTag("kryptonite_comet_core_blocks");
        public static final TagKey<Block> SCULK_COMET_CRUST_BLOCKS = createTag("sculk_comet_crust_blocks");
        public static final TagKey<Block> SCULK_COMET_CORE_BLOCKS = createTag("sculk_comet_core_blocks");
        public static final TagKey<Block> VIBRANIUM_COMET_CRUST_BLOCKS = createTag("vibranium_comet_crust_blocks");
        public static final TagKey<Block> VIBRANIUM_COMET_CORE_BLOCKS = createTag("vibranium_comet_core_blocks");

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(MyneHeroes.MOD_ID, name));
        }
    }

    public static class Items {

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(MyneHeroes.MOD_ID, name));
        }
    }

    public static class Entity {
        public static final TagKey<EntityType<?>> COMETS = createTag("comets");

        private static TagKey<EntityType<?>> createTag(String name) {
            return TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(MyneHeroes.MOD_ID, name));
        }
    }
}
