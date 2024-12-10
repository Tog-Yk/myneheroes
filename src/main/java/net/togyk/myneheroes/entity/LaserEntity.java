package net.togyk.myneheroes.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class LaserEntity extends PersistentProjectileEntity {

    public LaserEntity(EntityType<LaserEntity> laserProjectileEntityEntityType, World world) {
        super(laserProjectileEntityEntityType, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            for (int i = 0; i < 5; i++) {
                this.getWorld().addParticle(ParticleTypes.END_ROD,
                        this.getX() + this.random.nextGaussian() * 0.1,
                        this.getY() + this.random.nextGaussian() * 0.1,
                        this.getZ() + this.random.nextGaussian() * 0.1,
                        0, 0, 0);
            }
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        this.discard();
        if (!getWorld().isClient) {
            Block hitBlock = getWorld().getBlockState(blockHitResult.getBlockPos()).getBlock();
            if (hitBlock == Blocks.ICE) {
                getWorld().setBlockState(blockHitResult.getBlockPos(), Blocks.WATER.getDefaultState());
            }
        }
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        //will give a crash at some point. Don't care enough to fix right now
        return ItemStack.EMPTY;
        //>>>Caused by: java.lang.IllegalStateException: Cannot encode empty ItemStack
    }
}
