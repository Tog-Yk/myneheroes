package net.togyk.myneheroes.Item.custom;

import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShieldItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.togyk.myneheroes.entity.StationaryItemEntity;
import net.togyk.myneheroes.entity.ThrownItemEntity;

public class ThrowableShieldItem extends ShieldItem implements StationaryItem, ThrowableItem {
    private final float bonusAttackDamage;

    public ThrowableShieldItem(float bonusAttackDamage, Settings settings) {
        super(settings);
        this.bonusAttackDamage = bonusAttackDamage;
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        return this.bonusAttackDamage;
    }

    @Override
    public void Throw(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem() instanceof ThrowableShieldItem) {
            Vec3d look = player.getRotationVec(1.0F);

            ItemStack projectileStack = stack.copy();
            projectileStack.setCount(1);

            PersistentProjectileEntity projectile = new ThrownItemEntity(player.getWorld(), player, projectileStack);
            projectile.setOwner(player);
            projectile.setPosition(player.getX(), player.getEyeY(), player.getZ());
            projectile.setVelocity(look.x, look.y, look.z, 2.0F, 0.0F);
            projectile.applyDamageModifier(bonusAttackDamage);

            player.getWorld().spawnEntity(projectile);
            stack.decrement(1);
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();

        if (!world.isClient() && player.isSneaking()) {
            StationaryItemEntity entity = new StationaryItemEntity(world);
            entity.setItem(context.getStack().copyAndEmpty());
            entity.setOwner(player);
            Vec3d hitPos = context.getHitPos();
            Vec3d SideVec = Vec3d.of(context.getSide().getVector()).multiply(0.4);

            entity.setPosition(hitPos.add(SideVec));

            world.spawnEntity(entity);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }


    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
        return enchantment == Enchantments.LOYALTY || super.canBeEnchantedWith(stack, enchantment, context);
    }
}
