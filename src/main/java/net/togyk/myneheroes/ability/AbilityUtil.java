package net.togyk.myneheroes.ability;

import net.minecraft.item.ArmorItem;

import java.util.*;

public class AbilityUtil {
    private static final Map<ArmorItem.Type,List<Ability>> standardArmorAbilities;
    static {
        Map<ArmorItem.Type,List<Ability>> abilitiesPerType = new HashMap<>();
        List<Ability> HelmetAbilities = new ArrayList<>();
        HelmetAbilities.add(new BooleanAbility("toggle_hud"));
        abilitiesPerType.put(ArmorItem.Type.HELMET, HelmetAbilities);
        List<Ability> abilities = new ArrayList<>();
        abilities.add(new ShootLazarAbilityFromReactor("shoot_lazar", 10));
        abilitiesPerType.put(ArmorItem.Type.CHESTPLATE,abilities);
        standardArmorAbilities = abilitiesPerType;
    }

    public static Ability getAbilityMatchingName(List<Ability> abilityList, String name) {
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
}
