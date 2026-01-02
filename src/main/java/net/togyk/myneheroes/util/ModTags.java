package net.togyk.myneheroes.util;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.registry.ModRegistryKeys;
import net.togyk.myneheroes.upgrade.Upgrade;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> NOT_METEOR_REPLACEABLE = createTag("not_meteor_replaceable");

        public static final TagKey<Block> METEOR_CORE_BLOCKS = createTag("meteor_core_blocks");
        public static final TagKey<Block> METEOR_CRUST_BLOCKS = createTag("meteor_crust_blocks");
        public static final TagKey<Block> KRYPTONITE_METEOR_CRUST_BLOCKS = createTag("kryptonite_meteor_crust_blocks");
        public static final TagKey<Block> KRYPTONITE_METEOR_CORE_BLOCKS = createTag("kryptonite_meteor_core_blocks");
        public static final TagKey<Block> SCULK_METEOR_CRUST_BLOCKS = createTag("sculk_meteor_crust_blocks");
        public static final TagKey<Block> SCULK_METEOR_CORE_BLOCKS = createTag("sculk_meteor_core_blocks");
        public static final TagKey<Block> VIBRANIUM_METEOR_CRUST_BLOCKS = createTag("vibranium_meteor_crust_blocks");
        public static final TagKey<Block> VIBRANIUM_METEOR_CORE_BLOCKS = createTag("vibranium_meteor_core_blocks");

        public static final TagKey<Block> RADIATION_GOES_THROUGH = createTag("radiation_goes_trough");
        public static final TagKey<Block> INCORRECT_FOR_ADAMANTIUM_TOOL = createTag("incorrect_for_adamantium_tool");

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(MyneHeroes.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> REACTOR_FUEL = createTag("reactor_fuel");
        public static final TagKey<Item> COLORING_FUEL = createTag("coloring_fuel");
        public static final TagKey<Item> DYES = createTag("dyes");

        public static final TagKey<Item> CAN_BE_REPLACED_BY_TEMPORARY_ITEMS = createTag("can_be_replaced_by_temporary_items");
        public static final TagKey<Item> CANT_BE_PLACED_IN_ITEM_FRAME = createTag("cant_be_placed_in_item_frame");
        public static final TagKey<Item> CANT_BE_PLACED_IN_INVENTORIES = createTag("cant_be_placed_in_inventories");

        public static final TagKey<Item> SHIELDS = createTag("shields");
        public static final TagKey<Item> KATANAS = createTag("katanas");

        public static final TagKey<Item> ONLY_LOYALTY = createTag("only_loyalty");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(MyneHeroes.MOD_ID, name));
        }
    }

    public static class Entity {
        public static final TagKey<EntityType<?>> SMALL_MONSTERS = createTag("small_monsters");
        public static final TagKey<EntityType<?>> MEDIUM_MONSTERS = createTag("medium_monsters");
        public static final TagKey<EntityType<?>> LARGE_MONSTERS = createTag("large_monsters");

        public static final TagKey<EntityType<?>> FRIENDLY = createTag("friendly");
        public static final TagKey<EntityType<?>> PETS = createTag("pets");

        private static TagKey<EntityType<?>> createTag(String name) {
            return TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(MyneHeroes.MOD_ID, name));
        }
    }

    public static class Abilities {

        private static TagKey<Ability> createTag(String name) {
            return TagKey.of(ModRegistryKeys.ABILITY, Identifier.of(MyneHeroes.MOD_ID, name));
        }
    }

    public static class Powers {
        public static final TagKey<Power> RADIATION_OBTAINABLE = createTag("radiation_obtainable");
        public static final TagKey<Power> RARE_RADIATION_OBTAINABLE = createTag("rare_radiation_obtainable");
        public static final TagKey<Power> COMMON_RADIATION_OBTAINABLE = createTag("common_radiation_obtainable");
        public static final TagKey<Power> OFTEN_RADIATION_OBTAINABLE = createTag("often_radiation_obtainable");
        public static final TagKey<Power> MUTANT = createTag("mutant");
        public static final TagKey<Power> RARE_MUTANT = createTag("rare_mutant");
        public static final TagKey<Power> COMMON_MUTANT = createTag("common_mutant");
        public static final TagKey<Power> OFTEN_MUTANT = createTag("often_mutant");

        public static final TagKey<Power> SPIDER = createTag("spider");

        private static TagKey<Power> createTag(String name) {
            return TagKey.of(ModRegistryKeys.POWER, Identifier.of(MyneHeroes.MOD_ID, name));
        }
    }

    public static class Upgrades {

        private static TagKey<Upgrade> createTag(String name) {
            return TagKey.of(ModRegistryKeys.UPGRADE, Identifier.of(MyneHeroes.MOD_ID, name));
        }
    }
}
