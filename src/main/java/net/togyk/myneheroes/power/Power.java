package net.togyk.myneheroes.power;

import net.minecraft.nbt.NbtCompound;
import net.togyk.myneheroes.ability.Ability;

import java.util.List;

public class Power {
    private final String name;
    private final float damageMultiplier;
    private final float resistance;
    public List<Ability> abilities;

    private boolean isDampened = false;
    int color;

    public Power(String name, float damageMultiplier, float resistance, int color, List<Ability> abilities) {
        this.name = name;
        this.damageMultiplier = damageMultiplier;
        this.resistance = resistance;
        this.color = color;
        this.abilities = abilities;
    }
    public NbtCompound getNbt() {
        NbtCompound nbt = new NbtCompound();
        return nbt;
    }

    public void readNbt(NbtCompound nbt) {
    }

    public String getName() {
        return name;
    }

    public boolean isDampened() {
        return isDampened;
    }

    public void setIsDampened(boolean dampened) {
        isDampened = dampened;
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public float getResistance() {
        return resistance;
    }

    public int getColor() {
        return color;
    }
}
