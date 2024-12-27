package net.togyk.myneheroes.power;

import net.minecraft.nbt.NbtCompound;
import net.togyk.myneheroes.ability.Ability;

import java.util.List;

public class Power {
    private final String name;
    private final int damageMultiplier;
    public List<Ability> abilities;

    private boolean isDampened = false;

    public Power(String name, int damageMultiplier, List<Ability> abilities) {
        this.name = name;
        this.damageMultiplier = damageMultiplier;
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

    public int getDamageMultiplier() {
        return damageMultiplier;
    }
}
