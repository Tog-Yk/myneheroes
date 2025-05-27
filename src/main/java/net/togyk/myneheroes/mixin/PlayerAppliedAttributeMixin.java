package net.togyk.myneheroes.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.togyk.myneheroes.ability.*;
import net.togyk.myneheroes.damage.ModDamageTypes;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.PlayerAbilities;
import net.togyk.myneheroes.util.PowerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerAppliedAttributeMixin {

    @ModifyVariable(
            method = "attack",
            at = @At("STORE"),
            ordinal = 0
    )
    private DamageSource modifyAttackSource(DamageSource source) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        // Check if the source is a player
        if (player != null) {
            // Multiply the damage taken
            double multiplier = PowerData.getDamageMultiplier(player); // Your custom multiplier
            if (multiplier != 1.0F) {
                return ModDamageTypes.of(player.getWorld(), ModDamageTypes.POWERFUL_PUNCH_TYPE_KEY, player);
            }
        }
        return source;
    }

    @ModifyVariable(
            method = "attack",
            at = @At("STORE"),
            ordinal = 3
    )
    private float modifyAttackDamage(float i) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        // Check if the source is a player
        if (player != null) {
            // Multiply the damage taken
            double multiplier = PowerData.getDamageMultiplier(player); // Your custom multiplier
            if (multiplier != 1.00) {
                return (float) (i * multiplier);
            }
        }
        return i;
    }

    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    private void PassiveAbilityOnHitUpdater(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        List<Ability> abilities = ((PlayerAbilities) player).getAbilities();

        boolean shouldCancel = false;
        for (Ability ability : abilities) {
            if (ability instanceof PassiveAbility passiveAbility && !passiveAbility.onGotHit(player, source, amount)) {
                shouldCancel = true;
            }
        }

        if (shouldCancel) {
            cir.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "attack", cancellable = true)
    private void PassiveAbilityAttackUpdater(Entity target, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        List<Ability> abilities = ((PlayerAbilities) player).getAbilities();

        boolean shouldCancel = false;
        for (Ability ability : abilities) {
            if (ability instanceof PassiveAbility passiveAbility && !passiveAbility.onDamage(player, target)) {
                shouldCancel = true;
            }
        }

        if (shouldCancel) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "onDeath", cancellable = true)
    private void PassiveAbilityDeathUpdater(DamageSource damageSource, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        List<Ability> abilities = ((PlayerAbilities) player).getAbilities();

        boolean shouldCancel = false;
        for (Ability ability : abilities) {
            if (ability instanceof PassiveAbility passiveAbility && !passiveAbility.onDeath(player, damageSource)) {
                shouldCancel = true;
            }
        }

        if (shouldCancel) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (mayFly(player)) {
            player.getAbilities().allowFlying = true;
        } else if (!(player.isInCreativeMode() || player.isSpectator())) {
            player.getAbilities().allowFlying = false;
            player.getAbilities().flying = false;
        }
    }

    private boolean mayFly(PlayerEntity player) {
        for (Power power: PowerData.getPowers(player)) {
            if (power.allowFlying(player)) {
                return true;
            }
        }
        for (Ability ability: ((PlayerAbilities) player).getAbilities()) {
            if (ability.getId() == Abilities.ALLOW_FLYING.getId() && ability instanceof BooleanAbility booleanAbility && booleanAbility.get()) {
                return true;
            }
        }
        return false;
    }
}