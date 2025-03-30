package net.togyk.myneheroes.ability;

import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;

import java.util.HashMap;
import java.util.Map;

public class Abilities {
    private static final Map<Identifier,Ability> ABILITIES = new HashMap<>();

    public static final Ability TOGGLE_HUD = registerAbility(new BooleanAbility(Identifier.of(MyneHeroes.MOD_ID, "toggle_hud"), "toggle_hud"));
    public static final Ability SHOOT_LAZAR = registerAbility(new ShootLazarAbilityFromReactor(Identifier.of(MyneHeroes.MOD_ID, "shoot_lazar"), "shoot_lazar", 10));
    public static final Ability LAZAR_EYES = registerAbility(new LazarsFromEyesAbility(Identifier.of(MyneHeroes.MOD_ID, "lazar_eyes"), "lazar_eyes", 2));

    public static void registerAbilities() {
        MyneHeroes.LOGGER.info("registering Abilities for" + MyneHeroes.MOD_ID);
    }

    private static Ability registerAbility(Ability ability) {
        if (!ABILITIES.containsKey(ability.id)) {
            ABILITIES.put(ability.id, ability);
            return ability;
        } else {
            MyneHeroes.LOGGER.error("There already exist an power with the id of {}", ability.id);
        }
        return null;
    }

    public static Ability get(Identifier id) {
        return ABILITIES.getOrDefault(id, null).copy();
    }
}
