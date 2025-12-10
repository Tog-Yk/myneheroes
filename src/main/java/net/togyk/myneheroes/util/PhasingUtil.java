package net.togyk.myneheroes.util;

import net.minecraft.block.Block;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.togyk.myneheroes.mixin.EntityShapeContextAccessor;
import net.togyk.myneheroes.power.Power;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class PhasingUtil {
    public static void handlePhasing(BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        VoxelShape voxelShape = cir.getReturnValue();
        if (voxelShape == null) {
            handlePhasing(pos, context, cir, 1);
        } else {
            handlePhasing(pos, context, cir, voxelShape.getMax(Direction.Axis.Y));
        }
    }

    public static void handlePhasing(BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir, double blockHeight) {
        if (!(context instanceof EntityShapeContext entityContext)) return;
        Entity entity = ((EntityShapeContextAccessor) entityContext).getEntity();
        if (!(entity instanceof PlayerEntity player)) return;

        for (Power power : PowerData.getPowersWithoutSyncing(player)) {
            if (!power.isPhasing()) continue;

            double blockTop = pos.getY() + blockHeight;

            if ((player.getPos().getY() < blockTop || (player.isSneaking() && !player.isOnGround())) && player.getPos().getY() + player.getBoundingBox().getLengthY() + 2 >= pos.getY()) {
                cir.setReturnValue(VoxelShapes.empty());
                break;
            }
        }
    }

    public static void handleStairPhasing(BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (!(context instanceof EntityShapeContext entityContext)) return;
        Entity entity = ((EntityShapeContextAccessor) entityContext).getEntity();
        if (!(entity instanceof PlayerEntity player)) return;

        for (Power power : PowerData.getPowersWithoutSyncing(player)) {
            if (!power.isPhasing()) continue;

            boolean isBelowTop = player.getPos().getY() < pos.getY() + 1F;
            boolean isBelowSlab = player.getPos().getY() < pos.getY() + 0.5F;

            if (player.getPos().getY() + player.getBoundingBox().getLengthY() + 2 >= pos.getY()) {
                if ((isBelowSlab || (player.isSneaking() && !player.isOnGround()))) {
                    cir.setReturnValue(VoxelShapes.empty());
                    break;
                } else if (isBelowTop) {
                    //set the collision shape to that of a bottom slab
                    VoxelShape bottomSlabShape = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
                    cir.setReturnValue(bottomSlabShape);
                    break;
                }
            }
        }
    }
}
