package net.togyk.myneheroes.entity;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
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
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.togyk.myneheroes.damage.ModDamageTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class LaserEntity extends PersistentProjectileEntity {
    private static final TrackedData<Integer> color = DataTracker.registerData(LaserEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> innerColor = DataTracker.registerData(LaserEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public LaserEntity(EntityType<LaserEntity> laserProjectileEntityEntityType, World world) {
        super(laserProjectileEntityEntityType, world);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        float f = (float)this.getVelocity().length();
        double d = this.getDamage();
        Entity entity2 = this.getOwner();
        DamageSource damageSource = ModDamageTypes.of(this.getWorld(), ModDamageTypes.LASER_TYPE_KEY, entity2 instanceof LivingEntity owner ? owner : null);
        if (this.getWeaponStack() != null) {
            World bl = this.getWorld();
            if (bl instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld)bl;
                d = (double) EnchantmentHelper.getDamage(serverWorld, this.getWeaponStack(), entity, damageSource, (float)d);
            }
        }

        int i = MathHelper.ceil(MathHelper.clamp((double)f * d, (double)0.0F, (double)Integer.MAX_VALUE));

        if (this.isCritical()) {
            long l = (long)this.random.nextInt(i / 2 + 2);
            i = (int)Math.min(l + (long)i, 2147483647L);
        }

        if (entity2 instanceof LivingEntity livingEntity) {
            livingEntity.onAttacking(entity);
        }

        boolean bl = entity.getType() == EntityType.ENDERMAN;
        int j = entity.getFireTicks();
        if (this.isOnFire() && !bl) {
            entity.setOnFireFor(5.0F);
        }

        if (entity.damage(damageSource, (float)i)) {
            if (bl) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity2 = (LivingEntity)entity;
                if (!this.getWorld().isClient && this.getPierceLevel() <= 0) {
                    livingEntity2.setStuckArrowCount(livingEntity2.getStuckArrowCount() + 1);
                }

                this.knockback(livingEntity2, damageSource);
                World var13 = this.getWorld();
                if (var13 instanceof ServerWorld) {
                    ServerWorld serverWorld2 = (ServerWorld)var13;
                    EnchantmentHelper.onTargetDamaged(serverWorld2, livingEntity2, damageSource, this.getWeaponStack());
                }

                this.onHit(livingEntity2);
                if (livingEntity2 != entity2 && livingEntity2 instanceof PlayerEntity && entity2 instanceof ServerPlayerEntity && !this.isSilent()) {
                    ((ServerPlayerEntity)entity2).networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, 0.0F));
                }
            }

            this.playSound(this.getSound(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            if (this.getPierceLevel() <= 0) {
                this.discard();
            }
        } else {
            entity.setFireTicks(j);
            this.deflect(ProjectileDeflection.SIMPLE, entity, this.getOwner(), false);
            this.setVelocity(this.getVelocity().multiply(0.2));
            if (!this.getWorld().isClient && this.getVelocity().lengthSquared() < 1.0E-7) {
                if (this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED) {
                    this.dropStack(this.asItemStack(), 0.1F);
                }

                this.discard();
            }
        }
        this.discard();
    }

    @Override
    protected double getGravity() {
        return 0.02;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (!getWorld().isClient) {
            Block hitBlock = getWorld().getBlockState(blockHitResult.getBlockPos()).getBlock();
            if (hitBlock == Blocks.ICE) {
                getWorld().setBlockState(blockHitResult.getBlockPos(), Blocks.WATER.getDefaultState());
            }
        }
        this.discard();
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return false;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(Items.ARROW);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(color, 0x40FF0000);
        builder.add(innerColor, 0xF0FFF0F0);
    }

    @NotNull
    public int getColor() {
        return this.getDataTracker().get(color);
    }

    public void setColor(int argb) {
        this.getDataTracker().set(color, argb);
    }

    @NotNull
    public int getInnerColor() {
        return this.getDataTracker().get(innerColor);
    }

    public void setInnerColor(int argb) {
        this.getDataTracker().set(innerColor, argb);
    }
}
