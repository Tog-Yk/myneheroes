package net.togyk.myneheroes.ability;

import net.minecraft.util.Identifier;
import net.togyk.myneheroes.power.VariableLinkedPower;

public class VariableLinkedActivationTimedAbility extends ActivationTimedAbility {
    private final String variableName;

    public VariableLinkedActivationTimedAbility(Identifier id, String variableName, int cooldown, int maxActivatedTime, Settings settings) {
        super(id, cooldown, maxActivatedTime, settings);
        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }

    @Override
    public void setActivated(boolean activated) {
        super.setActivated(activated);
        if (this.getIndirectHolder() instanceof VariableLinkedPower power) {
            if (power.canSet(this.getVariableName(), activated)) {
                power.set(this.getVariableName(), activated);
            }
        }
    }

    @Override
    public VariableLinkedActivationTimedAbility copy() {
        return new VariableLinkedActivationTimedAbility(id, variableName, maxCooldown, maxActivatedTime, settings);
    }
}
