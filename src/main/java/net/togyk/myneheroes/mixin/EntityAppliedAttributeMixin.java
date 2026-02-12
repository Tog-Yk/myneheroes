package net.togyk.myneheroes.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.togyk.myneheroes.util.PlayerHoverFlightControl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityAppliedAttributeMixin {
    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    private void replaceYaw(double mouseX, double mouseY, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof PlayerEntity player && (((PlayerHoverFlightControl) player).myneheroes$isHoverFlying() && player.isSprinting())) {

            float roll = ((PlayerHoverFlightControl) player).myneheroes$getRoll();
            float newRoll = roll + (float) (-mouseX * 0.15F);
            ((PlayerHoverFlightControl) player).myneheroes$setRoll(wrapAngle(newRoll));

            double pitchMultiplier = Math.cos(Math.toRadians(newRoll));
            double yawMultiplier = Math.sin(Math.toRadians(newRoll));
            float pitchDiff = (float) (mouseY * 0.15F * pitchMultiplier);
            float yawDiff = (float) (mouseY * 0.15F * yawMultiplier);

            player.setPitch(wrapAngle(player.getPitch() + pitchDiff));
            player.setYaw(wrapAngle(player.getYaw() + yawDiff));

            // Cancel vanilla yaw changing
            ci.cancel();
        }
    }

    @Unique
    private static float wrapAngle(float angle) {
        angle %= 360.0f; // reduce to -360..360 range

        if (angle >= 180.0f)
            angle -= 360.0f;
        if (angle < -180.0f)
            angle += 360.0f;

        return angle;
    }
}
