package net.togyk.myneheroes.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;

public record SwapFromToolbeltPayload(int toolbeltIndex, int index) implements CustomPayload {
    public static final Id<SwapFromToolbeltPayload> ID = new Id<>(Identifier.of(MyneHeroes.MOD_ID, "swap_from_toolbelt"));
    public static final PacketCodec<RegistryByteBuf, SwapFromToolbeltPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, SwapFromToolbeltPayload::toolbeltIndex,
            PacketCodecs.INTEGER, SwapFromToolbeltPayload::index,
            SwapFromToolbeltPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
