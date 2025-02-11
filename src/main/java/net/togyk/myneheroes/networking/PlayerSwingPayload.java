package net.togyk.myneheroes.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record PlayerSwingPayload() implements CustomPayload {
    public static final Id<PlayerSwingPayload> ID = new Id<>(ModMessages.MISSED_SWING_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, PlayerSwingPayload> CODEC = PacketCodec.unit(new PlayerSwingPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
