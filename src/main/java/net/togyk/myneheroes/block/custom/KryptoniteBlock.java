package net.togyk.myneheroes.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.togyk.myneheroes.block.ModBlockEntityTypes;
import org.jetbrains.annotations.Nullable;


public class KryptoniteBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final MapCodec<KryptoniteBlock> CODEC = KryptoniteBlock.createCodec(KryptoniteBlock::new);

    public KryptoniteBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.KRYPTONITE_BLOCK_ENTITY.instantiate(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient()) {
            return null;
        }

        return validateTicker(type, ModBlockEntityTypes.KRYPTONITE_BLOCK_ENTITY,
                (world1, pos, state1, blockEntity) -> blockEntity.tick((ServerWorld) world1, pos, state1));
    }
}
