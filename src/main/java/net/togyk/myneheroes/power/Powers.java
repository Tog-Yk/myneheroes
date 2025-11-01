package net.togyk.myneheroes.power;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Abilities;
import net.togyk.myneheroes.power.detailed.KryptonianPower;
import net.togyk.myneheroes.registry.ModRegistries;

import java.util.List;
import java.util.Set;

public class Powers {
    public static void registerPowers() {
        MyneHeroes.LOGGER.info("Registering powers");
    }

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
    public static Power SPIDER_ORGANIC_WEBBING = registerPower(new Power(Identifier.of(MyneHeroes.MOD_ID, "spider_organic_webbing"), 0xFFFF3A3A, List.of(Abilities.SPIDER_SENSE, Abilities.ORGANIC_WEBBING),
            new Power.Settings().damageMultiplier(3.50).armor(3).canWallCrawl(),
            new Power.attributeModifiers().addAttributeModifier(EntityAttributes.GENERIC_FALL_DAMAGE_MULTIPLIER, Identifier.of(MyneHeroes.MOD_ID, "spider_organic_webbing.fall_damage"), EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, () -> -1.0D)
    ));

    public static Power MUTANT_REGENERATION = registerPower(new Power(Identifier.of(MyneHeroes.MOD_ID, "mutant_regeneration"), 0xFFFF3A3A, List.of(Abilities.RAGE, Abilities.BONE_CLAWS, Abilities.ADAMANTIUM_CLAWS, Abilities.IMMORTALITY),
            new Power.Settings().damageMultiplier(3.50).armor(3),
            new Power.attributeModifiers()
    ));

    public static <T extends Power> T registerPower(T power) {
        return Registry.register(ModRegistries.POWER, power.id, power);
    }

    public static Power get(Identifier id) {
        Power power = ModRegistries.POWER.get(id);
        if (power != null) {
            return power.copy();
        } else {
            return null;
        }
    }

    public static boolean containsId(Identifier id) {
        return ModRegistries.POWER.containsId(id);
    }

    public static Set<Identifier> getIds() {
        return ModRegistries.POWER.getIds();
    }
}
