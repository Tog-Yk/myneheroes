package net.togyk.myneheroes.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.togyk.myneheroes.entity.MeteorVariant;
import net.togyk.myneheroes.util.ModTags;


public class MeteorFeature extends Feature<DefaultFeatureConfig> {
    public MeteorFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos pos = context.getOrigin();
        Random random = context.getRandom();

        MeteorVariant variant = MeteorVariant.getRandomVariant(random);
        int radius = random.nextBetween(3, 6);

        //seems to freeze the server
        createCrater(world, pos, radius, random);
        createCraterDecoration(world, pos, (int) (radius * 2.5), random, variant);
        //unknown status
        createMeteor(world, pos.down(), radius, random, variant);

        return true;
    }




    private void createCrater(StructureWorldAccess world, BlockPos origin, int radius, Random random) {
        double rx = radius * 1.5 * 1.6;   // X stretch
        double rz = radius * 1.5 * 1.6;   // Z stretch
        double ry = radius * 1.5;    // Y smaller

        boolean isWaterMeteor = world.getBlockState(origin).isOf(Blocks.WATER) && world.getBiome(origin).isIn(BiomeTags.IS_OCEAN);

        for (int dx = (int)-rx; dx <= (int)rx; dx++) {
            for (int dz = (int)-rz; dz <= (int)rz; dz++) {
                for (int dy = (int)-ry; dy <= ry; dy++) { // only carve downward
                    // normalized distances
                    double nx = dx / rx;
                    double ny = dy / ry;
                    double nz = dz / rz;

                    double distanceSq = nx*nx + ny*ny + nz*nz;

                    if (distanceSq <= 1.0) {
                        //add some randomness
                        if (distanceSq >= 0.5 && random.nextFloat() <= distanceSq) continue;

                        BlockPos pos = origin.add(dx, dy, dz);
                        BlockState state = world.getBlockState(pos);
                        if (!state.isIn(ModTags.Blocks.NOT_METEOR_REPLACEABLE)) {
                            if (!state.isOf(Blocks.WATER)) {
                                if (isWaterMeteor && world.getSeaLevel() > pos.getY()) {
                                    world.setBlockState(pos, Blocks.WATER.getDefaultState(), 2);
                                } else {
                                    world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void createCraterDecoration(StructureWorldAccess world, BlockPos origin, int radius, Random random, MeteorVariant variant) {

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

                        // Only replace replaceable blocks
                        if (!state.isIn(ModTags.Blocks.NOT_METEOR_REPLACEABLE)) {
                            if (state.isIn(BlockTags.SAND) && random.nextFloat() > 0.7F) {
                                world.setBlockState(pos, Blocks.GLASS.getDefaultState(), 2);
                            } else {
                                world.setBlockState(pos, getRandomBlockFromTag(random, variant.getCrustBlockTag()), 2);
                            }
                        }
                    }
                }
            }
        }
    }

    private void createMeteor(StructureWorldAccess world, BlockPos origin, int radius, Random random, MeteorVariant variant) {

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
                    if (world.getBlockState(pos).isOf(Blocks.BEDROCK)) continue; //No Bedrock breaking

                    double distSq2 = x*x + z*z + y*y;
                    if (distSq2 > (radius * 0.70)*(radius * 0.70)) {
                        //on the crust
                        world.setBlockState(pos, this.getRandomBlockFromTag(random, variant.getCrustBlockTag()), 2);
                    } else {
                        //in the core
                        world.setBlockState(pos, this.getRandomBlockFromTag(random, variant.getCoreBlockTag()), 2);
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
