package net.togyk.myneheroes.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;

public class ModEffects {
    public static final RegistryEntry<StatusEffect> KRYPTONITE_POISON = registryEffect("kryptonite_poison",
            new KryptonitePoisonEffect(StatusEffectCategory.HARMFUL, 0xFF9CDB94));

    public static RegistryEntry<StatusEffect> registryEffect(String name, StatusEffect effect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(MyneHeroes.MOD_ID, name), effect);
    }

    public static void registerEffects() {
        MyneHeroes.LOGGER.info("Registering Effects for " + MyneHeroes.MOD_ID);
    }
}
