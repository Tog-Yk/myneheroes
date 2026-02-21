package net.togyk.myneheroes.ability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.util.StockPile;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class StockpileLinkedAbility extends Ability {
    protected final int unlocksAt;
    protected final int cost;

    public StockpileLinkedAbility(Identifier id, int cooldown, int unlocksAt, int cost, Ability.Settings settings, @Nullable Function<PlayerEntity, Boolean> use, Function<PlayerEntity, Boolean> hold, int maxHoldTime) {
        super(id, cooldown, settings, use, hold, maxHoldTime);
        this.unlocksAt = unlocksAt;
        this.cost = cost;
    }
    public StockpileLinkedAbility(Identifier id, int cooldown, int unlocksAt, int cost, Ability.Settings settings, @Nullable Function<PlayerEntity, Boolean> use, Function<PlayerEntity, Boolean> hold) {
        this(id, cooldown, unlocksAt, cost, settings, use, hold, 0);
    }

    public StockpileLinkedAbility(Identifier id, int cooldown, int unlocksAt, int cost, Ability.Settings settings, Function<PlayerEntity, Boolean> use) {
        this(id, unlocksAt, cost, cooldown, settings, use, null, 0);
    }

    public int getUnlocksAt() {
        return unlocksAt;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public void pressed(PlayerEntity player) {
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
    public void held(PlayerEntity player) {
        if (hold != null) {
            if (this.getCooldown() == 0 && this.getIndirectHolder() instanceof StockPile stockPile && stockPile.getCharge() >= this.getCost()) {
                if (this.getMaxHoldTime() != 0) {
                    int holdTime = this.getHoldTime();
                    if (holdTime > this.getMaxHoldTime()) {
                        this.setHoldTime(this.getMaxCooldown());
                        this.save();
                    }
                    if (holdTime >= this.getMaxHoldTime()) {
                        this.setCooldown(this.getMaxCooldown());
                        this.released(player);
                        return;
                    } else {
                        this.setHoldTime(holdTime + 1);
                        this.save();
                    }
                }


                if (this.hold.apply(player)) {
                    stockPile.setCharge(stockPile.getCharge() - this.getCost());
                }
            }
            this.save();
        }
    }

    @Override
    public boolean isHidden() {
        return super.isHidden() && this.getIndirectHolder() instanceof StockPile stockpile && stockpile.getCharge() >= this.getUnlocksAt();
    }

    @Override
    public StockpileLinkedAbility copy() {
        return new StockpileLinkedAbility(this.getId(), this.getMaxCooldown(), this.unlocksAt, this.cost, settings, use, hold, maxHoldTime);
    }
}
