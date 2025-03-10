package net.togyk.myneheroes.power;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.ability.Ability;

import java.util.List;

public class Power {
    public final Identifier id;
    private final String name;
    private final float damageMultiplier;
    private final float resistance;
    public List<Ability> abilities;

    private boolean isDampened = false;
    private int color;

    public Power(Identifier id, String name, float damageMultiplier, float resistance, int color, List<Ability> abilities) {
        this.id = id;
        this.name = name;
        this.damageMultiplier = damageMultiplier;
        this.resistance = resistance;
        this.color = color;
        this.abilities = abilities;
    }
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putString("id", this.id.toString());
        nbt.putBoolean("is_dampened", this.isDampened);
        return nbt;
    }

    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("is_dampened")) {
            this.isDampened = nbt.getBoolean("is_dampened");
        }
    }

    public Identifier getId() {
        return id;
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

    public void tick() {

    }

    @Override
    public String toString() {
        return this.id.toString() + "{name: "+ this.name +
                ",\ndamageMultiplier: " + this.damageMultiplier +
                ",\nresistance: " + this.resistance +
                ",\nabilities:" + this.abilities.toString();
    }

    public Power copy() {
        return new Power(this.id, String.valueOf(this.getName()), this.getDamageMultiplier(), this.getResistance(), this.getColor(), List.copyOf(this.abilities));
    }
}
