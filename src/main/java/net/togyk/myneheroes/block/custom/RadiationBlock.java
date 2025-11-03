package net.togyk.myneheroes.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.togyk.myneheroes.effect.ModEffects;
import net.togyk.myneheroes.util.ModTags;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RadiationBlock extends Block {
    public static final BooleanProperty TICKING = BooleanProperty.of("ticking");

    public RadiationBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(TICKING, false));
    }

    //add properties to the state manager
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TICKING);
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!world.isClient && !state.get(TICKING)) {
            world.scheduleBlockTick(pos, this, 24); // start ticking
            world.setBlockState(pos, state.with(TICKING, true));
        }
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (usesDFS()) {
            radiateEntitiesInRangeUsingDFS(world, pos, this.getRange());
        } else {
            radiateEntitiesInRange(world, pos, this.getRange());
        }

        // Re-schedule tick
        if (state.get(TICKING)) {
            world.scheduleBlockTick(pos, this, 24);
        }
    }

    public boolean canGiveEffect(LivingEntity entity) {
        return true;
    }

    public RegistryEntry<StatusEffect> getEffect() {
        return ModEffects.RADIATION_POISON;
    }

    public void radiateEntitiesInRangeUsingDFS(World world, BlockPos origin, int range) {
        radiateEntitiesInRangeUsingDFS(world, origin, origin, range, new HashSet<>());
    }

    public void radiateEntitiesInRangeUsingDFS(World world, BlockPos origin, BlockPos pos, int range, Set<BlockPos> visited) {
        visited.add(pos);
        radiateEntitiesInRange(world, pos, 0);
        for (Direction dir : Direction.values()) {
            BlockPos next = pos.offset(dir);
            if (next.isWithinDistance(origin, range) && !visited.contains(next) && world.getBlockState(next).isIn(ModTags.Blocks.RADIATION_GOES_THROUGH)) {
                radiateEntitiesInRangeUsingDFS(world, origin, next, range, visited);
            }
        }
    }

    public void radiateEntitiesInRange(World world, BlockPos pos, int range) {
        Box area = new Box(pos).expand(range); // radiation radius
        List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, area, e -> true);

        for (LivingEntity entity : entities) {
            // Entity is exposed to radiation
            if (this.canGiveEffect(entity)) {
                entity.addStatusEffect(new StatusEffectInstance(
                        this.getEffect(), 72, 0, true, true
                ));
            }
        }
    }

    public boolean usesDFS() {
        return true;
    }

    public int getRange() {
        return 5;
    }
}
