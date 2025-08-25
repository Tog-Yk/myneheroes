package net.togyk.myneheroes.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.togyk.myneheroes.effect.ModEffects;

import java.util.List;

public class RadiationBlock extends Block {
    public static final BooleanProperty TICKING = BooleanProperty.of("ticking");

    public RadiationBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        Box area = new Box(pos).expand(5); // radiation radius
        List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, area, e -> true);

        for (LivingEntity entity : entities) {
            // Entity is exposed to radiation
            entity.addStatusEffect(new StatusEffectInstance(
                    ModEffects.RADIATION_POISON, 72, 0, true, true
            ));
        }

        // Re-schedule tick
        world.scheduleBlockTick(pos, this, 24);
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        if (!state.get(TICKING)) {
            // schedule tick
            world.scheduleBlockTick(pos, this, 24);
            world.setBlockState(pos, state.with(TICKING, true));
        }
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        if (!world.isClient) {
            world.scheduleBlockTick(pos, this, 24); // start ticking
            world.setBlockState(pos, state.with(TICKING, true));
        }
    }
}
