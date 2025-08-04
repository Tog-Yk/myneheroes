package net.togyk.myneheroes.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.entity.MeteorVariant;


public class MeteorFeature extends Feature<DefaultFeatureConfig> {
    public MeteorFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        World world = context.getWorld().toServerWorld();
        BlockPos pos = context.getOrigin();
        Random random = context.getRandom();

        MeteorVariant variant = MeteorVariant.getRandomVariant(new java.util.Random());
        int radius = random.nextBetween(3, 6);

        createCrater(world, pos, (int) (radius * 2.5), random, variant);
        createMeteor(world, pos.up(radius/2), radius, random, variant);

        MyneHeroes.LOGGER.info("placed meteor at: {}", pos);

        return true;
    }


    private void createCrater(World world, BlockPos origin, int radius, Random random, MeteorVariant variant) {

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
                            world.setBlockState(pos, this.getRandomBlockFromTag(random, variant.getCrustBlockTag()));
                        }
                    }
                }
            }
        }
    }

    private void createMeteor(World world, BlockPos origin, int radius, Random random, MeteorVariant variant) {

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
                        world.setBlockState(pos, this.getRandomBlockFromTag(random, variant.getCrustBlockTag()));
                    } else {
                        //in the core
                        world.setBlockState(pos, this.getRandomBlockFromTag(random, variant.getCoreBlockTag()));
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
