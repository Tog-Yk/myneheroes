package net.togyk.myneheroes.gamerule;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import net.togyk.myneheroes.MyneHeroes;

public class ModGamerules {

    public static final GameRules.Key<GameRules.BooleanRule> DO_METEOR_SPAWN =
            GameRuleRegistry.register(
                    "doMeteorSpawning_MH",
                    GameRules.Category.SPAWNING,
                    GameRuleFactory.createBooleanRule(false)
            );

    public static final GameRules.Key<GameRules.BooleanRule> KEEP_POWERS =
            GameRuleRegistry.register(
                    "keepPowers_MH",
                    GameRules.Category.PLAYER,
                    GameRuleFactory.createBooleanRule(true)
            );

    public static final GameRules.Key<GameRules.IntRule> POWER_LIMIT =
            GameRuleRegistry.register(
                    "powerLimit_MH",
                    GameRules.Category.PLAYER,
                    GameRuleFactory.createIntRule(1)
            );

    public static final GameRules.Key<GameRules.BooleanRule> GIVE_POWERS_ABOVE_LIMIT =
            GameRuleRegistry.register(
                    "givePowersAboveLimit_MH",
                    GameRules.Category.PLAYER,
                    GameRuleFactory.createBooleanRule(false)
            );

    public static void registerModGamerules() {
        MyneHeroes.LOGGER.info("Registering Gamerules for " + MyneHeroes.MOD_ID);
    }
}
