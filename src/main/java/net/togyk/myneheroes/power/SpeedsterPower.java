package net.togyk.myneheroes.power;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;

import java.util.List;
import java.util.Objects;

public class SpeedsterPower extends Power implements VariableLinkedPower {
    private int speedLevel = 0;
    private int maxSpeedLevel;

    private boolean speedActive = true;

    public SpeedsterPower(Identifier id, int color, List<Ability> abilities, int maxSpeedLevel, Settings settings, attributeModifiers attributeModifiers) {
        super(id, color, abilities, settings, attributeModifiers);
        this.maxSpeedLevel = maxSpeedLevel;
    }

    private Double getStepHeight() {
        if (isSpeedActive() && getSpeedLevel() >= 5) {
            return 1.0D;
        }
        return 0.0D;
    }

    private Double getSpeed() {
        if (isSpeedActive()) {
            return 0.5D * getSpeedLevel();
        }
        return 0.0D;
    }

    private Double getSafeFallDistance() {
        if (isSpeedActive()) {
            return 0.1D * getSpeedLevel();
        }
        return 0.0D;
    }

    private Double getFallDamageMultiplier() {
        if (isSpeedActive()) {
            return 1 - 0.08D * getSpeedLevel();
        }
        return 1.0D;
    }

    public int getMaxSpeedLevel() {
        return maxSpeedLevel;
    }

    public int getSpeedLevel() {
        return speedLevel;
    }

    public void setSpeedLevel(int level) {
        this.speedLevel = Math.max(0, Math.min(maxSpeedLevel, level));
    }

    public boolean isSpeedActive() {
        return speedActive;
    }

    public void setSpeedActive(boolean active) {
        speedActive = active;
    }

    @Override
    public void setInt(String name, int integer) {
        if (name.equals("speedLevel")) {
            setSpeedLevel(integer);
        }
    }

    @Override
    public int getInt(String name) {
        if (name.equals("speedLevel")) {
            return getSpeedLevel();
        }
        return 0;
    }

    @Override
    public void setBoolean(String name, boolean bool) {
        if (name.equals("speedActive")) {
            setSpeedActive(bool);
        }
    }

    @Override
    public boolean getBoolean(String name) {
        if (Objects.equals(name, "speedActive")) {
            return isSpeedActive();
        }
        return false;
    }

    @Override
    public attributeModifiers getAttributeModifiers() {
        return super.getAttributeModifiers()
                .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, Identifier.of(MyneHeroes.MOD_ID, "speedster_power.speed"), EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, this::getSpeed)
                .addAttributeModifier(EntityAttributes.PLAYER_BLOCK_BREAK_SPEED, Identifier.of(MyneHeroes.MOD_ID, "speedster_power.block_break_speed"), EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, this::getSpeed)
                .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, Identifier.of(MyneHeroes.MOD_ID, "speedster_power.attack_speed"), EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, this::getSpeed)
                .addAttributeModifier(EntityAttributes.GENERIC_STEP_HEIGHT, Identifier.of(MyneHeroes.MOD_ID, "speedster_power.step_height"), EntityAttributeModifier.Operation.ADD_VALUE, this::getStepHeight)
                .addAttributeModifier(EntityAttributes.GENERIC_FALL_DAMAGE_MULTIPLIER, Identifier.of(MyneHeroes.MOD_ID, "speedster_power.step_height"), EntityAttributeModifier.Operation.ADD_VALUE, this::getFallDamageMultiplier)
                .addAttributeModifier(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, Identifier.of(MyneHeroes.MOD_ID, "speedster_power.safe_fall_distance"), EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, this::getSafeFallDistance);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putBoolean("speedActive", speedActive);
        nbt.putInt("speedLevel", speedLevel);

        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("speedActive")) {
            speedActive = nbt.getBoolean("speedActive");
        }

        if (nbt.contains("speedLevel")) {
            speedLevel = nbt.getInt("speedLevel");
        }
    }

    @Override
    public SpeedsterPower copy() {
        return new SpeedsterPower(id, color, abilities, maxSpeedLevel, settings, attributeModifiers);
    }
}
