package net.togyk.myneheroes.ability;

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.registry.ModRegistries;

public class Abilities {
    public static final Ability SHOOT_LAZAR = register("shoot_lazar",new Ability("shoot_lazar",20));


    private static Ability register(String name, Ability ability) {
        return Registry.register(ModRegistries.ABILITY, Identifier.of(MyneHeroes.MOD_ID, name), ability);
    }

    public static void registerModItems() {
        MyneHeroes.LOGGER.info("Registering Abilities for " + MyneHeroes.MOD_ID);
    }
}
