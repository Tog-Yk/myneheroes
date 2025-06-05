package net.togyk.myneheroes.ability.detailed;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.ReactorItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.BooleanAbility;

public class FlyFromReactorAbility extends BooleanAbility {
    private final int cost;

    public FlyFromReactorAbility(Identifier id, Ability.Settings settings, int cost) {
        super(id, settings);
        this.cost = cost;
    }

    @Override
    public void tick(PlayerEntity player) {
        if (this.get() && player.getAbilities().flying) {
            ItemStack reactorStack = MyneHeroes.getReactorItemClass(player);
            if (reactorStack.getItem() instanceof ReactorItem reactor) {
                int reactorPower = reactor.getStoredPowerOrDefault(reactorStack, 0);
                if (reactorPower >= this.getCost()) {
                    reactor.setStoredPower(reactorStack, reactorPower - this.getCost());
                }
            }
        }
        super.tick(player);
    }

    @Override
    public boolean get() {
        if (super.get()) {
            if (this.getIndirectHolder() instanceof ItemStack stack && stack.getHolder() instanceof PlayerEntity player) {
                ItemStack reactorStack = MyneHeroes.getReactorItemClass(player);
                if (reactorStack.getItem() instanceof ReactorItem reactor) {
                    int reactorPower = reactor.getStoredPowerOrDefault(reactorStack, 0);
                    return reactorPower >= this.getCost();
                }
            }
        }
        return false;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public FlyFromReactorAbility copy() {
        return new FlyFromReactorAbility(id, settings, cost);
    }
}
