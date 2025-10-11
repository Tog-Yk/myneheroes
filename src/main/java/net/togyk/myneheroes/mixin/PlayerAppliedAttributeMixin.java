package net.togyk.myneheroes.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.ability.Abilities;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.BooleanAbility;
import net.togyk.myneheroes.ability.PassiveAbility;
import net.togyk.myneheroes.damage.ModDamageTypes;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.PlayerAbilities;
import net.togyk.myneheroes.util.PowerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerAppliedAttributeMixin {
    @Unique
    private int myneheroes$wallClimbSoundCooldown = 0;

    @ModifyVariable(
            method = "attack",
            at = @At("STORE"),
            ordinal = 0
    )
    private DamageSource myneheroes$modifyAttackSource(DamageSource source) {
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
    private float myneheroes$modifyAttackDamage(float i) {
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
    private void myneheroes$PassiveAbilityOnHitUpdater(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        List<Ability> abilities = ((PlayerAbilities) player).myneheroes$getAbilities();

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
    private void myneheroes$PassiveAbilityAttackUpdater(Entity target, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        List<Ability> abilities = ((PlayerAbilities) player).myneheroes$getAbilities();

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

    @Inject(at = @At("HEAD"), method = "tick")
    private void myneheroes$tick(CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (myneheroes$mayFly(player)) {
            player.getAbilities().allowFlying = true;
        } else if (!(player.isInCreativeMode() || player.isSpectator())) {
            player.getAbilities().allowFlying = false;
            player.getAbilities().flying = false;
        }

        List<Power> powers = PowerData.getPowers(player);

        if (!player.isSpectator()) {
            for (Power power : powers) {
                if (power.isPhasing()) {
                    BlockPos eyePos = BlockPos.ofFloored(player.getEyePos());
                    BlockState state = player.getWorld().getBlockState(eyePos);

                    if (!state.isAir() && !state.getCollisionShape(player.getWorld(), eyePos).isEmpty()) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 21, 0, false, false, false));
                    }
                    break;
                }
            }
        }

        if (player.horizontalCollision) {
            for (Power power : powers) {
                if (power.canWallClimb()) {
                    Vec3d velocity = player.getVelocity();
                    if (player.isSneaking()) {
                        // Stick to wall vertically
                        player.setVelocity(velocity.x, Math.max(player.getVelocity().y, 0), velocity.z);
                    } else {
                        // Climb if jumping
                        double movementSpeed = player.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                        double climbSpeed = movementSpeed * 0.90;

                        player.setVelocity(velocity.x, climbSpeed, velocity.z);

                        // Play footstep sound
                        Direction direction = Direction.fromVector((int) velocity.x, 0, (int) velocity.z);

                        if (direction == null || direction == Direction.UP || direction == Direction.DOWN) {
                            direction = player.getHorizontalFacing();
                        }

                        if (myneheroes$wallClimbSoundCooldown == 0) {
                            BlockPos pos = BlockPos.ofFloored(player.getPos()).offset(direction);
                            BlockState state = player.getWorld().getBlockState(pos);
                            BlockSoundGroup sound = state.getSoundGroup();

                            player.playSound(sound.getStepSound(), sound.getVolume() * 0.75F, sound.getPitch());

                            myneheroes$wallClimbSoundCooldown = (int) (8 / (climbSpeed / 0.09));
                        } else {
                            myneheroes$wallClimbSoundCooldown -= 1;
                        }
                    }
                    player.fallDistance = 0;

                    break;
                }
            }
        }
    }

    @Unique
    private boolean myneheroes$mayFly(PlayerEntity player) {
        for (Power power: PowerData.getPowers(player)) {
            if (power.allowFlying(player)) {
                return true;
            }
        }
        for (Ability ability: ((PlayerAbilities) player).myneheroes$getAbilities()) {
            if (ability.getId() == Abilities.ALLOW_FLYING.getId() && ability instanceof BooleanAbility booleanAbility && booleanAbility.get()) {
                return true;
            }
        }
        return false;
    }
}