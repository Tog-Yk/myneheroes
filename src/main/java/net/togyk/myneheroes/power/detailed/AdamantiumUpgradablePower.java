package net.togyk.myneheroes.power.detailed;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.power.Power;

import java.util.List;

public class AdamantiumUpgradablePower extends Power {
    private boolean hasAdamantium = false;
    protected final List<Ability> baseAbilities;
    protected final List<Ability> upgradeAbilities;
    protected final double armorBonus;

    public AdamantiumUpgradablePower(Identifier id, int color, List<Ability> abilities, List<Ability> upgradeAbilities, double armorBonus, Settings settings, attributeModifiers attributeModifiers) {
        super(id, color, abilities, settings, attributeModifiers);
        this.baseAbilities = abilities.stream().map(Ability::copy).toList();
        this.upgradeAbilities = upgradeAbilities.stream().map(Ability::copy).toList();
        this.armorBonus = armorBonus;
    }

    public boolean hasAdamantium() {
        return hasAdamantium;
    }

    public void setAdamantium(boolean adamantium) {
        List<Ability> oldAbilities = this.getAbilities();
        this.hasAdamantium = adamantium;

        this.saveAbilitiesDoNotAdd(oldAbilities);
    }

    @Override
    public List<Ability> getAbilities() {
        return this.hasAdamantium ? upgradeAbilities : baseAbilities;
    }

    @Override
    public double getArmor() {
        return super.getArmor() + (this.hasAdamantium ? this.armorBonus : 0);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putBoolean("has_adamantium", this.hasAdamantium());

        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("has_adamantium")) {
            this.hasAdamantium = nbt.getBoolean("has_adamantium");
        }

        super.readNbt(nbt);
    }

    @Override
    public Power copy() {
        return new AdamantiumUpgradablePower(id, color, baseAbilities, upgradeAbilities, armorBonus, settings, attributeModifiers);
    }
}
