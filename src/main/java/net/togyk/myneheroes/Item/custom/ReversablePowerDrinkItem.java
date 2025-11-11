package net.togyk.myneheroes.Item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.PowerData;

import java.util.List;

public class ReversablePowerDrinkItem extends PowerDrinkItem {
    public ReversablePowerDrinkItem(Identifier power, List<RegistryEntry<StatusEffect>> sideEffects, Settings settings) {
        super(power, sideEffects, settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) {
            if (user instanceof PlayerEntity player) {
                for (RegistryEntry<StatusEffect> effect : getSideEffects()) {
                    player.addStatusEffect(new StatusEffectInstance(effect, 200, 0));
                }
                Power power = this.getPower(player);

                List<Power> powers = PowerData.getPowers(player);
                if (powers.stream().map(Power::getId).toList().contains(power.getId())) {
                    int index = powers.stream().map(Power::getId).toList().indexOf(power.getId());
                    PowerData.removePower(player, powers.get(index));
                } else {
                    PowerData.addUniquePowerToLimit(player, power.copy());
                }

                if (!player.isCreative()) {
                    ItemStack remainder = stack.getRecipeRemainder();
                    if (stack.getCount() == 1) {
                        return remainder;
                    }

                    player.getInventory().insertStack(remainder);
                    player.getInventory().markDirty();

                    stack.decrement(1);

                    return stack;
                }
            }
        }

        return stack;
    }
}
