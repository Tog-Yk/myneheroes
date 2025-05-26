package net.togyk.myneheroes.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;

public record SelectionScreenAbilityPayload(Ability ability) implements CustomPayload {
    public static final Id<SelectionScreenAbilityPayload> ID = new Id<>(Identifier.of(MyneHeroes.MOD_ID, "selection_screen"));
    public static final PacketCodec<RegistryByteBuf, SelectionScreenAbilityPayload> CODEC = PacketCodec.tuple(
            Ability.PACKET_CODEC, SelectionScreenAbilityPayload::ability,
            SelectionScreenAbilityPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
