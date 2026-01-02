package net.togyk.myneheroes.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record WorthinessPayload(boolean isWorthy, int worthiness) implements CustomPayload {
    public static final Id<WorthinessPayload> ID = new Id<>(ModMessages.WORTHINESS_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, WorthinessPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, WorthinessPayload::isWorthy,
            PacketCodecs.INTEGER, WorthinessPayload::worthiness,
            WorthinessPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
