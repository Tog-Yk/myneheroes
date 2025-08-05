package net.togyk.myneheroes.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record AbilityKeybindPayload(int abilityIndex, int type) implements CustomPayload {
    public static final CustomPayload.Id<AbilityKeybindPayload> ID = new CustomPayload.Id<>(ModMessages.KEYBIND_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, AbilityKeybindPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, AbilityKeybindPayload::abilityIndex,
            //0: use
            //1: press
            //2: release
            PacketCodecs.INTEGER, AbilityKeybindPayload::type,
            AbilityKeybindPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
