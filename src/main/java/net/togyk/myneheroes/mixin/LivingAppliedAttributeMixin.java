package net.togyk.myneheroes.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.PassiveAbility;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.PlayerAbilities;
import net.togyk.myneheroes.util.PowerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LivingEntity.class)
public class LivingAppliedAttributeMixin {
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

            MyneHeroes.LOGGER.info("Died");

            boolean shouldCancel = false;
            for (Ability ability : abilities) {
                if (ability instanceof PassiveAbility passiveAbility && !passiveAbility.onDeath(player, source)) {
                    MyneHeroes.LOGGER.info("Cancel!!!!!!!!!!!!!!!!!!!!!!!");
                    shouldCancel = true;
                }
            }

            if (shouldCancel) {
                MyneHeroes.LOGGER.info("Or Did he?");
                player.setHealth(0.5F);
                cir.setReturnValue(true);
            }
        }
    }
}
