package net.togyk.myneheroes.power;

import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Abilities;
import net.togyk.myneheroes.power.detailed.KryptonianPower;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Powers {
    private static final Map<Identifier,Power> POWERS = new HashMap<>();

    public static Power KRYPTONIAN = registerPower(new KryptonianPower(Identifier.of(MyneHeroes.MOD_ID, "kryptonian"), 10000, 0xFF9CDB94, List.of(Abilities.FROST_BREATH.copy(), Abilities.LAZAR_EYES.copy()),
            new Power.Settings().damageMultiplier(5.00).resistance(0.3F).textureInterval(0.25).flyingUnlocksAt(0.05)));

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
