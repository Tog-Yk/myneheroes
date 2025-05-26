package net.togyk.myneheroes.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;

public record ToolBeltScreenAbilityPayload(Ability ability) implements CustomPayload {
    public static final Id<ToolBeltScreenAbilityPayload> ID = new Id<>(Identifier.of(MyneHeroes.MOD_ID, "toolbelt_screen"));
    public static final PacketCodec<RegistryByteBuf, ToolBeltScreenAbilityPayload> CODEC = PacketCodec.tuple(
            Ability.PACKET_CODEC, ToolBeltScreenAbilityPayload::ability,
            ToolBeltScreenAbilityPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
