package net.togyk.myneheroes.power;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.StockpileLinkedAbility;

import java.util.ArrayList;
import java.util.List;

public class StockpilePower extends Power {
    private int charge = 0;
    private final int maxCharge;

    private final Identifier chargeIcon;

    public StockpilePower(Identifier id, int maxCharge, float damageMultiplier, float resistance, int color, List<Ability> abilities) {
        super(id, damageMultiplier, resistance, color, abilities);
        this.maxCharge = maxCharge;
        this.chargeIcon = Identifier.of(id.getNamespace(),"textures/power/charge/"+id.getPath()+".png");;
    }

    @Override
    public void tick(PlayerEntity player) {
        super.tick(player);
        if (this.getMaxCharge() != this.getCharge() && !this.isDampened()) {
            setCharge(getCharge() + 1);
        }
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = Math.min(charge, this.getMaxCharge());
    }

    public int getMaxCharge() {
        return maxCharge;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("charge", this.getCharge());
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("charge")) {
            this.setCharge(nbt.getInt("charge"));
        }
    }

    @Override
    public float getDamageMultiplier() {
        return super.getDamageMultiplier() * (float) (Math.sqrt((double) this.getCharge() /this.getMaxCharge()));
    }

    @Override
    public float getResistance() {
        return 1.0F - (1.0F - resistance) * (float) (Math.sqrt((double) this.getCharge() / this.getMaxCharge()));
    }

    public Identifier getChargeIcon() {
        return chargeIcon;
    }

    @Override
    public StockpilePower copy() {
        return new StockpilePower(this.id, this.getMaxCharge(), damageMultiplier, resistance, this.getColor(), List.copyOf(this.abilities));
    }

    @Override
    public List<Ability> getAbilities() {
        List<Ability> abilities = new ArrayList<>();
        for (Ability ability: this.abilities) {
            if (ability instanceof StockpileLinkedAbility linkedAbility) {
                if (this.getCharge() >= linkedAbility.getUnlocksAt()) {
                    abilities.add(ability);
                }
            } else {
                abilities.add(ability);
            }
        }
        return abilities;
    }
}
