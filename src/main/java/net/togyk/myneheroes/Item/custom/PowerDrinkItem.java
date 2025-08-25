package net.togyk.myneheroes.Item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.PowerData;

import java.util.List;

public class PowerDrinkItem extends Item {
    private final Power power;
    private final List<RegistryEntry<StatusEffect>> sideEffects;

    public PowerDrinkItem(Power power, List<RegistryEntry<StatusEffect>> sideEffects, Settings settings) {
        super(settings);
        this.power = power;
        this.sideEffects = sideEffects;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 32;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) {
            if (user instanceof PlayerEntity player) {
                for (RegistryEntry<StatusEffect> effect : sideEffects) {
                    player.addStatusEffect(new StatusEffectInstance(effect, 200, 0));
                }

                PowerData.addUniquePowerToLimit(player, power.copy());

                if (!player.isCreative()) {
                    ItemStack remainder = stack.getRecipeRemainder();
                    stack.decrement(1);
                    return remainder;
                }
            }
        }

        return stack;
    }
}
