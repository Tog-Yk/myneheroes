package net.togyk.myneheroes.entity.trail;

import com.google.common.base.Predicates;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public abstract class TrailEntity extends Entity {
    private static final TrackedData<Integer> lifetime = DataTracker.registerData(TrailEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Optional<UUID>> connectedSegment = DataTracker.registerData(TrailEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final TrackedData<Optional<UUID>> parentUuid = DataTracker.registerData(TrailEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);


    public TrailEntity(EntityType<? extends TrailEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
    }

    public TrailEntity(Entity parent, Optional<UUID> connectedSegmentUuid, int lifetime, EntityType<? extends TrailEntity> type) {
        this(type, parent.getWorld());

        this.setParent(Optional.of(parent));
        this.setPose(parent.getPose());
        this.refreshPosition();
        this.setPos(parent.getPos().x, parent.getPos().y, parent.getPos().z);
        this.setRotation(parent.getYaw(), parent.getPitch());

        this.setConnectedSegment(connectedSegmentUuid);

        this.setLifetime(lifetime);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.age >= this.getLifetime()) {
            this.discard();
        }
    }

    public abstract void render(float entityYaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int packedLight, PlayerEntityRenderer playerRenderer);


    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(lifetime, -1);
        builder.add(connectedSegment, Optional.empty());
        builder.add(parentUuid, Optional.empty());
    }

    @NotNull
    public int getLifetime() {
        return this.getDataTracker().get(lifetime);
    }

    public void setLifetime(int lifetime) {
        this.getDataTracker().set(TrailEntity.lifetime, lifetime);
    }


    public Optional<TrailEntity> getConnectedSegment() {
        Optional<UUID> uuid = this.getDataTracker().get(connectedSegment);
        if (!uuid.isPresent()) {
            return Optional.empty();
        }
        Box box = new Box(this.getBlockPos()).expand(25);
        return this.getWorld().getEntitiesByClass(TrailEntity.class, box, Predicates.alwaysTrue()).stream().filter(entity -> uuid.get().equals(entity.getUuid())).findFirst();
    }

    public Optional<UUID> getConnectedSegmentUuid() {
        return this.getDataTracker().get(connectedSegment);
    }

    public void setConnectedSegment(Optional<UUID> segment) {
        this.getDataTracker().set(connectedSegment, segment);
    }


    public Optional<Entity> getParent() {
        Optional<UUID> uuid = this.getDataTracker().get(parentUuid);
        if (!uuid.isPresent()) {
            return Optional.empty();
        }
        PlayerEntity player = this.getWorld().getPlayerByUuid(uuid.get());
        if (player != null) {
            return Optional.of(player);
        }
        return this.getWorld().getEntitiesByClass(Entity.class, this.getBoundingBox().expand(25), Predicates.alwaysTrue()).stream().filter(entity -> uuid.equals(entity.getUuid())).findFirst();
    }

    public void setParent(Optional<Entity> parent) {
        if (!parent.isPresent()) {
            this.getDataTracker().set(parentUuid, Optional.empty());
        } else {
            this.getDataTracker().set(parentUuid, Optional.of(parent.get().getUuid()));
        }
    }

    public Optional<UUID> getParentUuid() {
        return this.getDataTracker().get(parentUuid);
    }

    public void setParentUuid(Optional<UUID> parent) {
        this.getDataTracker().set(parentUuid, parent);
    }

    public void setConnectedSegmentUuid(Optional<UUID> uuid) {
        this.getDataTracker().set(connectedSegment, uuid);
    }



    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return true;
    }

    @Override
    public void pushAwayFrom(Entity entity) {
    }

    @Override
    protected void pushOutOfBlocks(double x, double y, double z) {
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    @Override
    public boolean shouldRender(double distance) {
        return true;
    }

    @Override
    public Box getVisibilityBoundingBox() {
        return super.getVisibilityBoundingBox();
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("lifetime")) {
            this.setLifetime(nbt.getInt("lifetime"));
        }

        if (nbt.contains("connectedSegment")) {
            this.setConnectedSegmentUuid(Optional.of(nbt.getUuid("connectedSegment")));
        }

        if (nbt.contains("parent_uuid")) {
            this.setParentUuid(Optional.of(nbt.getUuid("parent_uuid")));
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("lifetime", this.getLifetime());

        if (this.getConnectedSegmentUuid().isPresent()) {
            nbt.putUuid("connectedSegment", this.getConnectedSegmentUuid().get());
        }

        if (this.getParentUuid().isPresent()) {
            nbt.putUuid("parent_uuid", this.getParentUuid().get());
        }
    }
}
