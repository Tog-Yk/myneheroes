package net.togyk.myneheroes.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.entity.ModEntities;

@Environment(EnvType.CLIENT)
public class ModEntityRenderers {
    public static void registerModEntityRenderers() {
        MyneHeroes.LOGGER.info("Registering Mod Entities for " + MyneHeroes.MOD_ID);
        EntityRendererRegistry.INSTANCE.register(ModEntities.LASER, (context) -> {
            return new LaserEntityRenderer(context);
        });
    }
}
