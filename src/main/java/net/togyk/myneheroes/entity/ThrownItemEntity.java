package net.togyk.myneheroes.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ProjectileDeflection;
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
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.togyk.myneheroes.Item.ModItems;
import net.togyk.myneheroes.Item.custom.ThrowableItem;
import net.togyk.myneheroes.MyneHeroes;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class ThrownItemEntity extends PersistentProjectileEntity {
    private static final TrackedData<ItemStack> STACK = DataTracker.registerData(ThrownItemEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    private static final TrackedData<Integer> LOYALTY = DataTracker.registerData(ThrownItemEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> BOUNCES = DataTracker.registerData(ThrownItemEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> IS_RETURNING = DataTracker.registerData(ThrownItemEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> LANDED = DataTracker.registerData(ThrownItemEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Vector3f> GROUNDED_OFFSET = DataTracker.registerData(ThrownItemEntity.class, TrackedDataHandlerRegistry.VECTOR3F);

    private static final TrackedData<Float> WIDTH = DataTracker.registerData(ThrownItemEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> HEIGHT = DataTracker.registerData(ThrownItemEntity.class, TrackedDataHandlerRegistry.FLOAT);

    public int returnTimer;

    public ThrownItemEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }
    public ThrownItemEntity(EntityType<? extends ThrownItemEntity> entityType, World world, LivingEntity owner, ItemStack stack) {
        super(entityType, owner, world, stack, null);
        this.setDisplayStack(stack);
        setLoyalty(this.getLoyalty(stack));
    }
    public ThrownItemEntity(World world, LivingEntity owner, ItemStack stack) {
        this(ModEntities.THROWN_ITEM, world, owner, stack);
    }

    public ThrownItemEntity(World world, LivingEntity owner, ItemStack stack, float size) {
        this(world, owner, stack, size, size);
    }

    public ThrownItemEntity(World world, LivingEntity owner, ItemStack stack, float width, float height) {
        this(ModEntities.THROWN_ITEM, world, owner, stack, width, height);
    }

    public ThrownItemEntity(EntityType<? extends ThrownItemEntity> entityType, World world, LivingEntity owner, ItemStack stack, float width, float height) {
        this(entityType, world, owner, stack);
        this.getDataTracker().set(WIDTH, width);
        this.getDataTracker().set(HEIGHT, height);
        setBoundingBox(new Box(-width / 2, 0, -width / 2, width / 2, height, width / 2));
    }

    @Override
    public void tick() {

        Entity entity = this.getOwner();
        int loyalty = getLoyalty();
        if ((this.isReturning() || this.isNoClip()) && entity != null) {
            if (!this.isOwnerAlive()) {
                if (!this.getWorld().isClient && this.pickupType == PickupPermission.ALLOWED) {
                    this.dropStack(this.asItemStack(), 0.1F);
                }

                this.discard();
            } else {
                this.setNoClip(true);
                Vec3d vec3d = entity.getEyePos().subtract(this.getPos());
                this.setPos(this.getX(), this.getY() + vec3d.y * 0.015 * (double) Math.max(loyalty, 1), this.getZ());
                if (this.getWorld().isClient) {
                    this.lastRenderY = this.getY();
                }

                double speedMultiplier = 0.05 * (double) Math.max(loyalty, 1);
                this.setVelocity(this.getVelocity().multiply(0.95).add(vec3d.normalize().multiply(speedMultiplier)));
                if (this.returnTimer == 0) {
                    this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.returnTimer;
            }
        }

        super.tick();
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

    public int getLoyalty() {
        return this.getDataTracker().get(LOYALTY);
    }

    public void setLoyalty(int loyalty) {
        this.getDataTracker().set(LOYALTY, loyalty);
    }
    public int getBounces() {
        return this.getDataTracker().get(BOUNCES);
    }

    public void setBounces(int bounces) {
        this.getDataTracker().set(BOUNCES, bounces);
    }

    public boolean isReturning() {
        return this.getDataTracker().get(IS_RETURNING);
    }

    public void setReturning(boolean isReturning) {
        this.getDataTracker().set(IS_RETURNING, isReturning);
    }

    public boolean landed() {
        return this.getDataTracker().get(LANDED);
    }

    public void setLanded(boolean isReturning) {
        this.getDataTracker().set(LANDED, isReturning);
    }

    public Vector3f getGroundedOffset() {
        return this.getDataTracker().get(GROUNDED_OFFSET);
    }

    public void setGroundedOffset(Vector3f groundedOffset) {
        this.getDataTracker().set(GROUNDED_OFFSET, groundedOffset);
    }

    public float getItemWidth() {
        return this.getDataTracker().get(WIDTH);
    }

    public float getItemHeight() {
        return this.getDataTracker().get(HEIGHT);
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
        builder.add(LOYALTY, 0);
        builder.add(BOUNCES, 5);
        builder.add(IS_RETURNING, false);
        builder.add(LANDED, false);
        builder.add(GROUNDED_OFFSET, new Vector3f());

        builder.add(WIDTH, this.getWidth());
        builder.add(HEIGHT, this.getHeight());
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        NbtCompound modNbt = new NbtCompound();
        if (!this.getDisplayStack().isEmpty()) {
            modNbt.put("item", this.getDisplayStack().encode(this.getRegistryManager()));
        }

        modNbt.putInt("loyalty", this.getLoyalty());
        modNbt.putInt("bounces", this.getBounces());
        modNbt.putBoolean("is_returning", this.isReturning());
        modNbt.putBoolean("landed", this.landed());

        Vector3f vector3f = this.getGroundedOffset();
        NbtCompound vec = new NbtCompound();
        vec.putFloat("x", vector3f.x);
        vec.putFloat("y", vector3f.y);
        vec.putFloat("z", vector3f.z);
        modNbt.put("groundedOffset", vec);

        //dimensions
        DataTracker dataTracker = this.getDataTracker();
        modNbt.putFloat("dimensions_width", dataTracker.get(WIDTH));
        modNbt.putFloat("dimensions_height", dataTracker.get(HEIGHT));

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

            if (modNbt.contains("loyalty")) {
                this.setLoyalty(modNbt.getInt("loyalty"));
            }

            if (modNbt.contains("bounces")) {
                this.setBounces(modNbt.getInt("bounces"));
            }

            if (modNbt.contains("is_returning")) {
                this.setReturning(modNbt.getBoolean("is_returning"));
            }

            if (modNbt.contains("landed")) {
                this.setLanded(modNbt.getBoolean("landed"));
            }

            if (modNbt.contains("groundedOffset")) {
                NbtCompound vec = modNbt.getCompound("groundedOffset");
                Vector3f vector3f = new Vector3f(vec.getFloat("x"), vec.getFloat("y"), vec.getFloat("z"));
                this.setGroundedOffset(vector3f);
            }

            DataTracker dataTracker = this.getDataTracker();

            if (nbt.contains("dimensions_width")) {
                dataTracker.set(WIDTH, nbt.getFloat("dimensions_width"));
            }
            if (nbt.contains("dimensions_height")) {
                dataTracker.set(HEIGHT, nbt.getFloat("dimensions_height"));
            }
            float width = this.getItemWidth();
            float height = this.getItemHeight();
            setBoundingBox(new Box(-width / 2, 0, -width / 2, width / 2, height, width / 2));
        }

        super.readCustomDataFromNbt(nbt);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (!this.getWorld().isClient) {
            //bounce
            int bounces = this.getBounces();
            int loyalty = this.getLoyalty();
            if (bounces > 0) {
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

                // Set the new velocity on the projectile
                this.setVelocity(newVelocity);

                //consume a bounce
                this.setBounces(bounces - 1);
            } else if (loyalty > 0) {
                this.setReturning(true);
            } else {
                super.onBlockHit(blockHitResult);
                setLanded(true);

                //todo fix snapping to block side
                if (this.getDisplayStack().getItem() instanceof ThrowableItem throwableItem) {
                    this.setGroundedOffset(throwableItem.getGroundedOffset(this, blockHitResult.getSide()));
                }
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


    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return this.isReturning() ? null : super.getEntityCollision(currentPosition, nextPosition);
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
}