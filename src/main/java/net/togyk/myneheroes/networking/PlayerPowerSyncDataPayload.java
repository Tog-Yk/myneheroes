package net.togyk.myneheroes.networking;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record PlayerPowerSyncDataPayload(NbtCompound nbt, UUID playerUuid) implements CustomPayload {
    public static final CustomPayload.Id<PlayerPowerSyncDataPayload> ID = new CustomPayload.Id<>(ModMessages.PLAYER_POWER_SYNC_DATA_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, PlayerPowerSyncDataPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.NBT_COMPOUND, PlayerPowerSyncDataPayload::nbt,
            Uuids.PACKET_CODEC, PlayerPowerSyncDataPayload::playerUuid,
            PlayerPowerSyncDataPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
