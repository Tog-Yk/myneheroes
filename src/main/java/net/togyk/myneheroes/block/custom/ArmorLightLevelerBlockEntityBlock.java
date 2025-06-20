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
import net.togyk.myneheroes.block.entity.ArmorLightLevelerBlockEntity;
import org.jetbrains.annotations.Nullable;

public class ArmorLightLevelerBlockEntityBlock extends Block implements BlockEntityProvider {
    public ArmorLightLevelerBlockEntityBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ArmorLightLevelerBlockEntity lightLevelerBlock && player != null) {
                player.openHandledScreen(lightLevelerBlock);
            }
        }
        return ActionResult.success(world.isClient);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.ARMOR_LIGHT_LEVELER_BLOCK_ENTITY.instantiate(pos, state);
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ArmorLightLevelerBlockEntity lightLevelerBlock) {
                // Drop all items from inventory
                ItemScatterer.spawn(world, pos, lightLevelerBlock.getInventory());
                // Also clear the inventory if needed
                lightLevelerBlock.getInventory().clear();
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}
