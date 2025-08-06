package net.togyk.myneheroes.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Uuids;
import net.togyk.myneheroes.ability.Ability;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record PlayerAbilitySyncDataPayload(List<Ability> abilities, UUID playerUuid) implements CustomPayload {
    public static final Id<PlayerAbilitySyncDataPayload> ID = new Id<>(ModMessages.PLAYER_ABILITY_SYNC_DATA_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, PlayerAbilitySyncDataPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.collection(ArrayList::new, Ability.PACKET_CODEC), PlayerAbilitySyncDataPayload::abilities,
            Uuids.PACKET_CODEC, PlayerAbilitySyncDataPayload::playerUuid,
            PlayerAbilitySyncDataPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
