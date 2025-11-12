package net.togyk.myneheroes.Item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.PowerData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiFunction;

public class PowerUpgradeItem<P extends Power> extends Item {
    private final BiFunction<? super P, ItemStack, Boolean> isUpgradable;
    private final BiFunction<? super P, ItemStack, Boolean> upgrade;
    private final Class<P> powerClass;

    public PowerUpgradeItem(Class<P> powerClass, @NotNull BiFunction<P, ItemStack, Boolean> isUpgradable, @NotNull BiFunction<P, ItemStack, Boolean> upgrade, Settings settings) {
        super(settings);
        this.powerClass = powerClass;
        this.upgrade = upgrade;
        this.isUpgradable = isUpgradable;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (getPullProgress(this.getMaxUseTime(stack, user) - remainingUseTicks) == 1.0f && user instanceof PlayerEntity player) {
            List<Power> powers = PowerData.getPowers(player);
            for (Power power : powers) {
                if (powerClass.isInstance(power)) {
                    P upgradablePower = powerClass.cast(power);
                    if (this.isUpgradable.apply(upgradablePower, stack)) {
                        if (this.upgrade.apply(upgradablePower, stack)) {
                            if (!player.isCreative()) {
                                ItemStack remainder = stack.getRecipeRemainder();

                                stack.decrement(1);
                                player.getInventory().insertStack(remainder);
                            }
                        }
                    }
                }
            }


            player.incrementStat(Stats.USED.getOrCreateStat(this));
        }
    }

    public static float getPullProgress(int useTicks) {
        float f = useTicks / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        List<Power> powers = PowerData.getPowers(user);
        for (Power power : powers) {
            if (powerClass.isInstance(power)) {
                P upgradablePower = powerClass.cast(power);
                if (this.isUpgradable.apply(upgradablePower, stack)) {
                    user.setCurrentHand(hand);
                    return TypedActionResult.consume(stack);
                }
            }
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }


    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
}
