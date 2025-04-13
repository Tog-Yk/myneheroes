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
    protected final Settings settings;
    public List<Ability> abilities;

    private boolean isDampened = false;
    private int color;

    private PlayerEntity holder;

    private final Identifier background;
    private final Identifier disabledBackground;

    public Power(Identifier id, int color, List<Ability> abilities, Settings settings) {
        this.id = id;
        this.color = color;
        this.abilities = abilities;
        this.background = Identifier.of(id.getNamespace(),"textures/power/"+id.getPath()+"_background.png");
        this.disabledBackground = Identifier.of(id.getNamespace(),"textures/power/"+id.getPath()+"_background_disabled.png");

        this.settings = settings;
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

    public double getDamageMultiplier() {
        return this.settings.damageMultiplier;
    }

    public double getResistance() {
        return this.settings.resistance;
    }

    public boolean allowFlying(PlayerEntity player) {
        return settings.canFly;
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

    public static class Settings{
        public double damageMultiplier = 1.00;
        public double resistance = 1.00;

        public boolean canFly = false;
        public double flyingUnlocksAt = 0.05;

        public double textureInterval = 1.00;

        public Settings() {
        }

        public Power.Settings damageMultiplier(double dbl) {
            this.damageMultiplier = dbl;
            return this;
        }

        public Power.Settings resistance(double dbl) {
            this.resistance = dbl;
            return this;
        }

        public Power.Settings canFly() {
            this.canFly = true;
            return this;
        }

        public Power.Settings flyingUnlocksAt(double dbl) {
            this.canFly = true;
            this.flyingUnlocksAt = dbl;
            return this;
        }

        public Power.Settings textureInterval(double dbl) {
            this.textureInterval = dbl;
            return this;
        }
    }

    @Override
    public String toString() {
        return this.id.toString() + "{\ndamageMultiplier: " + this.getDamageMultiplier() +
                ",\nresistance: " + this.getResistance() +
                ",\nabilities:" + this.abilities.toString();
    }

    public Power copy() {
        return new Power(this.id, this.getColor(), List.copyOf(this.abilities), settings);
    }
}
