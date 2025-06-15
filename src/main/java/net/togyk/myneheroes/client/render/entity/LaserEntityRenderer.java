package net.togyk.myneheroes.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.client.render.LaserRenderer;
import net.togyk.myneheroes.entity.LaserEntity;

@Environment(EnvType.CLIENT)
public class LaserEntityRenderer extends ProjectileEntityRenderer<LaserEntity> {
    public LaserEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(LaserEntity entity) {
        return Identifier.of(MyneHeroes.MOD_ID, "textures/entity/projectiles/laser_inner.png");
    }

    @Override
    public void render(LaserEntity entity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
        matrixStack.push();
        matrixStack.translate(0, entity.getDimensions(EntityPose.STANDING).height() / 2, 0);
        LaserRenderer renderer = new LaserRenderer();
        renderer.render(matrixStack, vertexConsumers, entity.getVelocity(), 0.25F, 1.0F, entity.getInnerColor(), entity.getColor());

        matrixStack.pop();
    }
}