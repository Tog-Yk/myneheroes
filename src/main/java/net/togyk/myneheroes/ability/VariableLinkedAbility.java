package net.togyk.myneheroes.ability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.power.VariableLinkedPower;

import java.util.function.Function;

public class VariableLinkedAbility extends Ability {
    protected final String variableName;
    protected final Function<VariableLinkedPower, Object> operation;

    public VariableLinkedAbility(Identifier id, String variableName, Function<VariableLinkedPower, Object> operation, int cooldown, Settings settings) {
        super(id, cooldown, settings, player -> false);
        this.variableName = variableName;
        this.operation = operation;
    }

    public String getVariableName() {
        return this.variableName;
    }

    @Override
    public void use(PlayerEntity player) {
        if (this.getCooldown() == 0 && this.getIndirectHolder() instanceof VariableLinkedPower power) {
            Object variable = this.operation.apply(power);
            if (power.canSet(this.getVariableName(), variable)) {
                power.set(this.getVariableName(), variable);
                this.setCooldown(this.getMaxCooldown());
            }
        }
        this.save();
    }

    @Override
    public boolean Usable() {
        if (this.getIndirectHolder() instanceof VariableLinkedPower power) {
            return power.canSet(this.getVariableName(), this.operation.apply(power)) && super.Usable();
        }
        return false;
    }

    public Object getVariable() {
        if (this.getIndirectHolder() instanceof VariableLinkedPower power) {
            return power.get(this.getVariableName());
        }
        return null;
    }

    @Override
    public VariableLinkedAbility copy() {
        return new VariableLinkedAbility(id, variableName, operation, maxCooldown, settings);
    }
}
