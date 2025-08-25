package net.togyk.myneheroes.persistent_data;

import net.minecraft.server.world.ServerWorld;

public class ModPersistentData {
    private static final String TIME_NEAR_KRYPTONITE_KEY = "time_near_kryptonite_data";

    public static TimeNearKryptoniteData getTimeNearKryptonite(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(
                TimeNearKryptoniteData.getPersistentStateType(world),
                TIME_NEAR_KRYPTONITE_KEY
        );
    }
}
