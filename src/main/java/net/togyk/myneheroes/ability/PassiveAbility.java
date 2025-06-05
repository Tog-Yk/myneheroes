package net.togyk.myneheroes.ability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class PassiveAbility extends Ability {
    public PassiveAbility(Identifier id, int cooldown, Ability.Settings settings) {
        super(id, cooldown, settings, (player) -> false);
    }

    //returns if the player still needs to be damaged
    public boolean onGotHit(PlayerEntity player, DamageSource source, float amount) {
        return true;
    }

    public boolean onDeath(PlayerEntity player, DamageSource source) {
        return true;
    }

    //returns if the player still needs to attack
    public boolean onDamage(PlayerEntity player, Entity target) {
        return true;
    }

    @Override
    public Ability copy() {
        return new PassiveAbility(id, maxCooldown, settings);
    }
}
