package net.togyk.myneheroes.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record SetHoverFlyingPayload(boolean flying) implements CustomPayload {
    public static final Id<SetHoverFlyingPayload> ID = new Id<>(ModMessages.SET_HOVER_FLYING_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, SetHoverFlyingPayload> PACKET_CODEC =
            PacketCodec.tuple(PacketCodecs.BOOL, SetHoverFlyingPayload::flying, SetHoverFlyingPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
