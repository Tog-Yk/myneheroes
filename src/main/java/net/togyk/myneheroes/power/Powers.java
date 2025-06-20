package net.togyk.myneheroes.power;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Abilities;
import net.togyk.myneheroes.power.detailed.KryptonianPower;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Powers {
    private static final Map<Identifier,Power> POWERS = new HashMap<>();

    public static Power KRYPTONIAN = registerPower(new KryptonianPower(Identifier.of(MyneHeroes.MOD_ID, "kryptonian"), 10000, 0xFF9CDB94, List.of(Abilities.FROST_BREATH.copy(), Abilities.LASER_EYES.copy()),
            new Power.Settings().damageMultiplier(5.00).armor(10.0F).textureInterval(0.25).flyingUnlocksAt(0.05).dampenedByKryptonite(),
            new Power.attributeModifiers()
                    .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, Identifier.of(MyneHeroes.MOD_ID, "kryptonian.speed"), EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, () -> 0.6D)
                    .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, Identifier.of(MyneHeroes.MOD_ID, "kryptonian.health"), EntityAttributeModifier.Operation.ADD_VALUE, () -> 20.0D)
    ));

    public static Power SPEEDSTER = registerPower(new SpeedsterPower(Identifier.of(MyneHeroes.MOD_ID, "speedster"), 0xFFFFDE3A, List.of(Abilities.TOGGLE_SPEED.copy(), Abilities.SPEED_UP.copy(), Abilities.SPEED_DOWN.copy()), 15,
            new Power.Settings().damageMultiplier(1.10),
            new Power.attributeModifiers()
                    .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, Identifier.of(MyneHeroes.MOD_ID, "speedster.health"), EntityAttributeModifier.Operation.ADD_VALUE, () -> 2.0D)
    ));

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

    public static Set<Identifier> getIds() {
        return POWERS.keySet();
    }
}
