package net.togyk.myneheroes.block.entity;

import com.google.common.base.Predicates;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.togyk.myneheroes.block.ModBlockEntityTypes;
import net.togyk.myneheroes.block.custom.MeteorRadarBlockEntityBlock;
import net.togyk.myneheroes.entity.MeteorEntity;

import java.util.ArrayList;
import java.util.List;

public class MeteorRadarBlockEntity extends BlockEntity {
    List<MeteorEntity> meteors = new ArrayList<>();
    int range = 400;

    public MeteorRadarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.METEOR_RADAR_BLOCK_ENTITY, pos, state);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        int initialSize = this.meteors.size();
        this.meteors = world.getEntitiesByClass(MeteorEntity.class, new Box(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range), Predicates.alwaysTrue());
        if (state.getBlock() instanceof MeteorRadarBlockEntityBlock) {
            world.setBlockState(pos, state.with(MeteorRadarBlockEntityBlock.foundMeteor, !meteors.isEmpty()));
            if (initialSize != meteors.size()) {
                state.updateNeighbors(world, pos, 0);
            }
        }
    }

    public List<MeteorEntity> getMeteorsInRange() {
        return meteors;
    }

    public int getRange() {
        return range;
    }
}
