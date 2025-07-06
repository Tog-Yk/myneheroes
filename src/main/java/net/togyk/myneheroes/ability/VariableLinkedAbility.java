package net.togyk.myneheroes.ability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.power.VariableLinkedPower;

import java.util.function.BiFunction;
import java.util.function.Function;

public class VariableLinkedAbility extends Ability {
    protected final BiFunction<PlayerEntity, VariableLinkedPower, Boolean> use;
    protected final BiFunction<PlayerEntity, VariableLinkedPower, Boolean> hold;
    protected final Function<VariableLinkedPower, Boolean> usable;

    public VariableLinkedAbility(Identifier id, int cooldown, Settings settings, BiFunction<PlayerEntity, VariableLinkedPower, Boolean> use, BiFunction<PlayerEntity, VariableLinkedPower, Boolean> hold, Function<VariableLinkedPower, Boolean> usable) {
        super(id, cooldown, settings, null, null);
        this.use = use;
        this.hold = hold;
        this.usable = usable;
    }
    public VariableLinkedAbility(Identifier id, int cooldown, Settings settings, BiFunction<PlayerEntity, VariableLinkedPower, Boolean> use, Function<VariableLinkedPower, Boolean> usable) {
        super(id, cooldown, settings, null, null);
        this.use = use;
        this.hold = null;
        this.usable = usable;
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
    public boolean Usable() {
        if (this.getIndirectHolder() instanceof VariableLinkedPower power) {
            if (this.usable.apply(power)) {
                return super.Usable();
            }
        }
        return false;
    }

    @Override
    public void hold(PlayerEntity player) {
        if (this.hold != null) {
            if (this.getCooldown() == 0 && this.getIndirectHolder() instanceof VariableLinkedPower power) {
                if (this.hold.apply(player, power)) {
                    this.setCooldown(this.getMaxCooldown());
                }
            }
            this.save(player.getWorld());
        }
    }

    @Override
    public boolean canHold(PlayerEntity player) {
        return this.hold != null;
    }

    @Override
    public VariableLinkedAbility copy() {
        return new VariableLinkedAbility(id, maxCooldown, settings, use, hold, usable);
    }
}
