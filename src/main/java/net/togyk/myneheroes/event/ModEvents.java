package net.togyk.myneheroes.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.Powers;
import net.togyk.myneheroes.util.PlayerPowers;

import java.util.*;

import static java.lang.Math.max;

public class ModEvents {

    public static void registerEvents() {
        MyneHeroes.LOGGER.info("registering Events for" + MyneHeroes.MOD_ID);

        Map<ServerPlayerEntity, Long> timeInNether = new HashMap<>();
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerWorld world : server.getWorlds()) {
                if (world.getRegistryKey().equals(ServerWorld.NETHER)) {

                    for (ServerPlayerEntity player : world.getPlayers()) {
                        timeInNether.put(player, timeInNether.getOrDefault(player, 0L) + 1);

                        if (timeInNether.get(player) >= 2 * 60 * 20L) {
                            Power kryptonianPower = Powers.get(Identifier.of(MyneHeroes.MOD_ID, "kryptonian"));
                            if (kryptonianPower != null) {
                                ((PlayerPowers) player).addPower(kryptonianPower);
                                MyneHeroes.LOGGER.info("congrats on becoming a kryptonian");
                            }
                            timeInNether.put(player,0L);
                        }
                    }
                } else {
                    for (ServerPlayerEntity player : world.getPlayers()) {
                        timeInNether.put(player, max(timeInNether.getOrDefault(player, 0L) - 1, 0L));
                    }
                }
            }

            // count down if players are no longer in the Nether
            timeInNether.keySet().removeIf(player -> player.getWorld() != server.getWorld(ServerWorld.NETHER));
        });
    }
}
