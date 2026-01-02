package net.togyk.myneheroes.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.togyk.myneheroes.Item.custom.StationaryItem;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.UUID;

public class StationaryItemEntity extends LivingEntity implements Ownable {
    private ItemStack item = ItemStack.EMPTY;
    private static final TrackedData<Optional<UUID>> ownerUuid = DataTracker.registerData(StationaryItemEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    @Nullable
    private Entity owner;

    private static final TrackedData<Vector3f> followDirection = DataTracker.registerData(StationaryItemEntity.class, TrackedDataHandlerRegistry.VECTOR3F);

    public StationaryItemEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    public StationaryItemEntity(World world) {
        super(ModEntities.STATIONARY_ITEM, world);
    }

    @Override
    @Nullable
    public Iterable<ItemStack> getArmorItems() {
        return null;
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return item;
    }

    public ItemStack getItem() {
        return item;
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
        item = stack;
    }

    public void setItem(ItemStack stack) {
        item = stack;
    }

    @Override
    public Arm getMainArm() {
        return null;
    }

    @Override
    public void tick() {
        if (this.item.isEmpty()) {
            this.discard();
        }
        super.tick();
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void tickCramming() {
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        //Method Is not called properly
        if (player.getStackInHand(hand).isEmpty() && player instanceof ServerPlayerEntity serverPlayer) {
            if (this.getItem().getItem() instanceof StationaryItem item) {
                if (item.tryPickup(player, hand)) {
                    if (this.canPickup(serverPlayer, item)) {
                        player.setStackInHand(hand, this.getItem().copyAndEmpty());
                        this.onPickup(serverPlayer);
                    } else {
                        this.onCantPickup(serverPlayer);
                    }
                }
            }
        }
        return super.interact(player, hand);
    }

    public boolean canPickup(ServerPlayerEntity player, StationaryItem item) {
        return !item.hasToBeOwnerToPickup(this.getItem()) || isOwner(player);
    }

    public void onPickup(ServerPlayerEntity serverPlayer) {
    }

    public void onCantPickup(ServerPlayerEntity serverPlayer) {
    }

    public Vector3f getFollowDirection() {
        return this.getDataTracker().get(followDirection);
    }

    public void setFollowDirection(Vector3f followDirection) {
        this.getDataTracker().set(this.followDirection, followDirection);
    }

    public UUID getOwnerUuid() {
        return this.getDataTracker().get(ownerUuid).orElse(null);
    }

    public void setOwnerUuid(@Nullable UUID uuid) {
        this.getDataTracker().set(ownerUuid, Optional.ofNullable(uuid));
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.put("item", item.encodeAllowEmpty(this.getRegistryManager()));

        if (this.getOwnerUuid() != null) {
            nbt.putUuid("owner", this.getOwnerUuid());
        } else if (this.owner != null) {
            nbt.putUuid("owner", this.owner.getUuid());
        }

        Vector3f vec3d = this.getFollowDirection();
        nbt.put("follow_direction", this.toNbtList(vec3d.x, vec3d.y, vec3d.z));

        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("item")) {
            this.setItem(ItemStack.fromNbtOrEmpty(this.getRegistryManager(), nbt.getCompound("item")));
        }

        if (nbt.containsUuid("owner")) {
            this.setOwnerUuid(nbt.getUuid("owner"));
            this.owner = null;
        }
        if (nbt.contains("follow_direction")) {
            NbtList nbtList2 = nbt.getList("follow_direction", NbtElement.FLOAT_TYPE);
            this.setFollowDirection(new Vector3f(nbtList2.getFloat(0), nbtList2.getFloat(1), nbtList2.getFloat(2)));
        }
    }

    @Override
    public @Nullable Entity getOwner() {
        if (this.owner != null && !this.owner.isRemoved()) {
            return this.owner;
        } else {
            if (this.getOwnerUuid() != null) {
                World world = this.getWorld();
                if (world instanceof ServerWorld serverWorld) {
                    this.owner = serverWorld.getEntity(this.getOwnerUuid());
                    return this.owner;
                }
            }

            return null;
        }
    }

    public boolean isOwner(Entity entity) {
        return (owner != null && this.getOwnerUuid() != null) || owner == entity || this.getOwnerUuid() == entity.getUuid();
    }

    public void setOwner(@Nullable Entity entity) {
        this.owner = entity;
    }

    public void setOwnerUuid(Optional<UUID> entity) {
        this.getDataTracker().set(ownerUuid, entity);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return createLivingAttributes()
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, (double)0.0F)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ownerUuid, Optional.empty());
        builder.add(followDirection, new Vector3f());
    }
}
