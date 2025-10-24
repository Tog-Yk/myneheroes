package net.togyk.myneheroes.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.upgrade.Upgrade;

public class ModRegistries {
    public static final Registry<Ability> ABILITY = FabricRegistryBuilder.createSimple(ModRegistryKeys.ABILITY).buildAndRegister();
    public static final Registry<Power> POWER = FabricRegistryBuilder.createSimple(ModRegistryKeys.POWER).buildAndRegister();
    public static final Registry<Upgrade> UPGRADE = FabricRegistryBuilder.createSimple(ModRegistryKeys.UPGRADE).buildAndRegister();
}
