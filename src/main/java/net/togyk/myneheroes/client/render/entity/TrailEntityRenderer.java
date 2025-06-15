package net.togyk.myneheroes.client.render.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.entity.trail.TrailEntity;

import java.util.Optional;
import java.util.UUID;

public class TrailEntityRenderer<T extends TrailEntity> extends EntityRenderer<T> {
    private final PlayerEntityRenderer playerRenderer;
    public TrailEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        playerRenderer = new PlayerEntityRenderer(ctx, true) {
            @Override
            protected boolean hasLabel(AbstractClientPlayerEntity livingEntity) {
                return false;
            }
        };
    }


    @Override
    public void render(T entity, float entityYaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int packedLight) {
        Optional<UUID> parentUuid = entity.getParentUuid();
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.getInstance().player;
        if (parentUuid.isPresent() && player != null && parentUuid.get().equals(player.getUuid()) && client.options.getPerspective().isFirstPerson() && player.getPos().squaredDistanceTo(entity.getPos()) < 3) {
            return;
        }
        entity.render(entityYaw, tickDelta, matrixStack, vertexConsumerProvider, packedLight, playerRenderer);
    }

    @Override
    public Identifier getTexture(T entity) {
        return null;
    }
}
