package net.togyk.myneheroes.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.EntityShapeContext;
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

@Mixin(AbstractBlock.class)
public class BlockMixin {
    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    private void onGetCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (context instanceof EntityShapeContext entityContext) {
            Entity entity = ((EntityShapeContextAccessor) entityContext).getEntity();
            if (entity == null) {
                return;
            }

            if (entity instanceof PlayerEntity player) {
                for (Power power : PowerData.getPowersWithoutSyncing(player)) {
                    if (power.isPhasing() && player.getPos().getY() < pos.getY() + 1 && player.getPos().getY() + player.getBoundingBox().getLengthY() + 2 >= pos.getY()) {
                        cir.setReturnValue(VoxelShapes.empty());
                        break;
                    }
                }
            }
        }
    }
}
