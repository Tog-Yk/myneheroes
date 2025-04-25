package net.togyk.myneheroes.gamerule;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import net.togyk.myneheroes.MyneHeroes;

public class ModGamerules {

    public static final GameRules.Key<GameRules.BooleanRule> DO_METEOR_SPAWN =
            GameRuleRegistry.register(
                    "doMeteorSpawning",
                    GameRules.Category.SPAWNING,
                    GameRuleFactory.createBooleanRule(true)
            );

    public static void registerModGamerules() {
        MyneHeroes.LOGGER.info("Registering Gamerules for " + MyneHeroes.MOD_ID);
    }
}
