package net.togyk.myneheroes.ability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.power.VariableLinkedPower;

import java.util.function.BiFunction;

public class VariableLinkedAbility extends Ability {
    protected final String variableName;
    protected final BiFunction<PlayerEntity, VariableLinkedPower, Boolean> use;

    public VariableLinkedAbility(Identifier id, String variableName, int cooldown, Settings settings, BiFunction<PlayerEntity, VariableLinkedPower, Boolean> use) {
        super(id, cooldown, settings, null);
        this.variableName = variableName;
        this.use = use;
    }

    public String getVariableName() {
        return variableName;
    }

    @Override
    public void Use(PlayerEntity player) {
        if (this.getCooldown() == 0 && this.getIndirectHolder() instanceof VariableLinkedPower power) {
            if (this.use.apply(player, power)) {
                this.setCooldown(this.getMaxCooldown());
            }
        }
        this.save(player.getWorld());
    }

    @Override
    public VariableLinkedAbility copy() {
        return new VariableLinkedAbility(id, variableName, maxCooldown, settings, use);
    }
}
