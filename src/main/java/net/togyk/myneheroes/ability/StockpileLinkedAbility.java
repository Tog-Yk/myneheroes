package net.togyk.myneheroes.ability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.power.StockpilePower;

import java.util.function.Function;

public class StockpileLinkedAbility extends Ability{
    protected final int unlocksAt;
    protected final int cost;

    public StockpileLinkedAbility(Identifier id, String name, int cooldown, int unlocksAt, int cost, Ability.Settings settings, Function<PlayerEntity, Boolean> use) {
        super(id, name, cooldown, settings, use);
        this.unlocksAt = unlocksAt;
        this.cost = cost;
    }

    public int getUnlocksAt() {
        return unlocksAt;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public void Use(PlayerEntity player) {
        if (this.getCooldown() == 0 && this.getHolderItem() instanceof StockpilePower power && power.getCharge() >= this.getCost()) {
            if (this.use.apply(player)) {
                power.setCharge(power.getCharge() - this.getCost());
                this.setCooldown(this.getMaxCooldown());
            }
        }
        this.save();
    }

    @Override
    public StockpileLinkedAbility copy() {
        return new StockpileLinkedAbility(this.getId(), this.getName(), this.getMaxCooldown(), this.unlocksAt, this.cost, settings, use);
    }
}
