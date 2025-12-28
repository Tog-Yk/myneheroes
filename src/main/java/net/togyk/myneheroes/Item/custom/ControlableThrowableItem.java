package net.togyk.myneheroes.Item.custom;

import com.google.common.base.Predicates;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.togyk.myneheroes.component.ModDataComponentTypes;
import net.togyk.myneheroes.entity.CallableStationaryItemEntity;
import net.togyk.myneheroes.entity.CallableThrownItemEntity;
import net.togyk.myneheroes.entity.StationaryItemEntity;
import net.togyk.myneheroes.util.ModTags;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ControlableThrowableItem extends SwordItem implements StationaryItem, ThrowableItem {
    public ControlableThrowableItem(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public void Throw(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem() instanceof ControlableThrowableItem) {
            Vec3d look = player.getRotationVec(1.0F);

            ItemStack projectileStack = stack.copy();
            projectileStack.setCount(1);

            CallableThrownItemEntity projectile = new CallableThrownItemEntity(player.getWorld(), player, projectileStack, 0.5F);
            projectile.setOwner(player);
            projectile.setPosition(player.getX(), player.getEyeY(), player.getZ());
            projectile.setVelocity(look.x, look.y, look.z, 2.0F, 0.0F);
            projectile.setBounces(0);

            projectile.setTargetUuids(getMarkedEntityUuids(projectileStack));

            player.getWorld().spawnEntity(projectile);
            stack.decrement(1);
        }
    }

    @Override
    public StationaryItemEntity createBaseEntity(World world, ItemStack stack) {
        return new CallableStationaryItemEntity(world);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return place(context);
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
        boolean canBeEnchant = super.canBeEnchantedWith(stack, enchantment, context);
        boolean isRiptide = enchantment.matchesKey(Enchantments.RIPTIDE);
        boolean isChanneling = enchantment.matchesKey(Enchantments.RIPTIDE);
        boolean isImpaling = enchantment.matchesKey(Enchantments.IMPALING);
        boolean inTag = stack.isIn(ModTags.Items.ONLY_LOYALTY);
        boolean isLoyaltyCompatible = (!inTag || (!isRiptide && !isChanneling && !isImpaling));
        return canBeEnchant && isLoyaltyCompatible;
    }

    @Override
    public Direction getThrownFollowDirection() {
        return Direction.DOWN;
    }

    @Override
    public boolean willFaceWhenPlaced(PlayerEntity player, Hand hand) {
        //todo only when player is worthy
        return true;
    }

    @Override
    public Direction getStationaryFaceDirection() {
        return Direction.DOWN;
    }

    //homing

    public static List<UUID> getMarkedEntityUuids(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentTypes.MARKED_ENTITIES, new ArrayList<>());
    }

    public static List<LivingEntity> getMarkedEntities(World world, BlockPos pos, ItemStack stack) {
        List<UUID> uuids = getMarkedEntityUuids(stack);
        Box box = new Box(pos).expand(512);
        return world.getEntitiesByClass(LivingEntity.class, box, Predicates.alwaysTrue()).stream()
                .filter(
                        found -> uuids.stream().anyMatch(uuid -> uuid.equals(found.getUuid())))
                .toList();
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        //if buffer is 0 empty marked
        int timer = getTimer(stack);
        if (timer == 0) {
            emptyMarkedEntities(stack);
        }
        if (timer >= 0) {
            setTimer(stack, timer - 1);
        }

        //if in main hand
        if (stack == entity.getWeaponStack()) {
            for (LivingEntity marked : getMarkedEntities(world, entity.getBlockPos(), stack)) {
                marked.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.GLOWING, 2, 0, true, true
                ), entity);
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand); // starts "using"
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }


    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity player)) return;

        if (!world.isClient) {
            markLookedAtEntity(world, player, stack);
        }

        setTimer(stack, -1);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
        setTimer(stack, 40);
    }

    private void emptyMarkedEntities(ItemStack stack) {
        stack.set(ModDataComponentTypes.MARKED_ENTITIES, new ArrayList<>());
    }

    private void markLookedAtEntity(World world, PlayerEntity player, ItemStack stack) {
        LivingEntity target = raycastEntity(player, 512);

        if (target != null) {
            addMarked(stack, target);
            // Optional feedback:
            // player.sendMessage(Text.literal("Marked: " + target.getName().getString()), true);
        }
    }

    private static LivingEntity raycastEntity(PlayerEntity player, double distance) {
        Vec3d start = player.getEyePos();
        Vec3d direction = player.getRotationVec(1.0f);
        Vec3d end = start.add(direction.multiply(distance));

        Box box = player.getBoundingBox().stretch(direction.multiply(distance)).expand(1.0);

        EntityHitResult result = ProjectileUtil.raycast(player, start, end, box, entity -> !entity.isSpectator() && entity.isAttackable() && entity instanceof LivingEntity, distance);

        BlockHitResult blockHit = player.getWorld().raycast(new RaycastContext(
                start,
                end,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                player
        ));

        if (result != null) {
            boolean hitEntityBeforeBlock = blockHit.getType() == HitResult.Type.MISS || start.squaredDistanceTo(result.getPos()) < start.squaredDistanceTo(blockHit.getPos());
            if (hitEntityBeforeBlock) {
                return (LivingEntity) result.getEntity();
            }
        }
        return null;
    }

    private static void addMarked(ItemStack stack, Entity entity) {
        List<UUID> marked = stack.getOrDefault(ModDataComponentTypes.MARKED_ENTITIES, new ArrayList<>());
        List<UUID> uuids = new ArrayList<>(marked);

        if (!uuids.contains(entity.getUuid())) {
            uuids.add(entity.getUuid());
        }

        stack.set(ModDataComponentTypes.MARKED_ENTITIES, uuids);
    }

    public static int getTimer(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentTypes.TIMER, -1);
    }

    public static void setTimer(ItemStack stack, int time) {
        stack.set(ModDataComponentTypes.TIMER, time);
    }
}
