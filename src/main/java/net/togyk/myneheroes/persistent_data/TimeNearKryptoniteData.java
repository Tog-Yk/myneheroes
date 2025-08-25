package net.togyk.myneheroes.persistent_data;

import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimeNearKryptoniteData extends PersistentState {
    private final Map<UUID, Long> timeMap = new HashMap<>();

    public TimeNearKryptoniteData() {}

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound mapNbt = new NbtCompound();
        timeMap.forEach((uuid, time) -> mapNbt.putLong(uuid.toString(), time));
        nbt.put("time_map", mapNbt);
        return nbt;
    }

    public TimeNearKryptoniteData(NbtCompound nbt) {
        NbtCompound mapNbt = nbt.getCompound("time_map");
        mapNbt.getKeys().forEach(key -> {
            timeMap.put(UUID.fromString(key), mapNbt.getLong(key));
        });
    }

    public Map<UUID, Long> getTimeMap() {
        return timeMap;
    }

    public void setTime(UUID player, long time) {
        timeMap.put(player, time);
        markDirty(); // marks the data as needing saving
    }


    public static PersistentState.Type<TimeNearKryptoniteData> getPersistentStateType(ServerWorld world) {
        return new PersistentState.Type<>(TimeNearKryptoniteData::new, (nbt, registryLookup) -> new TimeNearKryptoniteData(nbt), DataFixTypes.SAVED_DATA_RAIDS);
    }
}