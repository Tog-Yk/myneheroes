package net.togyk.myneheroes.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.entity.trail.AfterimageTrailEntity;
import net.togyk.myneheroes.entity.trail.LightningTrailEntity;

public class ModEntities {
    public static final EntityType<LaserEntity> LASER = registerEntity("laser",
            EntityType.Builder.create(LaserEntity::new, SpawnGroup.MISC).dimensions(0.3F, 0.3F));

    public static final EntityType<StationaryItemEntity> STATIONARY_ITEM = registerEntity("stationary_item",
            EntityType.Builder.<StationaryItemEntity>create(StationaryItemEntity::new, SpawnGroup.MISC).dimensions(0.75F, 0.4F));

    public static final EntityType<ThrownItemEntity> THROWN_ITEM = registerEntity("thrown_item",
            EntityType.Builder.<ThrownItemEntity>create(ThrownItemEntity::new, SpawnGroup.MISC).dimensions(0.75F, 0.4F));

    public static final EntityType<StationaryArmorEntity> STATIONARY_ARMOR = registerEntity("stationary_armor",
            EntityType.Builder.create(StationaryArmorEntity::new, SpawnGroup.MISC)
                    .dimensions(0.6F, 1.8F));

    public static final EntityType<MeteorEntity> METEOR = registerEntity("meteor",
            EntityType.Builder.<MeteorEntity>create(MeteorEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5F, 0.5F)
                    .maxTrackingRange(1200)
                    .spawnableFarFromPlayer()
                    .alwaysUpdateVelocity(true));

    public static final EntityType<LightningTrailEntity> LIGHTNING_TRAIL = registerEntity("lightning_trail",
            EntityType.Builder.<LightningTrailEntity>create(LightningTrailEntity::new, SpawnGroup.MISC));

    public static final EntityType<AfterimageTrailEntity> AFTERIMAGE_TRAIL = registerEntity("after_image_trail",
            EntityType.Builder.<AfterimageTrailEntity>create(AfterimageTrailEntity::new, SpawnGroup.MISC));

    private static <T extends Entity> EntityType<T> registerEntity(String name, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of(MyneHeroes.MOD_ID, name), type.build());
    }
    public static void registerModEntities() {
        MyneHeroes.LOGGER.info("Registering Mod Entities for " + MyneHeroes.MOD_ID);
    }
}
