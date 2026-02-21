package net.togyk.myneheroes.ability.detailed;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.ability.ScrollableAbility;
import net.togyk.myneheroes.ability.VariableLinkedAbility;
import net.togyk.myneheroes.power.VariableLinkedPower;

import java.util.function.BiFunction;

public class ToggleOrConfigureVLAbility extends VariableLinkedAbility implements ScrollableAbility {
    protected final String toggleVariableName;
    protected final BiFunction<VariableLinkedPower, Double, Object> configureOperation;
    private boolean blockedDueToScrolling;

    public ToggleOrConfigureVLAbility(Identifier id, String toggleVariableName, String variableName, BiFunction<VariableLinkedPower, Double, Object> operation, int cooldown, Settings settings) {
        super(id, variableName,
                power -> {
                    if (power.get(toggleVariableName) instanceof Boolean bool) {
                        return !bool;
                    }
                    return false;
                },
        cooldown, settings);
        this.toggleVariableName = toggleVariableName;
        this.configureOperation = operation;
    }

    @Override
    public void pressed(PlayerEntity player) {
    }

    @Override
    public void released(PlayerEntity player) {
        if (!this.blockedDueToScrolling) {
            if (this.getCooldown() == 0 && this.getIndirectHolder() instanceof VariableLinkedPower power) {
                Object variable = this.operation.apply(power);
                if (power.canSet(this.getToggleVariableName(), variable)) {
                    power.set(this.getToggleVariableName(), variable);
                    this.setCooldown(this.getMaxCooldown());
                }
            }
        }
        this.blockedDueToScrolling = false;
        this.save();
    }

    @Override
    public boolean scroll(double mouseX, double mouseY, double hScroll, double vScroll) {
        if (hScroll != 0) {
            if (this.getCooldown() == 0 && this.getIndirectHolder() instanceof VariableLinkedPower power) {
                Object variable = this.configureOperation.apply(power, hScroll);
                if (power.canSet(this.getVariableName(), variable)) {
                    power.set(this.getVariableName(), variable);
                    this.setCooldown(this.getMaxCooldown());
                }
            }
            this.blockedDueToScrolling = true;
            this.save();
            return true;
        }
        return true;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putBoolean("blockedDueToScrolling", blockedDueToScrolling);
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("blockedDueToScrolling")) {
            blockedDueToScrolling = nbt.getBoolean("blockedDueToScrolling");
        }
    }

    @Override
    public boolean isUsable() {
        if (this.getIndirectHolder() instanceof VariableLinkedPower power) {
            return power.canSet(this.getToggleVariableName(), this.operation.apply(power)) && super.isUsable();
        }
        return false;
    }

    public String getToggleVariableName() {
        return toggleVariableName;
    }

    @Override
    public VariableLinkedAbility copy() {
        return new ToggleOrConfigureVLAbility(id, toggleVariableName, variableName, configureOperation, maxCooldown, settings);
    }
}
