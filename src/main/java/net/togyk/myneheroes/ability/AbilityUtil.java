package net.togyk.myneheroes.ability;

import net.minecraft.item.ArmorItem;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AbilityUtil {
    private static final Map<ArmorItem.Type,List<Ability>> standardArmorAbilities;

    private static final Map<String,List<Ability>> powerAbilities;
    static {
        Map<ArmorItem.Type,List<Ability>> abilitiesPerType = new HashMap<>();
        List<Ability> HelmetAbilities = new ArrayList<>();
        HelmetAbilities.add(new BooleanAbility("toggle_hud"));
        abilitiesPerType.put(ArmorItem.Type.HELMET, HelmetAbilities);
        List<Ability> abilities = new ArrayList<>();
        abilities.add(new ShootLaserAbilityFromReactor("shoot_laser", 10));
        abilitiesPerType.put(ArmorItem.Type.CHESTPLATE,abilities);
        standardArmorAbilities = abilitiesPerType;

        Map<String,List<Ability>> abilitiesPerPower = new HashMap<>();
        List<Ability> kryptonian = new ArrayList<>();
        kryptonian.add(new LasersFromEyesAbility("laser_eyes", 2));
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
}
