package net.togyk.myneheroes.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.togyk.myneheroes.block.ModBlockEntityTypes;
import net.togyk.myneheroes.block.entity.MeteorRadarBlockEntity;
import org.jetbrains.annotations.Nullable;

public class MeteorRadarBlockEntityBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final BooleanProperty foundMeteor = Properties.LIT;

    public static final MapCodec<MeteorRadarBlockEntityBlock> CODEC = MeteorRadarBlockEntityBlock.createCodec(MeteorRadarBlockEntityBlock::new);

    public MeteorRadarBlockEntityBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.METEOR_RADAR_BLOCK_ENTITY.instantiate(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlockEntityTypes.METEOR_RADAR_BLOCK_ENTITY,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if (state.get(foundMeteor)) {
            return 15;
        } else {
            return 0;
        }
    }

    @Override
    protected boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        BlockEntity var5 = world.getBlockEntity(pos);
        if (var5 instanceof MeteorRadarBlockEntity meteorRadarBlockEntity) {
            return Math.min(meteorRadarBlockEntity.getMeteorsInRange().size(), 15);
        } else {
            return 0;
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(foundMeteor);
    }
}
