package net.togyk.myneheroes.power;

import net.minecraft.nbt.NbtCompound;
import net.togyk.myneheroes.ability.Ability;

import java.util.ArrayList;
import java.util.List;

public class Power {
    public List<Ability> abilities;

    public Power(List<Ability> abilities) {
        this.abilities = abilities;
    }
    public NbtCompound getNbt() {
        NbtCompound nbt = new NbtCompound();
        return nbt;
    }

    public void readNbt(NbtCompound nbt) {
    }
}
