package net.togyk.myneheroes.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.block.entity.ArmorDyeingBlockEntity;
import net.togyk.myneheroes.block.entity.ArmorLightLevelerBlockEntity;

public class ModBlockEntityTypes {
    public static final BlockEntityType<ArmorDyeingBlockEntity> ARMOR_DYEING_BLOCK_ENTITY = register("armor_dyeing_block_entity",
            BlockEntityType.Builder.create(ArmorDyeingBlockEntity::new, ModBlocks.ARMOR_DYEING_STATION)
                    .build());
    public static final BlockEntityType<ArmorLightLevelerBlockEntity> ARMOR_LIGHT_LEVELER_BLOCK_ENTITY = register("armor_light_leveler_block_entity",
            BlockEntityType.Builder.create(ArmorLightLevelerBlockEntity::new, ModBlocks.ARMOR_LIGHT_LEVELING_STATION)
                    .build());

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(MyneHeroes.MOD_ID, name), type);
    }
    public static void registerModBlockEntityTypes() {
        MyneHeroes.LOGGER.info("Registering Block Entity Types for " + MyneHeroes.MOD_ID);
    }
}
