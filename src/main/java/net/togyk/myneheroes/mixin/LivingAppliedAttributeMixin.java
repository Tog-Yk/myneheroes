package net.togyk.myneheroes.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.PowerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
}
