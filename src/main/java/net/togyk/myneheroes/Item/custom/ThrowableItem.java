package net.togyk.myneheroes.Item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.togyk.myneheroes.entity.ThrownItemEntity;
import org.joml.Vector3f;

public interface ThrowableItem {
     default void Throw(World world, PlayerEntity player, Hand hand) {
         ItemStack stack = player.getStackInHand(hand);
         if (stack.getItem() instanceof ThrowableShieldItem) {
             Vec3d look = player.getRotationVec(1.0F);

             ItemStack projectileStack = stack.copy();
             projectileStack.setCount(1);

             PersistentProjectileEntity projectile = new ThrownItemEntity(player.getWorld(), player, projectileStack);
             projectile.setOwner(player);
             projectile.setPosition(player.getX(), player.getEyeY(), player.getZ());
             projectile.setVelocity(look.x, look.y, look.z, 3.0F, 0.0F);

             player.getWorld().spawnEntity(projectile);
             stack.decrement(1);
         }
     }

     default Vector3f getGroundedOffset(ThrownItemEntity entity, Direction hitSide) {
         return new Vector3f(0, 0, 0);
     }

     default float getFollowAnimationMaxSpeed() {
         return 2;
     }

     default AnimationType getAnimationType() {
         return AnimationType.FOLLOW;
     }

     default Direction getFollowDirection() {
         return Direction.NORTH;
     }

     default SpinType getSpinType() {
         return SpinType.NONE;
     }

     enum AnimationType {
         FOLLOW,
         FACE
     }

     enum SpinType {
         NONE,
         POS_PITCH,
         NEG_PITCH,
         POS_YAW,
         NEG_YAW,
         POS_ROLL,
         NEG_ROLL,
     }
}
