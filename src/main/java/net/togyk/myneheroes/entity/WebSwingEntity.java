package net.togyk.myneheroes.entity;

import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.togyk.myneheroes.Item.ModItems;
import net.togyk.myneheroes.entity.data.ModTrackedData;
import net.togyk.myneheroes.util.PlayerAbilities;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class WebSwingEntity extends PersistentProjectileEntity {
    private static final TrackedData<Boolean> landed = DataTracker.registerData(WebSwingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Float> range = DataTracker.registerData(WebSwingEntity.class, TrackedDataHandlerRegistry.FLOAT);

    private static final TrackedData<Optional<UUID>> hitEntity = DataTracker.registerData(WebSwingEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final TrackedData<Vec3d> relativePos = DataTracker.registerData(WebSwingEntity.class, ModTrackedData.VEC3D);

    private static final TrackedData<Integer> age = DataTracker.registerData(WebSwingEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public WebSwingEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public WebSwingEntity(World world, Entity entity) {
        super(ModEntities.SWING_WEB, world);
        setOwner(entity);
    }

    @Override
    public void tick() {
        super.tick();

        Entity owner = this.getOwner();
        float range = this.getRange();

        //swing
        if (this.landed()) {
            if (owner != null) {
                //going (up and) down
                if (!owner.isOnGround()) {
                    if (owner.isSneaking()) {
                        this.setRange(range + 0.1F);
                    } else if (owner instanceof PlayerAbilities playerAbilities && playerAbilities.myneheroes$isHoldingJump()) {
                        this.setRange(Math.max(0.0F, range - 0.1F));
                    }
                }

                //actual swing
                Vec3d pos = owner.getBoundingBox().getCenter();
                Vec3d toAnchor = this.getBoundingBox().getCenter().subtract(pos);
                double dist = toAnchor.length();
                double pullStrength = Math.clamp((dist - range), 0.0D, 7.D);

                Vec3d desiredVel = toAnchor.normalize().multiply(0.1 * pullStrength);
                Vec3d currentVel = owner.getVelocity();
                Vec3d newVel = currentVel.add(desiredVel.x, desiredVel.y, desiredVel.z).multiply(0.95);
                if (newVel.y >= 0) {
                    owner.fallDistance = 0;
                }
                owner.setVelocity(newVel);
            }
        }

        //pull the hit entity to the owner

        Optional<Entity> optionalEntity = this.getHitEntity();
        if (optionalEntity.isPresent() && owner != null) {
            Entity entity = optionalEntity.get();

            Vec3d relativePos = this.getRelativePos();
            Vec3d newPos = entity.getPos().add(relativePos);
            this.setPos(newPos.x, newPos.y, newPos.z);

            //actual swing
            Vec3d pos = entity.getBoundingBox().getCenter();
            Vec3d toAnchor = owner.getPos().subtract(pos);
            double dist = toAnchor.length();
            double pullStrength = Math.clamp((dist - range), 0.0D, 7.D);

            Vec3d desiredVel = toAnchor.normalize().multiply(0.1 * pullStrength);
            Vec3d currentVel = entity.getVelocity();
            Vec3d newVel = currentVel.add(desiredVel.x, desiredVel.y, desiredVel.z).multiply(0.95);
            if (newVel.y >= 0) {
                entity.fallDistance = 0;
            }
            entity.setVelocity(newVel);
        }
    }

    @Override
    protected void age() {
        this.setAge(this.getAge() + 1);
        if (this.getAge() >= 6000) {
            this.discard();
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        //only hit a block if it didn't already hit an entity
        Optional<Entity> entity = this.getHitEntity();
        if (entity.isEmpty() || !entity.get().isAlive()) {
            super.onBlockHit(blockHitResult);
            setLanded(true);
            Entity owner = this.getOwner();
            if (owner != null) {
                float distance = (float) this.getPos().squaredDistanceTo(owner.getPos());
                setRange(Math.min(distance * 0.75F, 5.0F));
            }
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        //make it stick to the hit entity and pull the entity to the owner
        if (this.getHitEntity().isEmpty()) {
            //set hit entity info
            Entity hitEntity = entityHitResult.getEntity();
            Vec3d hitEntityPos = hitEntity.getPos();

            Vec3d pos = entityHitResult.getPos();

            Vec3d relativePos = pos.subtract(hitEntityPos);
            this.setRelativePos(relativePos);

            this.setHitEntity(Optional.of(hitEntity.getUuid()));


            //set range
            Entity owner = this.getOwner();
            if (owner != null) {
                float distance = (float) owner.getPos().squaredDistanceTo(hitEntity.getPos());
                setRange(Math.min(distance * 0.75F, 5.0F));
            }
        }
    }

    @Override
    public Vec3d getPos() {
        //if it hit the entity stick to that entity by using its pos
        return super.getPos();
    }

    @Override
    public Vec3d getVelocity() {
        Optional<Entity> entity = this.getHitEntity();
        if (entity.isPresent()) {
            return entity.get().getVelocity();
        }
        return super.getVelocity();
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return ModItems.WEB_FLUID.getDefaultStack();
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return false;
    }

    @Override
    public Box getVisibilityBoundingBox() {
        return this.getBoundingBox().expand(100);
    }

    @NotNull
    public boolean landed() {
        return this.getDataTracker().get(landed);
    }

    public void setLanded(boolean b) {
        this.getDataTracker().set(landed, b);
    }

    @NotNull
    public float getRange() {
        return this.getDataTracker().get(range);
    }

    public void setRange(float f) {
        this.getDataTracker().set(range, f);
    }

    @NotNull
    public int getAge() {
        return this.getDataTracker().get(age);
    }

    public void setAge(int i) {
        this.getDataTracker().set(age, i);
    }

    public Optional<UUID> getHitEntityUuid() {
        return this.getDataTracker().get(hitEntity);
    }

    public Optional<Entity> getHitEntity() {
        Optional<UUID> uuid = this.getDataTracker().get(hitEntity);
        if (!uuid.isPresent()) {
            return Optional.empty();
        }
        Box box = new Box(this.getBlockPos()).expand(25);
        return this.getWorld().getEntitiesByClass(Entity.class, box, Predicates.alwaysTrue()).stream().filter(entity -> uuid.get().equals(entity.getUuid())).findFirst();
    }

    public void setHitEntity(Optional<UUID> uuid) {
        this.getDataTracker().set(hitEntity, uuid);
    }

    public Vec3d getRelativePos() {
        return this.getDataTracker().get(relativePos);
    }

    public void setRelativePos(Vec3d vec) {
        this.getDataTracker().set(relativePos, vec);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(landed, false);
        builder.add(range, 3.0F);

        builder.add(hitEntity, Optional.empty());
        builder.add(relativePos, new Vec3d(0, 0, 0));

        builder.add(age, 0);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putBoolean("landed", this.landed());

        nbt.putFloat("range", this.getRange());
        nbt.putInt("age", this.getAge());

        if (this.getHitEntity().isPresent()) {
            nbt.putUuid("hit_entity", this.getHitEntityUuid().get());
        }


        Vec3d vec = this.getRelativePos();
        NbtCompound vecTag = new NbtCompound();
        vecTag.putDouble("x", vec.x);
        vecTag.putDouble("y", vec.y);
        vecTag.putDouble("z", vec.z);
        nbt.put("relative_pos", vecTag);

        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("landed")) {
            this.setLanded(nbt.getBoolean("landed"));
        }

        if (nbt.contains("range")) {
            this.setRange(nbt.getFloat("range"));
        }
        if (nbt.contains("age")) {
            this.setAge(nbt.getInt("age"));
        }

        if (nbt.contains("hit_entity")) {
            this.setHitEntity(Optional.ofNullable(nbt.getUuid("hit_entity")));
        } else {
            this.setHitEntity(Optional.empty());
        }

        if (nbt.contains("relative_pos")) {
            NbtCompound vecTag = nbt.getCompound("relative_pos");
            Vec3d vec = new Vec3d(
                    vecTag.getDouble("x"),
                    vecTag.getDouble("y"),
                    vecTag.getDouble("z")
            );
            this.setRelativePos(vec);
        }
    }
}