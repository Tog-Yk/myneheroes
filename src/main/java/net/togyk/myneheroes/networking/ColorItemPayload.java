package net.togyk.myneheroes.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record ColorItemPayload(BlockPos pos, int index, int color) implements CustomPayload {
    public static final Id<ColorItemPayload> ID = new Id<>(ModMessages.COLOR_ITEM_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, ColorItemPayload> CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC, ColorItemPayload::pos,
            PacketCodecs.INTEGER, ColorItemPayload::index,
            PacketCodecs.INTEGER, ColorItemPayload::color,
            ColorItemPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
