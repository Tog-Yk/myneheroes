package net.togyk.myneheroes.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.entity.LaserEntity;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class LaserEntityRenderer extends ProjectileEntityRenderer<LaserEntity> {
    public static final Identifier TEXTURE_INNER = Identifier.of(MyneHeroes.MOD_ID, "textures/entity/projectiles/laser_inner.png");
    public static final Identifier TEXTURE_OUTER = Identifier.of(MyneHeroes.MOD_ID, "textures/entity/projectiles/laser_outer.png");

    public LaserEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(LaserEntity entity) {
        return TEXTURE_INNER;
    }

    @Override
    public void render(LaserEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        // Apply transformations if necessary
        matrices.translate(0.0, 0.0, 0.0);

        // Render the base texture
        VertexConsumer baseConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE_INNER));
        renderBox(matrices, baseConsumer, 0xF000F0, 0.05f, entity.getInnerColor());

        // Render the emissive texture
        VertexConsumer emissiveConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE_OUTER));
        renderBox(matrices, emissiveConsumer, 0xF000F0, 0.075f, entity.getColor());

        matrices.pop();
    }


    private void renderBox(MatrixStack matrices, VertexConsumer vertexConsumer, int light, float size, int color) {
        MatrixStack.Entry entry = matrices.peek();

        // Front face
        vertexConsumer.vertex(entry.getPositionMatrix(), -size, -size, -size)
                .color(color)
                .texture(0.0f, 1.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(0.0f, 0.0f, -1.0f);
        vertexConsumer.vertex(entry.getPositionMatrix(), size, -size, -size)
                .color(color)
                .texture(1.0f, 1.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(0.0f, 0.0f, -1.0f);
        vertexConsumer.vertex(entry.getPositionMatrix(), size, size, -size)
                .color(color)
                .texture(1.0f, 0.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(0.0f, 0.0f, -1.0f);
        vertexConsumer.vertex(entry.getPositionMatrix(), -size, size, -size)
                .color(color)
                .texture(0.0f, 0.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(0.0f, 0.0f, -1.0f);

        // Back face
        vertexConsumer.vertex(entry.getPositionMatrix(), -size, -size, size)
                .color(color)
                .texture(0.0f, 1.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(0.0f, 0.0f, 1.0f);
        vertexConsumer.vertex(entry.getPositionMatrix(), size, -size, size)
                .color(color)
                .texture(1.0f, 1.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(0.0f, 0.0f, 1.0f);
        vertexConsumer.vertex(entry.getPositionMatrix(), size, size, size)
                .color(color)
                .texture(1.0f, 0.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(0.0f, 0.0f, 1.0f);
        vertexConsumer.vertex(entry.getPositionMatrix(), -size, size, size)
                .color(color)
                .texture(0.0f, 0.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(0.0f, 0.0f, 1.0f);

        // Left face
        vertexConsumer.vertex(entry.getPositionMatrix(), -size, -size, size)
                .color(color)
                .texture(0.0f, 1.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(-1.0f, 0.0f, 0.0f);
        vertexConsumer.vertex(entry.getPositionMatrix(), -size, -size, -size)
                .color(color)
                .texture(1.0f, 1.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(-1.0f, 0.0f, 0.0f);
        vertexConsumer.vertex(entry.getPositionMatrix(), -size, size, -size)
                .color(color)
                .texture(1.0f, 0.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(-1.0f, 0.0f, 0.0f);
        vertexConsumer.vertex(entry.getPositionMatrix(), -size, size, size)
                .color(color)
                .texture(0.0f, 0.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(-1.0f, 0.0f, 0.0f);

        // Right face
        vertexConsumer.vertex(entry.getPositionMatrix(), size, -size, size)
                .color(color)
                .texture(0.0f, 1.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(1.0f, 0.0f, 0.0f);
        vertexConsumer.vertex(entry.getPositionMatrix(), size, -size, -size)
                .color(color)
                .texture(1.0f, 1.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(1.0f, 0.0f, 0.0f);
        vertexConsumer.vertex(entry.getPositionMatrix(), size, size, -size)
                .color(color)
                .texture(1.0f, 0.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(1.0f, 0.0f, 0.0f);
        vertexConsumer.vertex(entry.getPositionMatrix(), size, size, size)
                .color(color)
                .texture(0.0f, 0.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(1.0f, 0.0f, 0.0f);

        // Top face
        vertexConsumer.vertex(entry.getPositionMatrix(), -size, size, size)
                .color(color)
                .texture(0.0f, 1.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(0.0f, 1.0f, 0.0f);
        vertexConsumer.vertex(entry.getPositionMatrix(), size, size, size)
                .color(color)
                .texture(1.0f, 1.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(0.0f, 1.0f, 0.0f);
        vertexConsumer.vertex(entry.getPositionMatrix(), size, size, -size)
                .color(color)
                .texture(1.0f, 0.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(0.0f, 1.0f, 0.0f);
        vertexConsumer.vertex(entry.getPositionMatrix(), -size, size, -size)
                .color(color)
                .texture(0.0f, 0.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(0.0f, 1.0f, 0.0f);

        // Bottom face
        vertexConsumer.vertex(entry.getPositionMatrix(), -size, -size, size)
                .color(color)
                .texture(0.0f, 1.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(0.0f, -1.0f, 0.0f);
        vertexConsumer.vertex(entry.getPositionMatrix(), size, -size, size)
                .color(color)
                .texture(1.0f, 1.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(0.0f, -1.0f, 0.0f);
        vertexConsumer.vertex(entry.getPositionMatrix(), size, -size, -size)
                .color(color)
                .texture(1.0f, 0.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(0.0f, -1.0f, 0.0f);
        vertexConsumer.vertex(entry.getPositionMatrix(), -size, -size, -size)
                .color(color)
                .texture(0.0f, 0.0f)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(0.0f, -1.0f, 0.0f);
    }
}