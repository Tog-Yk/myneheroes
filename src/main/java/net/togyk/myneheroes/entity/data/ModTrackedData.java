package net.togyk.myneheroes.entity.data;

import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.List;
import java.util.UUID;

public class ModTrackedData {
    private static final PacketCodec<RegistryByteBuf, Vec3d> VEC3D_CODEC = PacketCodec.of(
            (vec, buf) -> { // Writer
                buf.writeDouble(vec.x);
                buf.writeDouble(vec.y);
                buf.writeDouble(vec.z);
            },
            buf -> new Vec3d( // Reader
                    buf.readDouble(),
                    buf.readDouble(),
                    buf.readDouble()
            )
    );

    public static final TrackedDataHandler<List<Vector3f>> VEC3F_LIST = TrackedDataHandler.create(PacketCodecs.VECTOR3F.collect(PacketCodecs.toList()));
    public static final TrackedDataHandler<Vec3d> VEC3D = TrackedDataHandler.create(VEC3D_CODEC);
    public static final TrackedDataHandler<List<Integer>> INTEGER_LIST = TrackedDataHandler.create(PacketCodecs.VAR_INT.collect(PacketCodecs.toList()));
    public static final TrackedDataHandler<List<UUID>> UUID_LIST = TrackedDataHandler.create(Uuids.PACKET_CODEC.collect(PacketCodecs.toList()));

    public static void register() {
        TrackedDataHandlerRegistry.register(VEC3F_LIST);
        TrackedDataHandlerRegistry.register(VEC3D);
        TrackedDataHandlerRegistry.register(INTEGER_LIST);
        TrackedDataHandlerRegistry.register(UUID_LIST);
    }
}
