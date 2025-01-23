package net.togyk.myneheroes.power;

import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.AbilityUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Powers {
    private static final Map<Identifier,Power> POWERS = new HashMap<>();

    public static void registerPowers() {
        MyneHeroes.LOGGER.info("registering Powers for" + MyneHeroes.MOD_ID);

        registerPower(Identifier.of(MyneHeroes.MOD_ID, "kryptonian"), new Power("kryptonian", 5, 0xFFBB0033, AbilityUtil.getAbilitiesForPower("kryptonian")));
    }

    public static void registerPower(Identifier id, Power power) {
        if (!POWERS.containsKey(id)) {
            POWERS.put(id, power);
        } else {
            MyneHeroes.LOGGER.error("There already exist an power with the id of {}",id);
        }
    }

    public static Power get(Identifier id) {
        return POWERS.getOrDefault(id, null);
    }

    public static Identifier getFirstMatchingId(Power power) {
        if (POWERS.containsValue(power)) {
            for (Map.Entry<Identifier, Power> entry : POWERS.entrySet()) {
                if (entry.getValue().equals(power)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public static Set<Identifier> getMatchingIds(Power power) {
        Set<Identifier> identifiers = new HashSet<>();
        if (POWERS.containsValue(power)) {
            for (Map.Entry<Identifier, Power> entry : POWERS.entrySet()) {
                if (entry.getValue().equals(power)) {
                    identifiers.add(entry.getKey());
                }
            }
        }
        return identifiers;
    }
    public static boolean containsId(Identifier id) {
        return POWERS.containsKey(id);
    }
    public static boolean containsPower(Power power) {
        return POWERS.containsValue(power);
    }
}
