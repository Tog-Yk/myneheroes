package net.togyk.myneheroes.Item.custom;

import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.togyk.myneheroes.entity.StationaryItemEntity;
import net.togyk.myneheroes.entity.ThrownItemEntity;
import net.togyk.myneheroes.util.ModTags;

public class ContolableThrowableItem extends SwordItem implements StationaryItem, ThrowableItem {

    public ContolableThrowableItem(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public void Throw(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem() instanceof ContolableThrowableItem) {
            Vec3d look = player.getRotationVec(1.0F);

            ItemStack projectileStack = stack.copy();
            projectileStack.setCount(1);

            ThrownItemEntity projectile = new ThrownItemEntity(player.getWorld(), player, projectileStack, 0.5F);
            projectile.setOwner(player);
            projectile.setPosition(player.getX(), player.getEyeY(), player.getZ());
            projectile.setVelocity(look.x, look.y, look.z, 2.0F, 0.0F);
            projectile.setBounces(0);

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
    public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
        boolean canBeEnchant = super.canBeEnchantedWith(stack, enchantment, context);
        boolean isRiptide = enchantment.matchesKey(Enchantments.RIPTIDE);
        boolean isChanneling = enchantment.matchesKey(Enchantments.RIPTIDE);
        boolean isImpaling = enchantment.matchesKey(Enchantments.IMPALING);
        boolean inTag = stack.isIn(ModTags.Items.ONLY_LOYALTY);
        boolean isLoyaltyCompatable = (!inTag || (!isRiptide && !isChanneling && !isImpaling));
        return canBeEnchant && isLoyaltyCompatable;
    }

    @Override
    public Direction getFollowDirection() {
        return Direction.DOWN;
    }
}
