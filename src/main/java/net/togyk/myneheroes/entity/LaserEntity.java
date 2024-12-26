package net.togyk.myneheroes.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import net.togyk.myneheroes.MyneHeroes;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class LaserEntity extends PersistentProjectileEntity {
    private Color color;
    private Color innerColor;

    public LaserEntity(EntityType<LaserEntity> laserProjectileEntityEntityType, World world) {
        super(laserProjectileEntityEntityType, world);
        this.color = Color.RED;
        this.innerColor = new Color(255, 200, 200);
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
    protected boolean tryPickup(PlayerEntity player) {
        return false;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        //will give a crash at some point. Don't care enough to fix right now
        return new ItemStack(Items.ARROW);
        //>>>Caused by: java.lang.IllegalStateException: Cannot encode empty ItemStack
    }

    @NotNull
    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @NotNull
    public Color getInnerColor() {
        return this.innerColor;
    }

    public void setInnerColor(Color color) {
        this.innerColor = color;
    }
}
