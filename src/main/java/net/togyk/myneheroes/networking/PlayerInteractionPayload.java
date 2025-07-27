package net.togyk.myneheroes.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record PlayerInteractionPayload() implements CustomPayload {
    public static final Id<PlayerInteractionPayload> ID = new Id<>(ModMessages.MISSED_INTERACTION_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, PlayerInteractionPayload> CODEC = PacketCodec.unit(new PlayerInteractionPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
