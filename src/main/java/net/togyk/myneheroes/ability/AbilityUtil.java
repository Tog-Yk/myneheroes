package net.togyk.myneheroes.ability;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import java.util.*;

public class AbilityUtil {

    public static Ability getAbilityMatchingName(List<Ability> abilityList, String name) {
        for (Ability ability : abilityList) {
            if (ability != null && Objects.equals(ability.getName(), name)) {
                return ability;
            }
        }
        return null;
    }

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
