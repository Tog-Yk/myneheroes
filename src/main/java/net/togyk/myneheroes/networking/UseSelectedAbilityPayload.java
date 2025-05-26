package net.togyk.myneheroes.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;

public record UseSelectedAbilityPayload(int abilityIndex, int index) implements CustomPayload {
    public static final Id<UseSelectedAbilityPayload> ID = new Id<>(Identifier.of(MyneHeroes.MOD_ID, "use_selected_ability"));
    public static final PacketCodec<RegistryByteBuf, UseSelectedAbilityPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, UseSelectedAbilityPayload::abilityIndex,
            PacketCodecs.INTEGER, UseSelectedAbilityPayload::index,
            UseSelectedAbilityPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
