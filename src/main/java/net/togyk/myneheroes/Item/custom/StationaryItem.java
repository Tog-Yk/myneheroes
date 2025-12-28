package net.togyk.myneheroes.Item.custom;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.togyk.myneheroes.entity.StationaryItemEntity;

public interface StationaryItem {
    default boolean tryPickup(PlayerEntity player, Hand hand) {
        return true;
    }
    default boolean hasToBeOwnerToPickup(ItemStack stack) {
        return false;
    }

    default StationaryItemEntity createBaseEntity(World world, ItemStack stack) {
        return new StationaryItemEntity(world);
    }

    default StationaryItemEntity createEntity(ItemEntity entity, ItemStack stack) {
        StationaryItemEntity newItemEntity = createBaseEntity(entity.getWorld(), stack);
        newItemEntity.setPos(entity.getX(), entity.getY(), entity.getZ());
        if (entity.getOwner() != null) {
            newItemEntity.setOwner(entity.getOwner());
        }
        newItemEntity.setItem(stack);
        newItemEntity.setVelocity(entity.getVelocity());
        newItemEntity.setFollowDirection(getStationaryFaceDirection().getUnitVector());

        return newItemEntity;
    }

    default ActionResult place(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getStack();

        if (!world.isClient() && player.isSneaking()) {
            StationaryItemEntity entity = createBaseEntity(player.getWorld(), stack);
            entity.setItem(stack.copyAndEmpty());
            entity.setOwner(player);
            Vec3d hitPos = context.getHitPos();
            Vec3d dimensionsVec = new Vec3d(entity.getWidth() / 2, 0, entity.getWidth() / 2);
            if (context.getSide() == Direction.DOWN) {
                dimensionsVec = new Vec3d(0, entity.getHeight(), 0);
            }

            Vec3d SideVec = Vec3d.of(context.getSide().getVector()).multiply(dimensionsVec);

            entity.setPosition(hitPos.add(SideVec));

            if (willFaceWhenPlaced(player, context.getHand())) {
                Vec3d look = player.getRotationVec(1.0F);
                entity.setFollowDirection(look.toVector3f());
            }

            world.spawnEntity(entity);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    default boolean willFaceWhenPlaced(PlayerEntity player, Hand hand) {
        return false;
    }

    default Direction getStationaryFaceDirection() {
        return Direction.NORTH;
    }
}
