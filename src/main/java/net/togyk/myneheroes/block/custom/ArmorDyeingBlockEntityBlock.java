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
import net.togyk.myneheroes.block.entity.ArmorDyeingBlockEntity;
import org.jetbrains.annotations.Nullable;

public class ArmorDyeingBlockEntityBlock extends Block implements BlockEntityProvider {
    public ArmorDyeingBlockEntityBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ArmorDyeingBlockEntity dyeingBlock && player != null) {
                player.openHandledScreen(dyeingBlock);
            }
        }
        return ActionResult.success(world.isClient);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.ARMOR_DYEING_BLOCK_ENTITY.instantiate(pos, state);
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ArmorDyeingBlockEntity dyeingBlock) {
                // Drop all items from inventory
                ItemScatterer.spawn(world, pos, dyeingBlock.getInput());
                ItemScatterer.spawn(world, pos, dyeingBlock.getFuel());
                // Also clear the inventory if needed
                dyeingBlock.getInput().clear();
                dyeingBlock.getFuel().clear();
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}
