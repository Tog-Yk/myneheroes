package net.togyk.myneheroes.ability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.util.StockPile;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class StockpileLinkedAbility extends Ability {
    protected final int unlocksAt;
    protected final int cost;

    public StockpileLinkedAbility(Identifier id, int cooldown, int unlocksAt, int cost, Ability.Settings settings, Function<PlayerEntity, Boolean> use) {
        super(id, cooldown, settings, use);
        this.unlocksAt = unlocksAt;
        this.cost = cost;
    }
    public StockpileLinkedAbility(Identifier id, int cooldown, int unlocksAt, int cost, Ability.Settings settings, @Nullable Function<PlayerEntity, Boolean> use, Function<PlayerEntity, Boolean> hold) {
        super(id, cooldown, settings, use, hold);
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
    public void use(PlayerEntity player) {
        if (use != null) {
            if (this.getCooldown() == 0 && this.getIndirectHolder() instanceof StockPile stockPile && stockPile.getCharge() >= this.getCost()) {
                if (this.use.apply(player)) {
                    stockPile.setCharge(stockPile.getCharge() - this.getCost());
                    this.setCooldown(this.getMaxCooldown());
                }
            }
            this.save();
        }
    }

    @Override
    public void usePressed(PlayerEntity player) {
        if (hold != null) {
            if (this.getCooldown() == 0 && this.getIndirectHolder() instanceof StockPile stockPile && stockPile.getCharge() >= this.getCost()) {
                if (this.hold.apply(player)) {
                    stockPile.setCharge(stockPile.getCharge() - this.getCost());
                }
            }
            this.save();
        }
    }

    @Override
    public boolean Usable() {
        return super.Usable() && this.getIndirectHolder() instanceof StockPile stockpile && stockpile.getCharge() >= this.getUnlocksAt();
    }

    @Override
    public StockpileLinkedAbility copy() {
        return new StockpileLinkedAbility(this.getId(), this.getMaxCooldown(), this.unlocksAt, this.cost, settings, use, hold);
    }
}
