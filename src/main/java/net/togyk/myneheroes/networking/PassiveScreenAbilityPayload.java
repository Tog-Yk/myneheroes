package net.togyk.myneheroes.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;

public record PassiveScreenAbilityPayload(Ability ability) implements CustomPayload {
    public static final Id<PassiveScreenAbilityPayload> ID = new Id<>(Identifier.of(MyneHeroes.MOD_ID, "passive_screen"));
    public static final PacketCodec<RegistryByteBuf, PassiveScreenAbilityPayload> CODEC = PacketCodec.tuple(
            Ability.PACKET_CODEC, PassiveScreenAbilityPayload::ability,
            PassiveScreenAbilityPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
