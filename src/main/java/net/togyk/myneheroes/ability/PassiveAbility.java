package net.togyk.myneheroes.ability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.util.SimpleEventResult;

public interface PassiveAbility {

    //returns if the player still needs to be damaged
    default boolean onGotHit(PlayerEntity player, DamageSource source, float amount) {
        return true;
    }

    default boolean onDeath(PlayerEntity player, DamageSource source) {
        return true;
    }

    //returns if the player still needs to attack
    default boolean onDamage(PlayerEntity player, Entity target) {
        return true;
    }

    //returns if the player still needs to attack
    default SimpleEventResult onMissedInteraction(PlayerEntity player) {
        return SimpleEventResult.PASS;
    }

    default SimpleEventResult onMissedAttack(PlayerEntity player) {
        return SimpleEventResult.PASS;
    }
}
