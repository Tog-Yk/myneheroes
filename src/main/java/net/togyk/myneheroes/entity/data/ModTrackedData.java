package net.togyk.myneheroes.entity.data;

import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.codec.PacketCodecs;
import org.joml.Vector3f;

import java.util.List;

public class ModTrackedData {
    public static final TrackedDataHandler<List<Vector3f>> VEC3F_LIST = TrackedDataHandler.create(PacketCodecs.VECTOR3F.collect(PacketCodecs.toList()));
    public static final TrackedDataHandler<List<Integer>> INTEGER_LIST = TrackedDataHandler.create(PacketCodecs.VAR_INT.collect(PacketCodecs.toList()));

    public static void register() {
        TrackedDataHandlerRegistry.register(VEC3F_LIST);
        TrackedDataHandlerRegistry.register(INTEGER_LIST);
    }
}
