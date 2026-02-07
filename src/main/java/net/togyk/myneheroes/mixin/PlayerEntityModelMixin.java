package net.togyk.myneheroes.mixin;

import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.togyk.myneheroes.util.PlayerHoverFlightControl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityModel.class)
public abstract class PlayerEntityModelMixin<T extends LivingEntity> {

    @Inject(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            at = @At("TAIL")
    )
    private void myneheroes$applyHoverFlightPose(
            T entity,
            float limbAngle,
            float limbDistance,
            float animationProgress,
            float headYaw,
            float headPitch,
            CallbackInfo ci
    ) {
        if (!(entity instanceof PlayerEntity player)) return;
        PlayerEntityModel<?> model = (PlayerEntityModel<?>) (Object) this;
        PlayerHoverFlightControl flightControl = ((PlayerHoverFlightControl) player);

        float hover = flightControl.myneheroes$getHoverProgress();
        float flight = flightControl.myneheroes$getFlightProgress();
        if (hover != 0F) {
            applyHoverPose(model, flightControl, hover - flight);
        }
        if (flight != 0F) {
            applyFlightPose(model, flightControl, flight);
        }
    }

    @Unique
    private void applyHoverPose(PlayerEntityModel<?> model, PlayerHoverFlightControl flightControl, float progress) {

        float hoverProgressBack = flightControl.myneheroes$getHoverProgressBack();
        // --------------------
        // Arms: slightly back
        // --------------------

        float armsBackProgress = flightControl.myneheroes$getHoverProgress() - 1.5F*hoverProgressBack;
        float armBack = (float) Math.toRadians(12) * armsBackProgress;
        float armOut = (float) Math.toRadians(6) * progress;

        model.leftArm.pitch += armBack;
        model.leftSleeve.pitch += armBack;

        model.rightArm.pitch += armBack;
        model.rightSleeve.pitch += armBack;


        model.leftArm.yaw -= armOut;
        model.leftSleeve.yaw -= armOut;

        model.rightArm.yaw += armOut;
        model.rightSleeve.yaw += armOut;

        // Optional: slight bend
        model.leftArm.roll += (float) Math.toRadians(-3) * progress;
        model.leftSleeve.roll += (float) Math.toRadians(-3) * progress;

        model.rightArm.roll += (float) Math.toRadians(3) * progress;
        model.rightSleeve.roll += (float) Math.toRadians(3) * progress;

        // --------------------
        // Knee lift
        // --------------------

        float rightKneePitch = (float) Math.toRadians(15) * flightControl.myneheroes$getHoverProgressRight() * (1.0f - hoverProgressBack) * progress;
        float leftKneePitch = (float) Math.toRadians(15) * flightControl.myneheroes$getHoverProgressLeft() * (1.0f - hoverProgressBack) * progress;

        model.leftLeg.pitch += (leftKneePitch + rightKneePitch * 0.3F) * progress;
        model.leftPants.pitch += (leftKneePitch + rightKneePitch * 0.3F) * progress;

        model.rightLeg.pitch += (rightKneePitch + leftKneePitch * 0.3F) * progress;
        model.rightPants.pitch += (rightKneePitch + leftKneePitch * 0.3F) * progress;

        float rightKneeYaw = (float) Math.toRadians(10) * flightControl.myneheroes$getHoverProgressRight() * (1.0f - hoverProgressBack) * progress;
        float leftKneeYaw = (float) Math.toRadians(10) * flightControl.myneheroes$getHoverProgressLeft() * (1.0f - hoverProgressBack) * progress;

        model.rightLeg.yaw += rightKneeYaw;
        model.rightPants.yaw += rightKneeYaw;

        model.leftLeg.yaw -= leftKneeYaw;
        model.leftPants.yaw -= leftKneeYaw;

        float rightLegMovement = flightControl.myneheroes$getHoverProgressRight() * (1.0f - hoverProgressBack) * progress;
        model.rightLeg.pivotY -= rightLegMovement * 1.5F;
        model.rightLeg.pivotZ -= rightLegMovement * 2.0F;
        model.rightPants.pivotY -= rightLegMovement * 1.5F;
        model.rightPants.pivotZ -= rightLegMovement * 2.0F;

        float leftLegMovement = flightControl.myneheroes$getHoverProgressLeft() * (1.0f - hoverProgressBack) * progress;
        model.leftLeg.pivotY -= leftLegMovement * 1.5F;
        model.leftLeg.pivotZ -= leftLegMovement * 2.0F;
        model.leftPants.pivotY -= leftLegMovement * 1.5F;
        model.leftPants.pivotZ -= leftLegMovement * 2.0F;
    }

    @Unique
    private void applyFlightPose(PlayerEntityModel<?> model, PlayerHoverFlightControl flightControl, float progress) {
        model.head.pitch -= (float) ((model.head.pitch + Math.toRadians(90)) * progress);
        model.hat.pitch -= (float) ((model.hat.pitch + Math.toRadians(90)) * progress);

        model.rightArm.pitch -= (float) (Math.toRadians(180) * progress);
        model.rightSleeve.pitch -= (float) (Math.toRadians(180) * progress);
        model.rightArm.yaw += (float) (Math.toRadians(20) * progress);
        model.rightSleeve.yaw += (float) (Math.toRadians(20) * progress);
    }

}
