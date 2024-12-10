package net.togyk.myneheroes.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.entity.LaserEntity;

@Environment(EnvType.CLIENT)
public class LaserEntityRenderer extends ProjectileEntityRenderer<LaserEntity> {
    public static final Identifier TEXTURE = Identifier.of(MyneHeroes.MOD_ID,"textures/entity/projectiles/laser.png");

    public LaserEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(LaserEntity entity) {
        return TEXTURE;
    }
}
