package net.togyk.myneheroes.power;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.AbilityUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Power {
    public final Identifier id;
    protected final float damageMultiplier;
    protected final float resistance;
    public List<Ability> abilities;

    private boolean isDampened = false;
    private int color;

    private PlayerEntity holder;

    private final Identifier background;
    private final Identifier disabledBackground;

    public Power(Identifier id, float damageMultiplier, float resistance, int color, List<Ability> abilities) {
        this.id = id;
        this.damageMultiplier = damageMultiplier;
        this.resistance = resistance;
        this.color = color;
        this.abilities = abilities;
        this.background = Identifier.of(id.getNamespace(),"textures/power/"+id.getPath()+"_background.png");
        this.disabledBackground = Identifier.of(id.getNamespace(),"textures/power/"+id.getPath()+"_background_disabled.png");
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putString("id", this.id.toString());
        nbt.putBoolean("is_dampened", this.isDampened);

        NbtList abilitiesNbt = new NbtList();
        for (Ability ability : this.abilities) {
            abilitiesNbt.add(ability.writeNbt(new NbtCompound()));
        }

        nbt.put("abilities", abilitiesNbt);
        return nbt;
    }

    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("is_dampened")) {
            this.isDampened = nbt.getBoolean("is_dampened");
        }

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


    public Identifier getId() {
        return id;
    }

    public boolean isDampened() {
        return isDampened;
    }

    public void setIsDampened(boolean dampened) {
        isDampened = dampened;
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public float getResistance() {
        return resistance;
    }

    public boolean allowFlying(PlayerEntity player) {
        return false;
    }

    public int getColor() {
        return color;
    }

    public Identifier getBackground() {
        if (this.isDampened) {
            return disabledBackground;
        } else {
            return background;
        }
    }

    public void tick(PlayerEntity player) {
    }

    public void setHolder(@Nullable PlayerEntity holder) {
        this.holder = holder;
    }

    public PlayerEntity getHolder() {
        return this.holder;
    }

    public void saveAbility(Ability ability) {
        List<Identifier> identifiers = this.abilities.stream().map(Ability::getId).toList();
        Identifier id = ability.getId();
    }

    public List<Ability> getAbilities() {
        return this.abilities;
    }

    @Override
    public String toString() {
        return this.id.toString() + "{\ndamageMultiplier: " + this.damageMultiplier +
                ",\nresistance: " + this.resistance +
                ",\nabilities:" + this.abilities.toString();
    }

    public Power copy() {
        return new Power(this.id, damageMultiplier, resistance, this.getColor(), List.copyOf(this.abilities));
    }
}
