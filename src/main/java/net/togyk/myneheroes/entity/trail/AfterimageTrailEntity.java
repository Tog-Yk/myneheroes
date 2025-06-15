package net.togyk.myneheroes.entity.trail;

import com.mojang.blaze3d.systems.RenderSystem;
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
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.togyk.myneheroes.entity.ModEntities;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class AfterimageTrailEntity extends TrailEntity {
    private static final TrackedData<Float> alpha = DataTracker.registerData(AfterimageTrailEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<NbtCompound> imageNbt = DataTracker.registerData(AfterimageTrailEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);

    public AfterimageTrailEntity(EntityType<AfterimageTrailEntity> type, World world) {
        super(type, world);
    }

    public AfterimageTrailEntity(Entity parent, Optional<UUID> connectedSegment, int lifetime, float alpha) {
        super(parent, connectedSegment, lifetime, ModEntities.AFTERIMAGE_TRAIL);
        this.setAlpha(alpha);
        this.setImage(parent);
    }

    @Override
    public void render(float entityYaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int packedLight, PlayerEntityRenderer playerRenderer) {
        Entity image = this.getImage();
        if (image != null) {
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (1.0F - (float) this.age / this.getLifetime()) * this.getAlpha());

            renderAfterImage(image, entityYaw, matrixStack, vertexConsumerProvider, packedLight, playerRenderer);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);
            RenderSystem.disableBlend();
        }
    }

    private void renderAfterImage(Entity afterImage, float entityYaw, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, PlayerEntityRenderer playerRenderer) {
        if (this.getParent().isPresent()) {
            matrices.push();
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

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(alpha, 0.5F);
        builder.add(imageNbt, new NbtCompound());
    }

    @NotNull
    public float getAlpha() {
        return this.getDataTracker().get(alpha);
    }

    public void setAlpha(float alpha) {
        this.getDataTracker().set(AfterimageTrailEntity.alpha, alpha);
    }

    public Entity getImage() {
        NbtCompound nbt = this.getDataTracker().get(imageNbt);

        Optional<Entity> parent = this.getParent();
        if (parent.isPresent() && parent.get() instanceof PlayerEntity oldPlayer) {
            OtherClientPlayerEntity player = new OtherClientPlayerEntity((ClientWorld) this.getWorld(), oldPlayer.getGameProfile());
            player.getInventory().readNbt(nbt.getList("Myneheroes$Inventory", NbtElement.COMPOUND_TYPE));
            player.readNbt(nbt);
            return player;
        } else {
            EntityType<?> type = EntityType.get(nbt.getString("id")).orElse(null);
            if (type != null) {
                Entity snapshotEntity = type.create(this.getWorld());
                if (snapshotEntity != null) {
                    snapshotEntity.readNbt(nbt);
                }
                return snapshotEntity;
            }
        }
        return null;
    }

    public void setImage(Entity image) {
        NbtCompound nbt = new NbtCompound();
        image.saveSelfNbt(nbt);

        if (image instanceof PlayerEntity player) {
            EntityType<?> entityType = player.getType();
            Identifier identifier = EntityType.getId(entityType);
            if (identifier != null) {
                nbt.putString("id", identifier.toString());
                nbt.put("Myneheroes$Inventory", player.getInventory().writeNbt(new NbtList()));
                player.writeNbt(nbt);
            }
        }

        this.getDataTracker().set(AfterimageTrailEntity.imageNbt, nbt);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putFloat("alpha", this.getAlpha());

        nbt.put("image_nbt", this.getDataTracker().get(imageNbt));
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("alpha")) {
            this.setAlpha(nbt.getFloat("alpha"));
        }

        if (nbt.contains("image_nbt")) {
            this.getDataTracker().set(imageNbt, nbt.getCompound("image_nbt"));
        }
    }
}
