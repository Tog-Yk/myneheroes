package net.togyk.myneheroes.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.togyk.myneheroes.Item.custom.StationaryItem;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class StationaryItemEntity extends LivingEntity implements Ownable {
    private ItemStack item = ItemStack.EMPTY;
    @Nullable
    private UUID ownerUuid;
    @Nullable
    private Entity owner;

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
        if (!this.getWorld().isClient && player.getStackInHand(hand).isEmpty()) {
            if (this.getItem().getItem() instanceof StationaryItem item) {
                if (!item.hasToBeOwnerToPickup() || isOwner(player) && item.tryPickup(player, hand)) {
                    player.setStackInHand(hand, this.getItem().copyAndEmpty());
                }
            }
        }
        return super.interact(player, hand);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.put("item", item.encodeAllowEmpty(this.getRegistryManager()));

        if (this.ownerUuid != null) {
            nbt.putUuid("owner", this.ownerUuid);
        } else if (this.owner != null) {
            nbt.putUuid("owner", this.owner.getUuid());
        }
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("item")) {
            this.setItem(ItemStack.fromNbtOrEmpty(this.getRegistryManager(), nbt.getCompound("item")));
        }

        if (nbt.containsUuid("owner")) {
            this.ownerUuid = nbt.getUuid("owner");
            this.owner = null;
        }
    }

    @Override
    public @Nullable Entity getOwner() {
        if (this.owner != null && !this.owner.isRemoved()) {
            return this.owner;
        } else {
            if (this.ownerUuid != null) {
                World world = this.getWorld();
                if (world instanceof ServerWorld serverWorld) {
                    this.owner = serverWorld.getEntity(this.ownerUuid);
                    return this.owner;
                }
            }

            return null;
        }
    }

    public boolean isOwner(Entity entity) {
        return (owner != null && ownerUuid != null) || owner == entity || ownerUuid == entity.getUuid();
    }

    @Nullable
    public void setOwner(@Nullable Entity entity) {
        this.owner = entity;
    }

    @Nullable
    public void setOwnerUuid(UUID entity) {
        this.ownerUuid = entity;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return createLivingAttributes()
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, (double)0.0F)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25);
    }

    @Override
    public int getDespawnCounter() {
        return super.getDespawnCounter();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }
}
