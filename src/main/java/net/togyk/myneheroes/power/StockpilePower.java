package net.togyk.myneheroes.power;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.StockpileLinkedAbility;

import java.util.ArrayList;
import java.util.List;

public class StockpilePower extends Power {
    private int charge = 0;
    private final int maxCharge;

    private final Identifier chargeIcon;

    public StockpilePower(Identifier id, int maxCharge, int color, List<Ability> abilities, Settings settings, attributeModifiers attributeModifiers) {
        super(id, color, abilities, settings, attributeModifiers);
        this.maxCharge = maxCharge;
        this.chargeIcon = Identifier.of(id.getNamespace(),"textures/power/charge/"+id.getPath()+".png");;
    }

    @Override
    public void tick(PlayerEntity player) {
        super.tick(player);
        if (this.getMaxCharge() != this.getCharge() && !this.isDampened()) {
            setCharge(getCharge() + 1);
        }
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = Math.max(Math.min(charge, this.getMaxCharge()), 0);
    }

    public int getMaxCharge() {
        return maxCharge;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("charge", this.getCharge());
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("charge")) {
            this.setCharge(nbt.getInt("charge"));
        }
    }

    @Override
    public double getDamageMultiplier() {
        return super.getDamageMultiplier() * (float) (Math.sqrt((double) this.getCharge() /this.getMaxCharge()));
    }

    @Override
    public double getArmor() {
        double armor = super.getArmor() * (float) (Math.sqrt((double) this.getCharge() / this.getMaxCharge()));
        return armor;
    }

    public Identifier getChargeIcon() {
        return chargeIcon;
    }

    @Override
    public Identifier getBackground() {
        Identifier initialBG = super.getBackground();
        String path = initialBG.getPath().substring(0, initialBG.getPath().length() - ".png".length());
        return Identifier.of(initialBG.getNamespace(),  path + (int) (((double) this.getCharge() / this.getMaxCharge()) / settings.textureInterval) + ".png");
    }

    @Override
    public boolean allowFlying(PlayerEntity player) {
        return settings.canFly ? this.getCharge() >= (this.getMaxCharge() * settings.flyingUnlocksAt) : false;
    }

    @Override
    public StockpilePower copy() {
        return new StockpilePower(this.id, this.getMaxCharge(), this.getColor(), List.copyOf(this.abilities), settings, attributeModifiers);
    }

    @Override
    public List<Ability> getAbilities() {
        List<Ability> abilities = new ArrayList<>();
        for (Ability ability: this.abilities) {
            if (ability instanceof StockpileLinkedAbility linkedAbility) {
                if (this.getCharge() >= linkedAbility.getUnlocksAt()) {
                    abilities.add(ability);
                }
            } else {
                abilities.add(ability);
            }
        }
        return abilities;
    }
}
