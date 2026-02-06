package net.togyk.myneheroes.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.Item.custom.EquipCallbackItem;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.PassiveAbility;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.PlayerAbilities;
import net.togyk.myneheroes.util.PlayerHoverFlightControl;
import net.togyk.myneheroes.util.PowerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntity.class)
public class LivingAppliedAttributeMixin {
    @Unique
    private int jumps = 0;
    @Unique
    private int jumpResetTimer = 0;

    @Unique
    @Inject(method = "isInsideWall", at = @At("HEAD"), cancellable = true)
    private void ignoreInsideWall(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity)(Object)this;
        if (entity instanceof PlayerEntity player) {
            for (Power power : PowerData.getPowersWithoutSyncing(player)) {
                if (power.isPhasing()) {
                    cir.setReturnValue(false);
                    break;
                }
            }
        }
    }

    @Inject(method = "canWalkOnFluid", at = @At("HEAD"), cancellable = true)
    public void mayWalkOnFluid(FluidState state, CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof PlayerEntity player && !player.isSneaking()) {
            for (Power power : PowerData.getPowersWithoutSyncing(player)) {
                if (power.canStandOnWater()) {
                    cir.setReturnValue(true);
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "tryUseTotem", cancellable = true)
    private void myneheroes$PassiveAbilityDeathUpdater(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof PlayerEntity player) {
            List<Ability> abilities = ((PlayerAbilities) player).myneheroes$getAbilities();

            boolean shouldCancel = false;
            for (Ability ability : abilities) {
                if (ability instanceof PassiveAbility passiveAbility && !passiveAbility.onDeath(player, source)) {
                    shouldCancel = true;
                }
            }

            if (shouldCancel) {
                player.setHealth(0.5F);
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    private void myneheroes$resetJumpCharge(CallbackInfo ci) {
        if (jumpResetTimer > 0) {
            jumpResetTimer -= 1;
            if (jumpResetTimer <= 0 && jumps > 0) {
                LivingEntity entity = (LivingEntity) (Object) this;
                entity.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASS.value(), 1.0F, 1.0F);
                this.jumps = 0;
            }
        } else {
            this.jumpResetTimer = 0;
        }
    }

    @Inject(at = @At("HEAD"), method = "jump", cancellable = true)
    private void myneheroes$SuperJump(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof PlayerEntity player) {
            if (!canSuperJump(player)) return;
            int neededJumps = getJumpChargeNeeded(player);

            if (player.isOnGround() && !player.isSneaking()) {
                if (neededJumps <= this.jumps) {
                    float strength = 1.0f;

                    double vertical = 0.4 + (1 * strength); // height
                    double forward = 1.5 * strength;          // distance

                    Vec3d look = player.getRotationVec(1.0F);

                    Vec3d velocity = new Vec3d(
                            look.x * forward,
                            vertical,
                            look.z * forward
                    );

                    player.setVelocity(velocity);
                    player.velocityModified = true;

                    this.jumps = 0;
                    ci.cancel();
                } else {
                    player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 0.25F);
                    this.jumps += 1;
                    this.jumpResetTimer = 40;
                }
            }
        }
    }

    @Unique
    private boolean canSuperJump(PlayerEntity player) {
        for (Power power : PowerData.getPowers(player)) {
            if (power.canSuperJump()) {
                return true;
            }
        }
        return false;
    }

    @Unique
    private int getJumpChargeNeeded(PlayerEntity player) {
        List<Integer> charges = new ArrayList<>();
        for (Power power : PowerData.getPowers(player)) {
            if (power.canSuperJump()) {
                charges.add(power.getJumpChargesNeeded());
            }
        }
        return charges.stream().min(Integer::compareTo).orElse(0);
    }

    @Unique
    private double getJumpStrength(PlayerEntity player) {
        List<Double> strengths = new ArrayList<>();
        for (Power power : PowerData.getPowers(player)) {
            if (power.canSuperJump()) {
                strengths.add(power.getSuperJumpStrength());
            }
        }
        return strengths.stream().max(Double::compareTo).orElse(1.0);
    }

    @ModifyVariable(
            method = "updateLimbs*",
            at = @At("STORE"),
            ordinal = 0
    )
    private float myneheroes$scaleWalkAnim(float original) {
        LivingEntity entity = (LivingEntity)(Object)this;
        if (!(entity instanceof PlayerEntity player)) return original;

        float hover = ((PlayerHoverFlightControl) player)
                .myneheroes$getHoverProgress();

        // Fade walk animation out while hovering
        return original * (1.0F - hover);
    }

    //EquipCallbackItem items
    @Inject(method = "onEquipStack", at = @At("HEAD"))
    private void addAttributes(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo ci) {
        if (ItemStack.areEqual(oldStack, newStack) || !slot.isArmorSlot()) {
            return;
        }

        if (oldStack.getItem() instanceof EquipCallbackItem item) {
            item.onUnequipped((LivingEntity) (Object) this, oldStack, slot);
        }

        if (newStack.getItem() instanceof EquipCallbackItem item) {
            item.onEquipped((LivingEntity) (Object) this, newStack, slot);
        }
    }
}
