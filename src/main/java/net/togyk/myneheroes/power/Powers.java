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
    public static void registerPowers() {
        MyneHeroes.LOGGER.info("Registering powers");
    }

    private static final Map<Identifier,Power> POWERS = new HashMap<>();

    public static KryptonianPower KRYPTONIAN = registerPower(new KryptonianPower(Identifier.of(MyneHeroes.MOD_ID, "kryptonian"), 10000F, 0xFF9CDB94, List.of(Abilities.FROST_BREATH, Abilities.LASER_EYES),
            new Power.Settings().damageMultiplier(5.00).armor(10.0F).textureInterval(0.25).flyingUnlocksAt(0.05).dampenedByKryptonite(),
            new Power.attributeModifiers()
                    .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, Identifier.of(MyneHeroes.MOD_ID, "kryptonian.speed"), EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, () -> 0.6D)
                    .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, Identifier.of(MyneHeroes.MOD_ID, "kryptonian.health"), EntityAttributeModifier.Operation.ADD_VALUE, () -> 20.0D)
    ));

    public static SpeedsterPower SPEEDSTER = registerPower(new SpeedsterPower(Identifier.of(MyneHeroes.MOD_ID, "speedster"), 0xFFFFDE3A, List.of(Abilities.TOGGLE_SPEED, Abilities.SPEED_UP, Abilities.SPEED_DOWN, Abilities.TOGGLE_PHASING), 15,
            new Power.Settings().damageMultiplier(1.10),
            new Power.attributeModifiers()
                    .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, Identifier.of(MyneHeroes.MOD_ID, "speedster.health"), EntityAttributeModifier.Operation.ADD_VALUE, () -> 2.0D)
    ));

    public static Power SPIDER = registerPower(new Power(Identifier.of(MyneHeroes.MOD_ID, "spider"), 0xFFFF3A3A, List.of(Abilities.SPIDER_SENSE),
            new Power.Settings().damageMultiplier(3.50).armor(3).canWallCrawl(),
            new Power.attributeModifiers().addAttributeModifier(EntityAttributes.GENERIC_FALL_DAMAGE_MULTIPLIER, Identifier.of(MyneHeroes.MOD_ID, "spider.fall_damage"), EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, () -> -1.0D)
    ));
    public static Power SPIDER_ORGANIC_WEBBING = registerPower(new Power(Identifier.of(MyneHeroes.MOD_ID, "spider_organic_webbing"), 0xFFFF3A3A, List.of(Abilities.SPIDER_SENSE, Abilities.WEB_SHOOTER),
            new Power.Settings().damageMultiplier(3.50).armor(3).canWallCrawl(),
            new Power.attributeModifiers().addAttributeModifier(EntityAttributes.GENERIC_FALL_DAMAGE_MULTIPLIER, Identifier.of(MyneHeroes.MOD_ID, "spider_organic_webbing.fall_damage"), EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, () -> -1.0D)
    ));

    public static Power WEAPON_X = registerPower(new Power(Identifier.of(MyneHeroes.MOD_ID, "weapon-x"), 0xFFFF3A3A, List.of(Abilities.RAGE, Abilities.BONE_CLAWS, Abilities.ADAMANTIUM_CLAWS),
            new Power.Settings().damageMultiplier(3.50).armor(3),
            new Power.attributeModifiers()
    ));

    public static <T extends Power> T registerPower(T power) {
        if (!POWERS.containsKey(power.id)) {
            POWERS.put(power.id, power);
            return power;
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
