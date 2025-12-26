package net.togyk.myneheroes.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CallableStationaryItemEntity extends StationaryItemEntity {
    private static final TrackedData<Boolean> DISCOVERED = DataTracker.registerData(CallableStationaryItemEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> RETURNING = DataTracker.registerData(CallableStationaryItemEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public CallableStationaryItemEntity(EntityType<? extends CallableStationaryItemEntity> entityType, World world) {
        super(entityType, world);
    }
    public CallableStationaryItemEntity(World world) {
        super(ModEntities.CALLABLE_STATIONARY_ITEM, world);
    }

    @Override
    public void tick() {

        Entity entity = this.getOwner();
        int loyalty = getLoyalty(this.getItem());
        if ((this.isReturning() || this.noClip) && entity != null) {
            if (!this.isOwnerAlive()) {
                if (!this.getWorld().isClient) {
                    this.dropStack(this.getItem(), 0.1F);
                }

                this.discard();
            } else {
                this.noClip = true;
                Vec3d vec3d = entity.getEyePos().subtract(this.getPos());
                this.setPos(this.getX(), this.getY() + vec3d.y * 0.015 * (double) Math.max(loyalty, 1), this.getZ());
                if (this.getWorld().isClient) {
                    this.lastRenderY = this.getY();
                }


                double speedMultiplier = 0.15 * (double) Math.max(loyalty, 1);
                this.setVelocity(this.getVelocity().multiply(0.95).add(vec3d.normalize().multiply(speedMultiplier)));
            }
        }

        super.tick();
    }

    @Override
    protected double getGravity() {
        return this.isReturning() ? 0 : super.getGravity();
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(DISCOVERED, false);
        builder.add(RETURNING, false);
    }

    public boolean isDiscovered() {
        return this.getDataTracker().get(DISCOVERED);
    }

    public void setDiscovered(boolean bool) {
        this.getDataTracker().set(DISCOVERED, bool);
    }

    public boolean isReturning() {
        return this.getDataTracker().get(RETURNING);
    }

    public void setReturning(boolean bool) {
        this.getDataTracker().set(RETURNING, bool);
    }
    
    public boolean callable(LivingEntity caller) {
        return !this.isReturning() && (this.getOwner() != null || this.isDiscovered());
    }

    public void call(LivingEntity caller) {
        if (!this.callable(caller)) return;

        this.setReturning(true);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putBoolean("discovered", isDiscovered());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("discovered")) {
            this.setDiscovered(nbt.getBoolean("discovered"));
        }
    }

    private int getLoyalty(ItemStack stack) {
        World var3 = this.getWorld();
        if (var3 instanceof ServerWorld serverWorld) {
            return MathHelper.clamp(EnchantmentHelper.getTridentReturnAcceleration(serverWorld, stack, this), 0, 127);
        } else {
            return 0;
        }
    }

    private boolean isOwnerAlive() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        super.onPlayerCollision(player);

        // Only allow the owner to pick it up — and only while returning
        if (!this.isReturning()) return;
        if (player != this.getOwner()) return;

        ItemStack stack = this.getItem();
        if (stack.isEmpty()) return;

        // Try to give the item to the player
        if (player.getInventory().insertStack(stack.copy())) {
            // Pickup sound + trigger pickup animation
            player.sendPickup(this, stack.getCount());
            this.discard();
        }
    }
}