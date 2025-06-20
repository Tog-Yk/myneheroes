package net.togyk.myneheroes.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.togyk.myneheroes.block.ModBlockEntityTypes;
import net.togyk.myneheroes.block.entity.UpgradeStationBlockEntity;
import org.jetbrains.annotations.Nullable;

public class UpgradeStationBlockEntityBlock extends Block implements BlockEntityProvider {
    public UpgradeStationBlockEntityBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof UpgradeStationBlockEntity upgradeStation && player != null) {
                player.openHandledScreen(upgradeStation);
            }
        }
        return ActionResult.success(world.isClient);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.UPGRADE_STATION_BLOCK_ENTITY.instantiate(pos, state);
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof UpgradeStationBlockEntity upgradeStation) {
                // Drop all items from inventory
                ItemScatterer.spawn(world, pos, upgradeStation.getInput());
                // Also clear the inventory if needed
                upgradeStation.getInput().clear();
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}
