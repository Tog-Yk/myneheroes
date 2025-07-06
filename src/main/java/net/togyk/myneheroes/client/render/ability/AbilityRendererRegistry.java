package net.togyk.myneheroes.client.render.ability;

import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Abilities;
import net.togyk.myneheroes.ability.Ability;

import java.util.HashMap;
import java.util.Map;

public class AbilityRendererRegistry {
    private static final Map<Identifier, AbilityRenderer<?>> RENDERERS = new HashMap<>();


    private static <T extends Ability> void registerAbilityRenderer(T ability, AbilityRenderer<T> renderer) {
        if (!RENDERERS.containsKey(ability.id)) {
            RENDERERS.put(ability.id, renderer);
        } else {
            MyneHeroes.LOGGER.error("There's already a renderer linked to the ability with the id of {}", ability.id);
        }
    }

    static {
        registerAbilityRenderer(Abilities.LASER_EYES, new LaserEyesAbilityRenderer<>());
    }

    public static AbilityRenderer<?> get(Ability ability) {
        return RENDERERS.getOrDefault(ability.getId(), null);
    }
}
