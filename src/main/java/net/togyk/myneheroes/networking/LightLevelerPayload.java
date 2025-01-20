package net.togyk.myneheroes.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record LightLevelerPayload(BlockPos pos, int index, int lightLevel) implements CustomPayload {
    public static final Id<LightLevelerPayload> ID = new Id<>(ModMessages.LIGHT_LEVELER_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, LightLevelerPayload> CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC, LightLevelerPayload::pos,
            PacketCodecs.INTEGER, LightLevelerPayload::index,
            PacketCodecs.INTEGER, LightLevelerPayload::lightLevel,
            LightLevelerPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
