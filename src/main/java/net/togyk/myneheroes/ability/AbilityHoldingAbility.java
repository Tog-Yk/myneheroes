package net.togyk.myneheroes.ability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.util.AbilityUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class AbilityHoldingAbility extends Ability {
    protected List<Ability> abilities;

    public AbilityHoldingAbility(Identifier id, int cooldown, Settings settings, List<Ability> abilities, Function<PlayerEntity, Boolean> use, Function<PlayerEntity, Boolean> hold) {
        super(id, cooldown, settings, use, hold);
        this.abilities = abilities.stream().map(Ability::copy).toList();
    }

    public AbilityHoldingAbility(Identifier id, int cooldown, Settings settings, List<Ability> abilities, Function<PlayerEntity, Boolean> use) {
        super(id, cooldown, settings, use);
        this.abilities = abilities.stream().map(Ability::copy).toList();
    }

    public void saveAbility(Ability ability) {
        this.save();
    }

    @Override
    public void tick(PlayerEntity player) {
        super.tick(player);
        for (Ability ability : abilities) {
            ability.setHolder(this);
            ability.tick(player);
        }
    }

    @Override
    public void setHolder(@Nullable AbilityHoldingAbility holder) {
        throw new UnsupportedOperationException();
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList abilitiesNbt = new NbtList();
        for (Ability ability : this.abilities) {
            if (ability != null) {
                abilitiesNbt.add(ability.writeNbt(new NbtCompound()));
            }
        }

        nbt.put("abilities", abilitiesNbt);
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        NbtList abilitiesNbt = new NbtList();
        if (nbt.contains("abilities")) {
            abilitiesNbt = nbt.getList("abilities", NbtElement.COMPOUND_TYPE);
        }

        List<Ability> abilitiesList = new ArrayList<>();
        for (NbtElement nbtElement : abilitiesNbt) {
            if (nbtElement instanceof NbtCompound nbtCompound) {
                Ability ability = AbilityUtil.nbtToAbility(nbtCompound);
                abilitiesList.add(ability);
            }
        }
        this.abilities = abilitiesList;
    }
}
