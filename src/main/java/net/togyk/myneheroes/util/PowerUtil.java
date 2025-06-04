package net.togyk.myneheroes.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.Powers;

import java.util.ArrayList;
import java.util.List;

public class PowerUtil {

    public static Power getPowerMatchingId(List<Power> powerList, Identifier id) {
        for (Power power : powerList) {
            if (power != null && power.getId().equals(id)) {
                return power;
            }
        }
        return null;
    }

    public static List<Power> getPowersMatchingId(List<Power> powerList, Identifier id) {
        List<Power> powers = new ArrayList<>();
        for (Power power : powerList) {
            if (power != null && power.getId().equals(id)) {
                powers.add(power);
            }
        }
        return powers;
    }

    public static Power nbtToPower(NbtCompound nbt) {
        Identifier powerId = Identifier.of(nbt.getString("id"));
        Power power = Powers.get(powerId);
        if (power != null) {
            power.readNbt(nbt);
        }
        return power;
    }
}
