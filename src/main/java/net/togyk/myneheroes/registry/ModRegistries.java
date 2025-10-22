package net.togyk.myneheroes.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.Registry;
import net.minecraft.registry.SimpleRegistry;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.upgrade.Upgrade;

public class ModRegistries {
    public static final Registry<Ability> ABILITY = new SimpleRegistry<>(ModRegistryKeys.ABILITY, Lifecycle.stable(), false);
    public static final Registry<Power> POWER = new SimpleRegistry<>(ModRegistryKeys.POWER, Lifecycle.stable(), false);
    public static final Registry<Upgrade> UPGRADE = new SimpleRegistry<>(ModRegistryKeys.UPGRADE, Lifecycle.stable(), false);
}
