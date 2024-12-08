package net.togyk.myneheroes.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record KeybindPayload(int integer /*, blockPos, myUuid*/) implements CustomPayload {
    public static final CustomPayload.Id<KeybindPayload> ID = new CustomPayload.Id<>(ModMessages.KEYBIND_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, KeybindPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, KeybindPayload::integer,
            //BlockPos.PACKET_CODEC, BlockHighlightPayload::blockPos,
            //Uuids.PACKET_CODEC, BlockHighlightPayload::myUuid,
            KeybindPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
