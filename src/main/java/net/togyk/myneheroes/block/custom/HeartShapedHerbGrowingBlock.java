package net.togyk.myneheroes.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.togyk.myneheroes.block.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class HeartShapedHerbGrowingBlock extends Block {
    private final int range;

    public HeartShapedHerbGrowingBlock(int range, Settings settings) {
        super(settings);
        this.range = range;
    }

    @Override
    protected boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        Box box = new Box(pos).expand(this.range);
        BlockPos targetPos = findRandomGrassInBox(world, box);
        if (targetPos != null) {
            BlockState targetState = world.getBlockState(targetPos);

            if (targetState.isOf(Blocks.GRASS_BLOCK) && world.getBlockState(targetPos.up()).isAir()) {
                world.setBlockState(targetPos.up(), ModBlocks.HEART_SHAPED_HERB.getDefaultState(), 3);
            }
        }
    }

    private BlockPos findRandomGrassInBox(World world, Box box) {
        List<BlockPos> posList = findGrassInBox(world, box);
        if (posList.isEmpty()) {
            return null;
        }
        java.util.Random random = new java.util.Random();

        return posList.get(random.nextInt(posList.size()));
    }

    private List<BlockPos> findGrassInBox(World world, Box box) {
        List<BlockPos> posList = new ArrayList<>();
        for (double x = box.minX; x < box.maxX; x++) {
            for (double y = box.minY; y < box.maxY; y++) {
                for (double z = box.minZ; z < box.maxZ; z++) {
                    BlockPos pos = new BlockPos((int) x, (int) y, (int) z);
                    if (world.getBlockState(pos).isOf(Blocks.GRASS_BLOCK)) {
                        posList.add(pos);
                    }
                }
            }
        }
        return posList;
    }
}
