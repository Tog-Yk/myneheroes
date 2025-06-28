package net.togyk.myneheroes.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.PowerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidBlock.class)
public class FluidBlockMixin {
    @Inject(method = "getCollisionShape(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("HEAD"), cancellable = true)
    private void onGetCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx, CallbackInfoReturnable<VoxelShape> cir) {
        if (!(ctx instanceof EntityShapeContext esc)) {
            return;
        }
        Entity entity = ((EntityShapeContextAccessor) esc).getEntity();
        if (!(entity instanceof PlayerEntity player && !player.isSneaking() && entity.getBlockPos().getY() >= pos.getY() + 0.99)) {
            return;
        }


        for (Power power : PowerData.getPowersWithoutSyncing(player)) {
            if (power.canStandOnWater()) {
                VoxelShape surface = VoxelShapes.cuboid(0, 0.99, 0, 1, 1, 1);
                cir.setReturnValue(surface);
                return;
            }
        }
    }
}