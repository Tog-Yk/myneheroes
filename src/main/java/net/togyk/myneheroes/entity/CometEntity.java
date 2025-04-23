package net.togyk.myneheroes.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.togyk.myneheroes.block.ModBlocks;

import java.util.Random;

public class CometEntity extends PersistentProjectileEntity {
    private static final TrackedData<Float> Size = DataTracker.registerData(CometEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Integer> VariantId  = DataTracker.registerData(CometEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public CometEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world, CometVariant variant) {
        super(entityType, world);
        this.setVariant(variant);
        this.setSize((new Random()).nextFloat(5, 12));
    }

    public CometEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }


    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putFloat("size", this.getSize());
        nbt.putInt("variant", this.getVariant().getId());
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("size")) {
            this.setSize(nbt.getFloat("size"));
        }
        if (nbt.contains("variant")) {
            this.setVariant(nbt.getInt("variant"));
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        World world = this.getWorld();
        if (!world.isClient()) {
            BlockPos pos = this.getBlockPos();
            world.createExplosion(this, pos.getX(), pos.getY(), pos.getZ(), getImpactPower(), true, World.ExplosionSourceType.BLOCK);
            this.createCrater(world, this.getBlockPos(), new Random());
            this.createComet(world, this.getHighestBlockBelowY(world, pos.getX(), pos.getZ(), pos.getY()).up((int) (this.getSize() / 2)), new Random());
        }
        this.discard();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        World world = this.getWorld();
        if (!world.isClient()) {
            BlockPos pos = this.getBlockPos();
            world.createExplosion(this, pos.getX(), pos.getY(), pos.getZ(), getImpactPower(), true, World.ExplosionSourceType.BLOCK);
            this.createCrater(world, this.getBlockPos(), new Random());
            this.createComet(world, this.getHighestBlockBelowY(world, pos.getX(), pos.getZ(), pos.getY()).up((int) (this.getSize() / 2)), new Random());
        }
        this.discard();
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return ModBlocks.KRYPTONITE_CLUSTER.asItem().getDefaultStack();
    }

    @Override
    public boolean shouldRender(double distance) {
        return true;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(Size, 7.50F);
        builder.add(VariantId, 0);
        //CometVariant.getRandomVariant(new Random()).getId()
    }

    public CometVariant getVariant() {
        return CometVariant.byId(this.getDataTracker().get(VariantId));
    }

    public void setVariant(CometVariant variant) {
        this.getDataTracker().set(VariantId, variant.getId());
    }

    public void setVariant(int i) {
        this.getDataTracker().set(VariantId, i);
    }

    public float getSize() {
        return this.getDataTracker().get(Size);
    }

    public void setSize(float size) {
        this.getDataTracker().set(Size, size);
    }

    @Override
    protected Box calculateBoundingBox() {
        return super.calculateBoundingBox().expand(this.getSize() / 2.5);
    }

    public double getCraterRadius() {
        return this.getSize() * 2;
    }


    public float getImpactPower() {
        return (float) (this.getSize() * 2F);
    }


    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return false;
    }

    @Override
    protected double getGravity() {
        return 0.13;
    }

    private void createCrater(World world, BlockPos origin, Random random) {
        // Choose a random crater radius between 4 and 8
        double radius = this.getCraterRadius();

        // Loop over a square bounding the circle
        for (double x = -radius; x <= radius; x++) {
            for (double z = -radius; z <= radius; z++) {
                double distSq = x * x + z * z;
                if (distSq > radius*radius) continue; // outside circle

                // Compute crater depth by circle equation (hemisphere)
                double depth = Math.sqrt(radius*radius - distSq);

                // For each layer down to form the bowl
                for (double y = -depth; y <= depth; y++) {
                    if (random.nextFloat() > 0.7F) {
                        BlockPos pos = origin.add((int) x, (int) y, (int) z);
                        BlockState state = world.getBlockState(pos);

                        // Only replace stone‑replaceable blocks
                        if (state.isIn(BlockTags.SAND)) {
                            world.setBlockState(pos, Blocks.GLASS.getDefaultState(), 2);
                        } else if (state.isIn(BlockTags.BASE_STONE_OVERWORLD) || state.isIn(BlockTags.LOGS) || state.isIn(BlockTags.DIRT)) {
                            world.setBlockState(pos, this.getRandomBlockFromTag(random, this.getVariant().getCrustBlockTag()));
                        }
                    }
                }
            }
        }
    }

    private BlockPos getHighestBlockBelowY(World world, int x, int z, int maxY) {
        BlockPos.Mutable pos = new BlockPos.Mutable(x, maxY, z);

        // Scan downward until we find a solid block
        while (pos.getY() > world.getBottomY()) {
            BlockState state = world.getBlockState(pos);
            if (!state.isAir()) {
                return pos.toImmutable(); // Found the block
            }
            pos.move(Direction.DOWN);
        }

        // If no solid block found, return bottom of the world
        return new BlockPos(x, world.getBottomY(), z);
    }

    private void createComet(World world, BlockPos origin, Random random) {
        int radius = (int) (this.getSize() / 2);

        // Loop over a square bounding the circle
        for (double x = -radius; x <= radius; x++) {
            for (double z = -radius; z <= radius; z++) {
                double distSq = x*x + z*z;
                if (distSq > radius*radius) continue; // outside circle

                // Compute crater depth by circle equation (hemisphere)
                double depth = Math.sqrt(radius*radius - distSq);

                // For each layer down to form the bowl
                for (double y = -depth; y <= depth; y++) {
                    BlockPos pos = origin.add((int) x, (int) y, (int) z);
                    double distSq2 = x*x + z*z + y*y;
                    if (distSq2 > (radius * 0.70)*(radius * 0.70)) {
                        //on the crust
                        world.setBlockState(pos, this.getRandomBlockFromTag(random, this.getVariant().getCrustBlockTag()));
                    } else {
                        //in the core
                        world.setBlockState(pos, this.getRandomBlockFromTag(random, this.getVariant().getCoreBlockTag()));
                    }
                }
            }
        }
    }

    public BlockState getRandomBlockFromTag(Random random, TagKey<Block> tag) {
        // Get all the blocks in the tag
        var tagEntryList = Registries.BLOCK.getEntryList(tag);

        if (tagEntryList.isPresent()) {
            var entries = tagEntryList.get();
            int size = entries.size();
            if (size > 0) {
                var entry = entries.get(random.nextInt(size));
                return entry.value().getDefaultState();
            }
        }

        return Blocks.DEEPSLATE.getDefaultState();
    }
}
