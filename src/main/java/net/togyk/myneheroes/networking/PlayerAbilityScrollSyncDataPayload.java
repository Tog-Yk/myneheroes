package net.togyk.myneheroes.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record PlayerAbilityScrollSyncDataPayload(int scrolledAbilities, int scrolledPowers) implements CustomPayload {
    public static final Id<PlayerAbilityScrollSyncDataPayload> ID = new Id<>(ModMessages.PLAYER_ABILITY_SCROLLED_SYNC_DATA_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, PlayerAbilityScrollSyncDataPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, PlayerAbilityScrollSyncDataPayload::scrolledAbilities,
            PacketCodecs.INTEGER, PlayerAbilityScrollSyncDataPayload::scrolledPowers,
            PlayerAbilityScrollSyncDataPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
