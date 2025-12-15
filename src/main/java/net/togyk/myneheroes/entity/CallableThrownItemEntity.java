package net.togyk.myneheroes.entity;

import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.togyk.myneheroes.entity.data.ModTrackedData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CallableThrownItemEntity extends ThrownItemEntity {
    private static final TrackedData<Boolean> DISCOVERED = DataTracker.registerData(CallableThrownItemEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<List<UUID>> TARGETS = DataTracker.registerData(CallableThrownItemEntity.class, ModTrackedData.UUID_LIST);


    public CallableThrownItemEntity(EntityType<? extends CallableThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }
    public CallableThrownItemEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntities.CALLABLE_THROWN_ITEM, world, owner, stack);
    }
    public CallableThrownItemEntity(World world, LivingEntity owner, ItemStack stack, float size) {
        this(world, owner, stack, size, size);
    }

    public CallableThrownItemEntity(World world, LivingEntity owner, ItemStack stack, float width, float height) {
        super(ModEntities.CALLABLE_THROWN_ITEM, world, owner, stack, width, height);
    }

    @Override
    public void tick() {
        if (!this.isReturning()) {
            List<LivingEntity> targets = this.getTargets(this.getWorld(), this.getBlockPos());
            if (!targets.isEmpty()) {
                LivingEntity closest = targets.getFirst();
                double closestDistSq = Double.MAX_VALUE;

                Vec3d projPos = this.getPos();

                for (LivingEntity entity : targets) {
                    if (entity == null || !entity.isAlive()) continue;

                    double distSq = entity.squaredDistanceTo(projPos);

                    if (distSq < closestDistSq) {
                        closestDistSq = distSq;
                        closest = entity;
                    }
                }

                //fly to target
                Vec3d vec3d = closest.getEyePos().subtract(this.getPos());
                this.setPos(this.getX(), this.getY() + vec3d.y * 1, this.getZ());
                if (this.getWorld().isClient) {
                    this.lastRenderY = this.getY();
                }

                double speedMultiplier = 0.35;
                this.setVelocity(this.getVelocity().multiply(0.65).add(vec3d.normalize().multiply(speedMultiplier)));
            }
        }
        super.tick();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        this.setTargetUuids(new ArrayList<>());
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        LivingEntity target = getCurrentTarget(this.getWorld(), this.getBlockPos());
        if (entityHitResult.getEntity() == target) {
            List<UUID> uuids = new ArrayList<>(this.getTargetUuids().stream().toList());
            uuids.remove(target.getUuid());
            this.setTargetUuids(uuids);

            if (this.getTargets(this.getWorld(), this.getBlockPos()).isEmpty()) {
                this.setReturning(true);
            }
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(DISCOVERED, false);
        builder.add(TARGETS, new ArrayList<>());
    }

    public boolean isDiscovered() {
        return this.getDataTracker().get(DISCOVERED);
    }

    public void setDiscovered(boolean bool) {
        this.getDataTracker().set(DISCOVERED, bool);
    }

    public void setTargetUuids(List<UUID> uuids) {
        this.getDataTracker().set(TARGETS, uuids);
    }
    public List<UUID> getTargetUuids() {
        return this.getDataTracker().get(TARGETS);
    }

    public List<LivingEntity> getTargets(World world, BlockPos pos) {
        List<UUID> uuids = getTargetUuids();
        Box box = new Box(pos).expand(512);
        return world.getEntitiesByClass(LivingEntity.class, box, Predicates.alwaysTrue()).stream()
                .filter(
                        found -> uuids.stream().anyMatch(uuid -> uuid.equals(found.getUuid())))
                .toList();
    }

    public LivingEntity getCurrentTarget(World world, BlockPos pos) {
        List<LivingEntity> targets = this.getTargets(this.getWorld(), this.getBlockPos());
        if (!targets.isEmpty()) {
            LivingEntity closest = targets.getFirst();
            double closestDistSq = Double.MAX_VALUE;

            Vec3d projPos = this.getPos();

            for (LivingEntity entity : targets) {
                if (entity == null || !entity.isAlive()) continue;

                double distSq = entity.squaredDistanceTo(projPos);

                if (distSq < closestDistSq) {
                    closestDistSq = distSq;
                    closest = entity;
                }
            }
            return closest;
        }
        return null;
    }

    public boolean callable(LivingEntity caller) {
        return !this.isReturning() && (this.getOwner() != null || this.isDiscovered());
    }

    public boolean call(LivingEntity caller) {
        if (!this.callable(caller)) return false;

        this.setReturning(true);
        return true;
    }

    @Override
    protected boolean canHit(Entity entity) {
        boolean isTargetOrOwner = entity == this.getOwner() || entity == this.getCurrentTarget(this.getWorld(), this.getBlockPos());
        boolean noTargets = this.getTargetUuids().isEmpty();
        return super.canHit(entity) && (isTargetOrOwner || noTargets);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putBoolean("discovered", isDiscovered());

        NbtList list = new NbtList();

        for (UUID uuid : this.getTargetUuids()) {
            list.add(NbtHelper.fromUuid(uuid));
        }

        nbt.put("targets", list);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("discovered")) {
            this.setDiscovered(nbt.getBoolean("discovered"));
        }

        if (nbt.contains("targets")) {
            List<UUID> result = new ArrayList<>();

            NbtList list = nbt.getList("targets", NbtElement.INT_ARRAY_TYPE);

            for (NbtElement element : list) {
                if (element instanceof NbtIntArray arr) {
                    result.add(NbtHelper.toUuid(arr));
                }
            }

            this.setTargetUuids(result);
        }
    }
}