package net.togyk.myneheroes.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;

public record SelectPassivePayload(int abilityIndex, int index) implements CustomPayload {
    public static final Id<SelectPassivePayload> ID = new Id<>(Identifier.of(MyneHeroes.MOD_ID, "select_passive"));
    public static final PacketCodec<RegistryByteBuf, SelectPassivePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, SelectPassivePayload::abilityIndex,
            PacketCodecs.INTEGER, SelectPassivePayload::index,
            SelectPassivePayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
