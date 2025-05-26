package net.togyk.myneheroes.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.togyk.myneheroes.ability.Abilities;
import net.togyk.myneheroes.ability.Ability;

import java.util.*;

public class AbilityUtil {

    public static Ability getAbilityMatchingId(List<Ability> abilityList, Identifier id) {
        for (Ability ability : abilityList) {
            if (ability != null && ability.getId() == id) {
                return ability;
            }
        }
        return null;
    }

    public static List<Ability> getAbilitiesMatchingId(List<Ability> abilityList, Identifier id) {
        List<Ability> abilities = new ArrayList<>();
        for (Ability ability : abilityList) {
            if (ability != null && ability.getId() == id) {
                abilities.add(ability);
            }
        }
        return abilities;
    }

    public static Ability nbtToAbility(NbtCompound nbt) {
        Identifier abilityId = Identifier.of(nbt.getString("id"));
        Ability ability = Abilities.get(abilityId);
        if (ability != null) {
            ability.readNbt(nbt);
        }
        return ability;
    }
}
