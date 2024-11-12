package net.togyk.myneheroes.power;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Power {

    public boolean enabled = true;
    private int resistance;
    private int strength;

    // Unique UUIDs for attribute modifiers
    private static final Identifier Resistance_Identifier = Identifier.of(MyneHeroes.MOD_ID, "power_resistance_boost");
    private static final Identifier Strength_Identifier = Identifier.of(MyneHeroes.MOD_ID, "power_strength_boost");

    public Power(int resistance, int strength) {
        this.resistance = resistance;
        this.strength = strength;
    }

    // Save power data to a NBT
    public void saveToNbt(NbtCompound nbt) {
        nbt.putInt("resistanceBoost", resistance);
        nbt.putInt("strengthBoost", strength);
        nbt.putBoolean("enabled", enabled);
    }

    // Load power data from a NBT
    public void loadFromNbt(NbtCompound nbt) {
        this.resistance = nbt.getInt("resistanceBoost");
        this.strength = nbt.getInt("strengthBoost");
        this.enabled =  nbt.getBoolean("enabled");
    }

    public void tick(@NotNull ServerPlayerEntity player) {
        if (this.enabled) {
            applyResistance(player);
            applyStrengthBoost(player);
        } else {
            removeResistance(player);
            removeStrengthBoost(player);
        }
    }

    // Apply the resistance boost to a player
    private void applyResistance(@NotNull ServerPlayerEntity player) {
        var attribute = player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR);
        if (attribute != null && resistance > 0) {
            // Check if the modifier is already applied; if not, add it
            if (attribute.getModifier(Resistance_Identifier) == null) {
                attribute.addPersistentModifier(new EntityAttributeModifier(
                        Resistance_Identifier, resistance, EntityAttributeModifier.Operation.ADD_VALUE));
            }
        }
    }

    // Apply the strength boost to a player
    private void applyStrengthBoost(@NotNull ServerPlayerEntity player) {
        var attribute = player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        if (attribute != null && strength > 0) {
            // Check if the modifier is already applied; if not, add it
            if (attribute.getModifier(Strength_Identifier) == null) {
                attribute.addPersistentModifier(new EntityAttributeModifier(
                        Strength_Identifier, strength, EntityAttributeModifier.Operation.ADD_VALUE));
            }
        }
    }

    // Remove resistance boost from a player
    private void removeResistance(@NotNull ServerPlayerEntity player) {
        var attribute = player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR);
        if (attribute != null) {
            attribute.removeModifier(Resistance_Identifier);
        }
    }

    // Remove strength boost from a player
    private void removeStrengthBoost(@NotNull ServerPlayerEntity player) {
        var attribute = player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        if (attribute != null) {
            attribute.removeModifier(Strength_Identifier);
        }
    }

    public Boolean IsEnabled() {
        return enabled;
    }
    public void SwitchState(@Nullable Boolean state) {
        if (state == null) {
            this.enabled = !enabled;
        } else {
            this.enabled = state;
        }
    }
}
