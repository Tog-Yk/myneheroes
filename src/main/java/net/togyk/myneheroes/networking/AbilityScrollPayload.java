package net.togyk.myneheroes.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record AbilityScrollPayload(int abilityIndex, double mouseX, double mouseY, double vScroll, double hScroll) implements CustomPayload {
    public static final Id<AbilityScrollPayload> ID = new Id<>(ModMessages.ABILITY_SCROLL_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, AbilityScrollPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, AbilityScrollPayload::abilityIndex,
            PacketCodecs.DOUBLE, AbilityScrollPayload::mouseX,
            PacketCodecs.DOUBLE, AbilityScrollPayload::mouseY,
            PacketCodecs.DOUBLE, AbilityScrollPayload::vScroll,
            PacketCodecs.DOUBLE, AbilityScrollPayload::hScroll,
            AbilityScrollPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
