package net.togyk.myneheroes.power;

import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.AbilityUtil;

import java.util.HashMap;
import java.util.Map;

public class Powers {
    private static final Map<Identifier,Power> POWERS = new HashMap<>();

    public static void registerPowers() {
        MyneHeroes.LOGGER.info("registering Powers for" + MyneHeroes.MOD_ID);

        registerPower(new Power(Identifier.of(MyneHeroes.MOD_ID, "kryptonian"),"kryptonian", 5.0F, 0.3F, 0xFFBB0033, AbilityUtil.getAbilitiesForPower("kryptonian")));
    }

    public static void registerPower(Power power) {
        if (!POWERS.containsKey(power.id)) {
            POWERS.put(power.id, power);
        } else {
            MyneHeroes.LOGGER.error("There already exist an power with the id of {}",power.id);
        }
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
