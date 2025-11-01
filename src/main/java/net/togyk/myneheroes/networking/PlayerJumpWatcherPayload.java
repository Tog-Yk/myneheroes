package net.togyk.myneheroes.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record PlayerJumpWatcherPayload(boolean jumping) implements CustomPayload {
    public static final Id<PlayerJumpWatcherPayload> ID = new Id<>(ModMessages.PLAYER_JUMPING_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, PlayerJumpWatcherPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, PlayerJumpWatcherPayload::jumping,
            PlayerJumpWatcherPayload::new);


    @Override
    public Id<PlayerJumpWatcherPayload> getId() {
        return ID;
    }
}
