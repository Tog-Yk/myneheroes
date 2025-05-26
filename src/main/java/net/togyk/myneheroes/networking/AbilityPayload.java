package net.togyk.myneheroes.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;

public record AbilityPayload(Ability ability) implements CustomPayload {
    public static final Id<AbilityPayload> ID = new Id<>(Identifier.of(MyneHeroes.MOD_ID, "ability"));
    public static final PacketCodec<RegistryByteBuf, AbilityPayload> CODEC = PacketCodec.tuple(
            Ability.PACKET_CODEC, AbilityPayload::ability,
            AbilityPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
