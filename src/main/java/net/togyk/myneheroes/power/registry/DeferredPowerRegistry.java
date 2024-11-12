package net.togyk.myneheroes.power.registry;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.power.Power;

import java.util.LinkedHashMap;
import java.util.Map;

public class DeferredPowerRegistry {
    private static final Map<Identifier, Power> ENTRIES = new LinkedHashMap<>();
    private static boolean isRegistered = false;

    public static void register() {
        // Register each entry in the ENTRIES map
        for (Map.Entry<Identifier, Power> entry : ENTRIES.entrySet()) {
            Identifier id = entry.getKey();
            Power power = entry.getValue();
            Registry.register(PowerRegistry.POWER_KEY, id, power);
        }
    }

    public static RegistryKey<Power> registerPower(String name, Power power) {
        Identifier id = Identifier.of(MyneHeroes.MOD_ID, name);
        ENTRIES.put(id, power);
        return RegistryKey.of(PowerRegistry.POWER_KEY, id);
    }
}