package net.togyk.myneheroes.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class LaserEntity extends PersistentProjectileEntity {
    private static final TrackedData<Integer> color = DataTracker.registerData(LaserEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> innerColor = DataTracker.registerData(LaserEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public LaserEntity(EntityType<LaserEntity> laserProjectileEntityEntityType, World world) {
        super(laserProjectileEntityEntityType, world);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
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
        //will give a crash at some point. Don't care enough to fix right now
        return new ItemStack(Items.ARROW);
        //>>>Caused by: java.lang.IllegalStateException: Cannot encode empty ItemStack
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(color, 0x55FF0000);
        builder.add(innerColor, 0xFFFFF0F0);
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
