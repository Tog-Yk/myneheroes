package net.togyk.myneheroes.power;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.AbilityUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Power {
    public final Identifier id;
    private final String name;
    protected final float damageMultiplier;
    protected final float resistance;
    public List<Ability> abilities;

    private boolean isDampened = false;
    private int color;

    private PlayerEntity holder;

    private final Identifier icon;
    private final Identifier disabledIcon;

    public Power(Identifier id, String name, float damageMultiplier, float resistance, int color, List<Ability> abilities) {
        this.id = id;
        this.name = name;
        this.damageMultiplier = damageMultiplier;
        this.resistance = resistance;
        this.color = color;
        this.abilities = abilities;
        this.icon = Identifier.of(MyneHeroes.MOD_ID,"textures/power/icon/"+name+".png");
        this.disabledIcon = Identifier.of(MyneHeroes.MOD_ID,"textures/power/icon/"+name+"_disabled.png");
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putString("id", this.id.toString());
        nbt.putBoolean("is_dampened", this.isDampened);

        NbtList abilitiesNbt = new NbtList();
        for (Ability ability : this.getAbilities()) {
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

    public String getName() {
        return name;
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

    public Identifier getIcon() {
        return icon;
    }

    public Identifier getDisabledIcon() {
        return disabledIcon;
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
        return this.id.toString() + "{\nname: "+ this.name +
                ",\ndamageMultiplier: " + this.damageMultiplier +
                ",\nresistance: " + this.resistance +
                ",\nabilities:" + this.abilities.toString();
    }

    public Power copy() {
        return new Power(this.id, String.valueOf(this.getName()), damageMultiplier, resistance, this.getColor(), List.copyOf(this.abilities));
    }
}
