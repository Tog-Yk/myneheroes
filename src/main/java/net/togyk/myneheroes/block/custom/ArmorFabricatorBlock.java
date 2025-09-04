package net.togyk.myneheroes.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.togyk.myneheroes.client.screen.handeler.ArmorFabricatorScreenHandler;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class ArmorFabricatorBlock extends Block {
    public static final MapCodec<ArmorFabricatorBlock> CODEC = createCodec(ArmorFabricatorBlock::new);
    private static final Text TITLE = Text.translatable("container.myneheroes.armor_fabricator");
    public static final VoxelShape shape = Stream.of(
            Block.createCuboidShape(0.0, 0.0, 0.0, 6.0, 4.0, 6.0),

            Block.createCuboidShape(0.0, 0.0, 10.0, 6.0, 4.0, 16.0),

            Block.createCuboidShape(10.0, 0.0, 0.0, 16.0, 4.0, 6.0),

            Block.createCuboidShape(10.0, 0.0, 10.0, 16.0, 4.0, 16.0),

            Block.createCuboidShape(0.0, 12.0, 0.0, 16.0, 16.0, 16.0),

            Block.createCuboidShape(1.0, 0.1, 1.0, 15.0, 12.0, 15.0)
    ).reduce(VoxelShapes.empty(), VoxelShapes::union);

    @Override
    public MapCodec<ArmorFabricatorBlock> getCodec() {
        return CODEC;
    }

    public ArmorFabricatorBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
            player.incrementStat(Stats.INTERACT_WITH_STONECUTTER);
            return ActionResult.CONSUME;
        }
    }

    @Nullable
    protected NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((syncId, playerInventory, player) -> new ArmorFabricatorScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos)), TITLE);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return shape;
    }
}
