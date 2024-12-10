package net.togyk.myneheroes.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;

public class ModEntities {
    public static final EntityType<LaserEntity> LASER = registerEntity("laser",
            EntityType.Builder.create(LaserEntity::new, SpawnGroup.MISC).dimensions(0.3F, 0.3F));

    private static <T extends Entity> EntityType<T> registerEntity(String name, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of(MyneHeroes.MOD_ID, name), type.build(name));
    }
    public static void registerModEntities() {
        MyneHeroes.LOGGER.info("Registering Mod Entities for " + MyneHeroes.MOD_ID);
    }
}
