package net.togyk.myneheroes.ability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.power.StockpilePower;

public class StockpileLinkedAbility extends Ability{
    protected final int unlocksAt;
    protected final int cost;

    public StockpileLinkedAbility(Identifier id, String name, int cooldown, int unlocksAt, int cost) {
        super(id, name, cooldown);
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
            power.setCharge(power.getCharge() - this.getCost());
            this.setCooldown(this.getMaxCooldown());
        }
        this.save();
    }

    @Override
    public StockpileLinkedAbility copy() {
        return new StockpileLinkedAbility(this.getId(), this.getName(), this.getMaxCooldown(), this.unlocksAt, this.cost);
    }
}
