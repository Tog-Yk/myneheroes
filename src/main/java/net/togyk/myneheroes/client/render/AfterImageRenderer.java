package net.togyk.myneheroes.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.entity.trail.AfterimageTrailEntity;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class AfterImageRenderer {
    public static void render(AfterimageTrailEntity trail, float entityYaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int packedLight, PlayerEntityRenderer playerRenderer) {
        Entity image = getImage(trail);
        if (image != null) {
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (1.0F - (float) trail.age / trail.getLifetime()) * trail.getAlpha());

            renderAfterImage(trail, image, entityYaw, matrixStack, vertexConsumerProvider, packedLight, playerRenderer);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);
            RenderSystem.disableBlend();
        }
    }

    private static void renderAfterImage(AfterimageTrailEntity trail, Entity afterImage, float entityYaw, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, PlayerEntityRenderer playerRenderer) {
        if (trail.getParent().isPresent()) {
            matrices.push();
            matrices.scale(trail.getScale(), trail.getScale(), trail.getScale());
            matrices.multiply(RotationAxis.NEGATIVE_Y.rotation((float) Math.toRadians(entityYaw)));
            if (afterImage instanceof AbstractClientPlayerEntity player) {
                playerRenderer.render(player, entityYaw, 0, matrices, vertexConsumers, light);
            } else {
                EntityRenderer<? super Entity> renderer = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(afterImage);
                if (renderer != null) {
                    Vec3d pos = afterImage.getPos();

                    matrices.translate(pos.x, pos.y, pos.z);

                    renderer.render(afterImage, entityYaw, 0, matrices, vertexConsumers, light);

                }
            }
            matrices.pop();
        }
    }

    public static Entity getImage(AfterimageTrailEntity trail) {
        NbtCompound nbt = trail.getImage();

        Optional<Entity> parent = trail.getParent();
        if (parent.isPresent() && parent.get() instanceof PlayerEntity oldPlayer) {
            OtherClientPlayerEntity player = new OtherClientPlayerEntity((ClientWorld) trail.getWorld(), oldPlayer.getGameProfile());
            player.getInventory().readNbt(nbt.getList("Myneheroes$Inventory", NbtElement.COMPOUND_TYPE));
            player.readNbt(nbt);
            return player;
        } else {
            EntityType<?> type = EntityType.get(nbt.getString("id")).orElse(null);
            if (type != null) {
                Entity snapshotEntity = type.create(trail.getWorld());
                if (snapshotEntity != null) {
                    snapshotEntity.readNbt(nbt);
                }
                return snapshotEntity;
            }
        }
        return null;
    }
}
