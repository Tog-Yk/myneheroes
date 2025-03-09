package net.togyk.myneheroes.ability;

import net.minecraft.item.ArmorItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AbilityUtil {
    private static final Map<ArmorItem.Type,List<Ability>> standardArmorAbilities;

    private static final Map<String,List<Ability>> powerAbilities;
    static {
        Map<ArmorItem.Type,List<Ability>> abilitiesPerType = new HashMap<>();
        List<Ability> HelmetAbilities = new ArrayList<>();
        HelmetAbilities.add(Abilities.get(Identifier.of(MyneHeroes.MOD_ID, "toggle_hud")));
        abilitiesPerType.put(ArmorItem.Type.HELMET, HelmetAbilities);
        List<Ability> abilities = new ArrayList<>();
        abilities.add(Abilities.get(Identifier.of(MyneHeroes.MOD_ID, "shoot_lazar")));
        abilitiesPerType.put(ArmorItem.Type.CHESTPLATE,abilities);
        standardArmorAbilities = abilitiesPerType;

        Map<String,List<Ability>> abilitiesPerPower = new HashMap<>();
        List<Ability> kryptonian = new ArrayList<>();
        kryptonian.add(Abilities.get(Identifier.of(MyneHeroes.MOD_ID, "lazar_eyes")));
        abilitiesPerPower.put("kryptonian", kryptonian);

        powerAbilities = abilitiesPerPower;
    }

    public static Ability getAbilityMatchingName(@NotNull List<Ability> abilityList, String name) {
        for (Ability ability : abilityList) {
            if (Objects.equals(ability.getName(), name)) {
                return ability;
            }
        }
        return null;
    }

    public static List<Ability> getStandardArmorAbilities(ArmorItem.Type type) {
        return standardArmorAbilities.get(type);
    }

    public static List<Ability> getAbilitiesForPower(String powerName) {
        return powerAbilities.containsKey(powerName) ? powerAbilities.get(powerName) : new ArrayList<>();
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
