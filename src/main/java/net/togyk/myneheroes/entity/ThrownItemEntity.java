package net.togyk.myneheroes.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.togyk.myneheroes.Item.ModItems;
import net.togyk.myneheroes.MyneHeroes;

public class ThrownItemEntity extends PersistentProjectileEntity {
    private static final TrackedData<ItemStack> STACK = DataTracker.registerData(ThrownItemEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    public ThrownItemEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }
    public ThrownItemEntity(EntityType<? extends ThrownItemEntity> entityType, World world, LivingEntity owner, ItemStack stack) {
        super(entityType, owner, world, stack, null);
        this.setDisplayStack(stack);
    }
    public ThrownItemEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntities.THROWN_ITEM, owner, world, stack, null);
        this.setDisplayStack(stack);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return ModItems.VIBRANIUM_SHIELD.getDefaultStack();
    }

    public ItemStack getDisplayStack() {
        return this.getDataTracker().get(STACK);
    }

    /**
     * Sets the item stack contained in this item entity to {@code stack}.
     */
    public void setDisplayStack(ItemStack stack) {
        this.getDataTracker().set(STACK, stack);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        if (STACK.equals(data)) {
            this.getDisplayStack().setHolder(this);
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(STACK, ItemStack.EMPTY);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        NbtCompound modNbt = new NbtCompound();
        if (!this.getDisplayStack().isEmpty()) {
            modNbt.put("item", this.getDisplayStack().encode(this.getRegistryManager()));
        }

        nbt.put(MyneHeroes.MOD_ID, modNbt);

        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains(MyneHeroes.MOD_ID)) {
            NbtCompound modNbt = nbt.getCompound(MyneHeroes.MOD_ID);
            if (nbt.contains("item", NbtElement.COMPOUND_TYPE)) {
                NbtCompound nbtCompound = modNbt.getCompound("item");
                this.setDisplayStack(ItemStack.fromNbt(this.getRegistryManager(), nbtCompound).orElse(ItemStack.EMPTY));
            } else {
                this.setDisplayStack(ItemStack.EMPTY);
            }
        }

        super.readCustomDataFromNbt(nbt);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        // Only bounce if we hit a block
        if (!this.getWorld().isClient) {
            // Get current velocity vector
            Vec3d velocity = this.getVelocity();

            // Get the face of the block that was hit
            Direction hitSide = blockHitResult.getSide();
            // Create a unit normal vector from the block face
            Vec3d normal = new Vec3d(hitSide.getOffsetX(), hitSide.getOffsetY(), hitSide.getOffsetZ());

            // Compute dot product between velocity and the normal
            double dot = velocity.dotProduct(normal);

            // Reflect the velocity: newVelocity = V - 2*(V·N)*N
            Vec3d reflected = velocity.subtract(normal.multiply(2 * dot));

            // Apply 90% speed retention (i.e. lose 10% of speed)
            Vec3d newVelocity = reflected.multiply(0.75);

            //check if the speed is fast enough to still bounce
            if (newVelocity.lengthSquared() <= 0.15) {
                super.onBlockHit(blockHitResult);
            } else {
                // Set the new velocity on the projectile
                this.setVelocity(newVelocity);
            }
        }
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if (!this.getWorld().isClient && this.shake <= 0 && (this.getOwner() == null || player == this.getOwner()) && this.age > 4) {
            if (this.tryPickup(player)) {
                player.sendPickup(this, 1);
                this.discard();
            }
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        //pick up if possible
        if (entity.isPlayer() && entity == getOwner()) {
            this.onPlayerCollision((PlayerEntity) entity);
        } else {

            //hit method modified to bounce
            float f = (float) this.getVelocity().length();
            double d = this.getDamage();
            Entity entity2 = this.getOwner();
            DamageSource damageSource = this.getDamageSources().arrow(this, (entity2 != null ? entity2 : this));
            if (this.getWeaponStack() != null && this.getWorld() instanceof ServerWorld serverWorld) {
                d = EnchantmentHelper.getDamage(serverWorld, this.getWeaponStack(), entity, damageSource, (float) d);
            }

            int i = MathHelper.ceil(MathHelper.clamp((double) f * d, 0.0, 2.147483647E9));

            if (this.isCritical()) {
                long l = (long) this.random.nextInt(i / 2 + 2);
                i = (int) Math.min(l + (long) i, 2147483647L);
            }

            if (entity2 instanceof LivingEntity livingEntity) {
                livingEntity.onAttacking(entity);
            }

            boolean bl = entity.getType() == EntityType.ENDERMAN;
            int j = entity.getFireTicks();
            if (this.isOnFire() && !bl) {
                entity.setOnFireFor(5.0F);
            }

            if (entity.damage(damageSource, (float) i)) {
                if (bl) {
                    return;
                }

                if (entity instanceof LivingEntity livingEntity2) {
                    if (!this.getWorld().isClient && this.getPierceLevel() <= 0) {
                        livingEntity2.setStuckArrowCount(livingEntity2.getStuckArrowCount() + 1);
                    }

                    this.knockback(livingEntity2, damageSource);
                    if (this.getWorld() instanceof ServerWorld serverWorld2) {
                        EnchantmentHelper.onTargetDamaged(serverWorld2, livingEntity2, damageSource, this.getWeaponStack());
                    }

                    this.onHit(livingEntity2);
                    if (livingEntity2 != entity2 && livingEntity2 instanceof PlayerEntity && entity2 instanceof ServerPlayerEntity && !this.isSilent()) {
                        ((ServerPlayerEntity) entity2)
                                .networkHandler
                                .sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, GameStateChangeS2CPacket.DEMO_OPEN_SCREEN));
                    }
                }

                this.playSound(this.getSound(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            } else {
                entity.setFireTicks(j);
                this.deflect(ProjectileDeflection.SIMPLE, entity, this.getOwner(), false);
                this.setVelocity(this.getVelocity().multiply(0.2));
                if (!this.getWorld().isClient && this.getVelocity().lengthSquared() < 1.0E-7) {
                    if (this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED) {
                        this.dropStack(this.asItemStack(), 0.1F);
                        this.discard();
                    }
                }
            }

            if (!this.getWorld().isClient) {
                // Get the current velocity of the projectile.
                Vec3d velocity = this.getVelocity();

                // Get the entity that was hit.
                Entity hitEntity = entityHitResult.getEntity();

                // Compute a normal from the center of the hit entity to the projectile.
                // Using the entity’s bounding box center gives a better approximation.
                Vec3d entityCenter = hitEntity.getBoundingBox().getCenter();
                Vec3d normal = this.getPos().subtract(entityCenter);

                // If, for any reason, the normal is zero, use a fallback (e.g. upward).
                if (normal.lengthSquared() == 0) {
                    normal = new Vec3d(0, 1, 0);
                } else {
                    normal = normal.normalize();
                }

                // Reflect the velocity using the formula: R = V - 2*(V·N)*N
                double dot = velocity.dotProduct(normal);
                Vec3d reflected = velocity.subtract(normal.multiply(2 * dot));

                // Multiply by 0.9 so the projectile retains 50% of its speed.
                Vec3d newVelocity = reflected.multiply(0.5);

                // Set the new velocity.
                this.setVelocity(newVelocity);

                // Optionally, if you’re implementing bounce limits, update your bounce counter here.
            }
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void age() {
        this.age++;
    }
}