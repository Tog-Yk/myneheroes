package net.togyk.myneheroes.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Abilities;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.BooleanAbility;
import net.togyk.myneheroes.ability.PassiveAbility;
import net.togyk.myneheroes.damage.ModDamageTypes;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.PlayerAbilities;
import net.togyk.myneheroes.util.PlayerHoverFlightControl;
import net.togyk.myneheroes.util.PowerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerAppliedAttributeMixin implements PlayerHoverFlightControl {
    @Unique
    private int myneheroes$wallClimbSoundCooldown = 0;
    @Unique
    private boolean myneheroes$isHoverFlying = false;
    @Unique
    private int myneheroes$flightTime = 0;
    @Unique
    private int myneheroes$hoverTimeRight = 0;
    @Unique
    private int myneheroes$hoverTimeLeft = 0;
    @Unique
    private int myneheroes$hoverTimeBack = 0;
    @Unique
    private boolean isMyneheroes$isHoveringRight = true;

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

        Vec3d relativeVelocity = this.getVelocityRelativeToYaw();
        if (myneheroes$isHoverFlying()) {
            if (relativeVelocity.x < 0 || (relativeVelocity.x == 0 && isMyneheroes$isHoveringRight)) {
                if (myneheroes$hoverTimeRight < 15) {
                    myneheroes$hoverTimeRight++;
                }
                if (myneheroes$hoverTimeLeft > 0) {
                    myneheroes$hoverTimeLeft--;
                }
                isMyneheroes$isHoveringRight = true;
            } else {
                if (myneheroes$hoverTimeLeft < 15) {
                    myneheroes$hoverTimeLeft++;
                }
                if (myneheroes$hoverTimeRight > 0) {
                    myneheroes$hoverTimeRight--;
                }
                isMyneheroes$isHoveringRight = false;
            }
            if (relativeVelocity.z < 0) {
                if (myneheroes$hoverTimeBack < 15) {
                    myneheroes$hoverTimeBack++;
                }
            } else if (myneheroes$hoverTimeBack > 0) {
                myneheroes$hoverTimeBack--;
            }

            if (player.isSprinting()) {
                if (myneheroes$flightTime < 15) {
                    myneheroes$flightTime++;
                }
            } else if (myneheroes$flightTime > 0) {
                myneheroes$flightTime--;
            }
        } else {
            if (myneheroes$hoverTimeRight > 0) {
                myneheroes$hoverTimeRight--;
            }
            if (myneheroes$hoverTimeLeft > 0) {
                myneheroes$hoverTimeLeft--;
            }
            if (myneheroes$hoverTimeBack > 0) {
                myneheroes$hoverTimeBack--;
            }
            if (myneheroes$flightTime > 0) {
                myneheroes$flightTime = 0;
            }
            isMyneheroes$isHoveringRight = false;
        }
    }

    @Inject(at = @At("HEAD"), method = "readCustomDataFromNbt")
    private void readFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains(MyneHeroes.MOD_ID)) {
            NbtCompound modNbt = nbt.getCompound(MyneHeroes.MOD_ID);
            if (modNbt.contains("flight_time")) {
                this.myneheroes$flightTime = modNbt.getInt("scrolled_ability_offset");
            }
            if (modNbt.contains("hover_time_right")) {
                this.myneheroes$hoverTimeRight = modNbt.getInt("hover_time_right");
            }
            if (modNbt.contains("hover_time_left")) {
                this.myneheroes$hoverTimeLeft = modNbt.getInt("hover_time_left");
            }
            if (modNbt.contains("hover_time_back")) {
                this.myneheroes$hoverTimeBack = modNbt.getInt("hover_time_back");
            }
            if (modNbt.contains("is_hover_flying")) {
                this.myneheroes$isHoverFlying = modNbt.getBoolean("is_hover_flying");
            }
        }
    }
    @Inject(at = @At("HEAD"), method = "writeCustomDataToNbt")
    private void writeToNbt(NbtCompound nbt, CallbackInfo info) {
        NbtCompound modNbt = new NbtCompound();
        if (nbt.contains(MyneHeroes.MOD_ID)) {
            modNbt = nbt.getCompound(MyneHeroes.MOD_ID);
        }

        modNbt.putInt("flight_time",this.myneheroes$flightTime);
        modNbt.putInt("hover_time_right",this.myneheroes$hoverTimeRight);
        modNbt.putInt("hover_time_left",this.myneheroes$hoverTimeLeft);
        modNbt.putInt("hover_time_back",this.myneheroes$hoverTimeBack);
        modNbt.putBoolean("is_hover_flying",this.myneheroes$isHoverFlying);

        nbt.put(MyneHeroes.MOD_ID,modNbt);
    }

    @Unique
    /**
     * Converts the player's world-space velocity into player-relative space
     * based on the player's yaw.
     *
     * <p>Coordinate meaning of the returned vector:</p>
     * <ul>
     *   <li><b>+Z</b> — moving forward (in the direction the player is facing)</li>
     *   <li><b>-Z</b> — moving backward</li>
     *   <li><b>+X</b> — moving right relative to the player's facing direction</li>
     *   <li><b>-X</b> — moving left</li>
     *   <li><b>Y</b> — unchanged vertical velocity</li>
     * </ul>
     *
     * @return a {@link Vec3d} representing the player's velocity relative to yaw
     */
    private Vec3d getVelocityRelativeToYaw() {
        PlayerEntity player = (PlayerEntity) (Object) this;
        Vec3d velocity = player.getVelocity();

        float yaw = (float) Math.toRadians(player.getYaw());

        double sin = Math.sin(-yaw);
        double cos = Math.cos(-yaw);

        double relativeX = velocity.x * cos - velocity.z * sin;
        double relativeZ = velocity.x * sin + velocity.z * cos;

        return new Vec3d(relativeX, velocity.y, relativeZ);
    }

    @Inject(at = @At("HEAD"), method = "tickMovement")
    private void moveHoverFlight(CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (myneheroes$isHoverFlying() && player.isOnGround()) {
            myneheroes$isHoverFlying = false;
        }
        if (myneheroes$isHoverFlying()) {
            if (!myneheroes$canHoverFly()) {
                myneheroes$setHoverFlying(false);
                return;
            }
            if (player.isSprinting()) {
                //Add do a barrel roll like movement
                Vec3d look = player.getRotationVector();
                double speed = 1D;
                player.setVelocity(look.multiply(speed));
            } else {
                // Zero gravity while hovering
                player.setVelocity(player.getVelocity().multiply(1, 0, 1));
            }

            if (((LivingEntityJumpAccessor) player).myneheroes$jumping()) {
                player.setVelocity(player.getVelocity().add(0, 0.3, 0));
            }
            if (player.isSneaking()) {
                player.setVelocity(player.getVelocity().add(0, -0.3, 0));
            }
            player.fallDistance = 0;
        }
    }

    @Unique
    public boolean myneheroes$isHoverFlying() {
        return this.myneheroes$isHoverFlying;
    }

    @Unique
    public void myneheroes$setHoverFlying(boolean flying) {
        this.myneheroes$isHoverFlying = flying;
    }

    @Unique
    public float myneheroes$getHoverProgress() {
        return myneheroes$getHoverProgressRight() + myneheroes$getHoverProgressLeft();
    }

    @Unique
    public float myneheroes$getHoverProgressRight() {
        return this.myneheroes$hoverTimeRight / 15F;
    }

    @Unique
    public float myneheroes$getHoverProgressLeft() {
        return this.myneheroes$hoverTimeLeft / 15F;
    }

    @Unique
    public float myneheroes$getHoverProgressBack() {
        return this.myneheroes$hoverTimeBack / 15F;
    }

    @Unique
    public float myneheroes$getFlightProgress() {
        return this.myneheroes$flightTime / 15F;
    }

    @Unique
    public boolean myneheroes$canHoverFly() {
        PlayerEntity player = (PlayerEntity) (Object) this;
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

    @ModifyReturnValue(method = "getOffGroundSpeed", at = @At("RETURN"))
    protected float modifyOffGroundSpeed(float original) {
        if (myneheroes$isHoverFlying()) {
            return 0.05F;
        }
        return original;
    }

    @Redirect(
            method = "updatePose",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;isFallFlying()Z"
            )
    )
    private boolean myneheroes$includeHoverFlying(PlayerEntity player) {
        return player.isFallFlying() || (myneheroes$isHoverFlying() && player.isSprinting());
    }

    @Redirect(
            method = "updatePose",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;isSneaking()Z"
            )
    )
    private boolean myneheroes$excludeHoverFlying(PlayerEntity player) {
        return player.isSneaking() && !myneheroes$isHoverFlying();
    }

    @ModifyReturnValue(method = "shouldSwimInFluids", at = @At("RETURN"))
    public boolean dontSwimWhileFlying(boolean original) {
        return original && !myneheroes$isHoverFlying();
    }

    @Inject(method = "updateSwimming", at = @At("HEAD"))
    private void stopSwimmingWhenFlying(CallbackInfo ci) {
        if (myneheroes$isHoverFlying()) {
            PlayerEntity player = (PlayerEntity) (Object) this;
            player.setSwimming(false);
        }
    }

    @ModifyReturnValue(method = "getVelocityMultiplier", at = @At("RETURN"))
    public float modifyVelocityMultiplier(float original) {
        return myneheroes$isHoverFlying() ? 1.0F : original;
    }
}