package net.togyk.myneheroes.Item.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.togyk.myneheroes.entity.LaserEntity;
import net.togyk.myneheroes.entity.ModEntities;
import net.togyk.myneheroes.entity.ThrownItemEntity;

public class ThrowableShieldItem extends ShieldItem implements StationaryItem, ThrowableItem{
    private final float bonusAttackDamage;
    private final EntityType<LaserEntity> projectileEntityType;

    public ThrowableShieldItem(EntityType<LaserEntity> projectileEntity, float bonusAttackDamage, Settings settings) {
        super(settings);
        this.bonusAttackDamage = bonusAttackDamage;
        this.projectileEntityType = projectileEntity;
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        return this.bonusAttackDamage;
    }

    public EntityType<LaserEntity> getProjectileEntityType() {
        return projectileEntityType;
    }

    @Override
    public ActionResult interactEntity(PlayerEntity player, Hand hand) {
        return ActionResult.PASS;
    }

    @Override
    public void Throw(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem() instanceof ThrowableShieldItem) {
            Vec3d look = player.getRotationVec(1.0F);

            ItemStack projectileStack = stack.copy();
            projectileStack.setCount(1);

            PersistentProjectileEntity projectile = new ThrownItemEntity(ModEntities.THROWN_ITEM, player.getWorld(), player, projectileStack);
            projectile.setOwner(player);
            projectile.setPosition(player.getX(), player.getEyeY(), player.getZ());
            projectile.setVelocity(look.x, look.y, look.z, 3.0F, 0.0F);
            projectile.applyDamageModifier(bonusAttackDamage);

            player.getWorld().spawnEntity(projectile);
            stack.decrement(1);
        }
    }
}
