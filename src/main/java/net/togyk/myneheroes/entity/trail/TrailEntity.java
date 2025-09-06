package net.togyk.myneheroes.entity.trail;

import com.google.common.base.Predicates;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
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
    //EntityDimensions
    private static final TrackedData<Float> WIDTH = DataTracker.registerData(TrailEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> HEIGHT = DataTracker.registerData(TrailEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> EYE_HEIGHT = DataTracker.registerData(TrailEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Boolean> FIXED = DataTracker.registerData(TrailEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final TrackedData<Float> SCALE = DataTracker.registerData(TrailEntity.class, TrackedDataHandlerRegistry.FLOAT);


    public TrailEntity(EntityType<? extends TrailEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
    }

    public TrailEntity(Entity parent, Optional<UUID> connectedSegmentUuid, int lifetime, EntityType<? extends TrailEntity> type) {
        this(type, parent.getWorld());

        this.setParent(Optional.of(parent));
        this.setPose(parent.getPose());
        this.setEntityDimensions(parent.getDimensions(parent.getPose()));
        if (parent instanceof LivingEntity entity) {
            EntityAttributeInstance instance = entity.getAttributeInstance(EntityAttributes.GENERIC_SCALE);
            if (instance != null) {
                this.setScale((float) instance.getValue());
            }
        }

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

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(lifetime, -1);
        builder.add(connectedSegment, Optional.empty());
        builder.add(parentUuid, Optional.empty());

        builder.add(WIDTH, this.getType().getDimensions().width());
        builder.add(HEIGHT, this.getType().getDimensions().height());
        builder.add(EYE_HEIGHT, this.getType().getDimensions().height());
        builder.add(FIXED, false);

        builder.add(SCALE, 1.0F);
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

    public void setEntityDimensions(EntityDimensions dims) {
        this.getDataTracker().set(WIDTH, dims.width());
        this.getDataTracker().set(HEIGHT, dims.height());
        this.getDataTracker().set(EYE_HEIGHT, dims.eyeHeight());
        this.getDataTracker().set(FIXED, dims.fixed());
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        DataTracker dataTracker = this.getDataTracker();
        EntityDimensions dimensions = dataTracker.get(FIXED) ? EntityDimensions.fixed(dataTracker.get(WIDTH), dataTracker.get(HEIGHT)) : EntityDimensions.changing(dataTracker.get(WIDTH), dataTracker.get(HEIGHT));
        return dimensions.withEyeHeight(dataTracker.get(EYE_HEIGHT));
    }

    public float getScale() {
        return this.getDataTracker().get(SCALE);
    }

    public void setScale(float scale) {
        this.getDataTracker().set(SCALE, scale);
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


        DataTracker dataTracker = this.getDataTracker();

        if (nbt.contains("dimensions_width")) {
            dataTracker.set(WIDTH, nbt.getFloat("dimensions_width"));
        }
        if (nbt.contains("dimensions_height")) {
            dataTracker.set(HEIGHT, nbt.getFloat("dimensions_height"));
        }
        if (nbt.contains("dimensions_eye_height")) {
            dataTracker.set(EYE_HEIGHT, nbt.getFloat("dimensions_eye_height"));
        }
        if (nbt.contains("dimensions_fixed")) {
            dataTracker.set(FIXED, nbt.getBoolean("dimensions_fixed"));
        }

        if (nbt.contains("scale")) {
            dataTracker.set(SCALE, nbt.getFloat("scale"));
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

        //dimensions
        DataTracker dataTracker = this.getDataTracker();
        nbt.putFloat("dimensions_width", dataTracker.get(WIDTH));
        nbt.putFloat("dimensions_height", dataTracker.get(HEIGHT));
        nbt.putFloat("dimensions_eye_height", dataTracker.get(EYE_HEIGHT));
        nbt.putBoolean("dimensions_fixed", dataTracker.get(FIXED));

        nbt.putFloat("scale", dataTracker.get(SCALE));
    }
}
