package net.togyk.myneheroes.ability.detailed;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.PassiveAbility;

public class ImmortalityAbility extends Ability implements PassiveAbility {
    public ImmortalityAbility(Identifier id, int cooldown, Settings settings) {
        super(id, cooldown, settings, null);
    }

    @Override
    public boolean Usable() {
        return false;
    }

    @Override
    public boolean onDeath(PlayerEntity player, DamageSource source) {
        if (this.getCooldown() == 0) {
            this.setCooldown(this.getMaxCooldown());
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 450, 1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 400, 0));
            return false;
        }
        return true;
    }

    @Override
    public Ability copy() {
        return new ImmortalityAbility(id, maxCooldown, settings);
    }
}
