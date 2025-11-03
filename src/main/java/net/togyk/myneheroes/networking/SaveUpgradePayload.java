package net.togyk.myneheroes.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.togyk.myneheroes.upgrade.Upgrade;

public record SaveUpgradePayload(int slot, Upgrade upgrade) implements CustomPayload {
    public static final CustomPayload.Id<SaveUpgradePayload> ID = new CustomPayload.Id<>(ModMessages.SAVE_UPGRADE_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, SaveUpgradePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, SaveUpgradePayload::slot,
            Upgrade.PACKET_CODEC, SaveUpgradePayload::upgrade,
            SaveUpgradePayload::new);

    @Override
    public CustomPayload.Id<SaveUpgradePayload> getId() {
        return ID;
    }
}
