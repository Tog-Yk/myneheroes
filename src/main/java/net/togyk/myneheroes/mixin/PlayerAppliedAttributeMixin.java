package net.togyk.myneheroes.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.togyk.myneheroes.damage.ModDamageTypes;
import net.togyk.myneheroes.util.PowerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntity.class)
public abstract class PlayerAppliedAttributeMixin {
    @ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0)
    private float modifyDamageAfterApply(float amount) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        // Check if the source is a player
        if (player != null) {
            // Multiply the damage taken
            float multiplier = PowerData.getResistance(player); // Your custom multiplier
            if (multiplier != 1.0F) {
                return amount * multiplier;
            }
        }
        return amount;
    }
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
            float multiplier = PowerData.getDamageMultiplier(player); // Your custom multiplier
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
            float multiplier = PowerData.getDamageMultiplier(player); // Your custom multiplier
            if (multiplier != 1.0F) {
                return i * multiplier;
            }
        }
        return i;
    }
}