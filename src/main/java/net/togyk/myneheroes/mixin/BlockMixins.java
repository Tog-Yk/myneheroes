package net.togyk.myneheroes.mixin;

import net.minecraft.block.*;
import net.minecraft.block.enums.SlabType;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.togyk.myneheroes.util.PhasingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class BlockMixins {
    @Mixin(AbstractBlock.class)
    public static class BlockMixin {
        @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
        private void onGetCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
            if (state.isIn(BlockTags.SLABS) && state.get(SlabBlock.TYPE) == SlabType.BOTTOM) {
                PhasingUtil.handlePhasing(pos, context, cir, 0.5D);
            } else if (state.isIn(BlockTags.STAIRS)) {
                PhasingUtil.handleStairPhasing(pos, context, cir);
            } else if (state.getBlock() == Blocks.FARMLAND || state.getBlock() == Blocks.DIRT_PATH) {
                PhasingUtil.handlePhasing(pos, context, cir, 1.0D * 15 / 16);
            } else {
                PhasingUtil.handlePhasing(pos, context, cir);
            }
        }
    }
    @Mixin(CactusBlock.class)
    public static class CactusBlockMixin {
        @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
        private void onGetCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
            PhasingUtil.handlePhasing(pos, context, cir);
        }
    }
    @Mixin(HorizontalConnectingBlock.class)
    public static class HorizontalConnectingBlockMixin {
        @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
        private void onGetCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
            PhasingUtil.handlePhasing(pos, context, cir);
        }
    }
    @Mixin(FenceGateBlock.class)
    public static class FenceGateBlockMixin {
        @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
        private void onGetCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
            PhasingUtil.handlePhasing(pos, context, cir);
        }
    }
    @Mixin(WallBlock.class)
    public static class WallBlockMixin {
        @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
        private void onGetCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
            PhasingUtil.handlePhasing(pos, context, cir);
        }
    }
    @Mixin(BigDripleafBlock.class)
    public static class BigDripleafBlockMixin {
        @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
        private void onGetCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
            PhasingUtil.handlePhasing(pos, context, cir);
        }
    }
    @Mixin(GrindstoneBlock.class)
    public static class GrindstoneBlockMixin {
        @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
        private void onGetCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
            PhasingUtil.handlePhasing(pos, context, cir);
        }
    }
    @Mixin(BellBlock.class)
    public static class BellBlockMixin {
        @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
        private void onGetCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
            PhasingUtil.handlePhasing(pos, context, cir);
        }
    }
    @Mixin(LecternBlock.class)
    public static class LecternBlockMixin {
        @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
        private void onGetCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
            PhasingUtil.handlePhasing(pos, context, cir);
        }
    }
}
