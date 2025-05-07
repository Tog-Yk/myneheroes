package net.togyk.myneheroes.block.entity;

import com.google.common.base.Predicates;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.togyk.myneheroes.block.ModBlockEntityTypes;
import net.togyk.myneheroes.block.custom.MeteorRadarBlockEntityBlock;
import net.togyk.myneheroes.entity.MeteorEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MeteorRadarBlockEntity extends BlockEntity {
    List<MeteorEntity> meteors = new ArrayList<>();
    int range = 300;

    public MeteorRadarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.METEOR_RADAR_BLOCK_ENTITY, pos, state);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        List<MeteorEntity> buffer = world.getEntitiesByClass(MeteorEntity.class, new Box(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range), Predicates.alwaysTrue());
        if (state.getBlock() instanceof MeteorRadarBlockEntityBlock) {
            world.setBlockState(pos, state.with(MeteorRadarBlockEntityBlock.foundMeteor, !meteors.isEmpty()));
            if (this.meteors.size() != buffer.size()) {

                Set<BlockPos> set = Sets.newHashSet();
                set.add(pos);

                for(Direction direction : Direction.values()) {
                    set.add(pos.offset(direction));
                }

                for(BlockPos blockPos : set) {
                    world.updateNeighborsAlways(blockPos, state.getBlock());
                }
            }
        }
        this.meteors = buffer.stream().filter(meteor -> !meteor.isRemoved()).collect(Collectors.toList());
    }

    public List<MeteorEntity> getMeteorsInRange() {
        return meteors;
    }

    public int getRange() {
        return range;
    }
}
