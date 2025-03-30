package net.togyk.myneheroes.power;

import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Abilities;
import net.togyk.myneheroes.ability.AbilityUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Powers {
    private static final Map<Identifier,Power> POWERS = new HashMap<>();

    public static Power KRYPTONIAN = registerPower(new Power(Identifier.of(MyneHeroes.MOD_ID, "kryptonian"),"kryptonian", 5.0F, 0.3F, 0xFFBB0033, List.of(Abilities.LAZAR_EYES)));

    public static void registerPowers() {
        MyneHeroes.LOGGER.info("registering Powers for" + MyneHeroes.MOD_ID);
    }

    public static Power registerPower(Power power) {
        if (!POWERS.containsKey(power.id)) {
            POWERS.put(power.id, power);
            return power.copy();
        } else {
            MyneHeroes.LOGGER.error("There already exist an power with the id of {}",power.id);
        }
        return null;
    }

    public static Power get(Identifier id) {
        Power power = POWERS.getOrDefault(id, null);
        if (power != null) {
            return power.copy();
        } else {
            return null;
        }
    }

    public static boolean containsId(Identifier id) {
        return POWERS.containsKey(id);
    }
    public static boolean containsPower(Power power) {
        return POWERS.containsValue(power);
    }
}
