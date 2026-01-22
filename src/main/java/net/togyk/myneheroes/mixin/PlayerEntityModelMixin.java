package net.togyk.myneheroes.mixin;

import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.togyk.myneheroes.util.PlayerHoverFlightControl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityModel.class)
public abstract class PlayerEntityModelMixin<T extends LivingEntity> {

    @Inject(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            at = @At("TAIL")
    )
    private void myneheroes$applyHoverPose(
            T entity,
            float limbAngle,
            float limbDistance,
            float animationProgress,
            float headYaw,
            float headPitch,
            CallbackInfo ci
    ) {
        if (!(entity instanceof PlayerEntity player)) return;

        PlayerHoverFlightControl flightControl = ((PlayerHoverFlightControl) player);
        float hover = flightControl.myneheroes$getHoverProgress();
        if (hover != 0F) {

            PlayerEntityModel<?> model = (PlayerEntityModel<?>) (Object) this;

            float hoverProgressBack = flightControl.myneheroes$getHoverProgressBack();
            // --------------------
            // Arms: slightly back
            // --------------------

            float armsBackProgress = hover - 1.5F*hoverProgressBack;
            float armBack = (float) Math.toRadians(12) * armsBackProgress;
            float armOut = (float) Math.toRadians(6) * hover;

            model.leftArm.pitch += armBack;
            model.leftSleeve.pitch += armBack;

            model.rightArm.pitch += armBack;
            model.rightSleeve.pitch += armBack;


            model.leftArm.yaw -= armOut;
            model.leftSleeve.yaw -= armOut;

            model.rightArm.yaw += armOut;
            model.rightSleeve.yaw += armOut;

            // Optional: slight bend
            model.leftArm.roll += (float) Math.toRadians(-3) * hover;
            model.leftSleeve.roll += (float) Math.toRadians(-3) * hover;

            model.rightArm.roll += (float) Math.toRadians(3) * hover;
            model.rightSleeve.roll += (float) Math.toRadians(3) * hover;

            // --------------------
            // Knee lift
            // --------------------

            float rightKneePitch = (float) Math.toRadians(15) * flightControl.myneheroes$getHoverProgressRight() * (1.0f - hoverProgressBack);
            float leftKneePitch = (float) Math.toRadians(15) * flightControl.myneheroes$getHoverProgressLeft() * (1.0f - hoverProgressBack);

            model.leftLeg.pitch += leftKneePitch + rightKneePitch * 0.3F;
            model.leftPants.pitch += leftKneePitch + rightKneePitch * 0.3F;

            model.rightLeg.pitch += rightKneePitch + leftKneePitch * 0.3F;
            model.rightPants.pitch += rightKneePitch + leftKneePitch * 0.3F;

            float rightKneeYaw = (float) Math.toRadians(10) * flightControl.myneheroes$getHoverProgressRight() * (1.0f - hoverProgressBack);
            float leftKneeYaw = (float) Math.toRadians(10) * flightControl.myneheroes$getHoverProgressLeft() * (1.0f - hoverProgressBack);

            model.rightLeg.yaw += rightKneeYaw;
            model.rightPants.yaw += rightKneeYaw;

            model.leftLeg.yaw -= leftKneeYaw;
            model.leftPants.yaw -= leftKneeYaw;

            float rightLegMovement = flightControl.myneheroes$getHoverProgressRight() * (1.0f - hoverProgressBack);
            model.rightLeg.pivotY -= rightLegMovement * 1.5F;
            model.rightLeg.pivotZ -= rightLegMovement * 2.0F;
            model.rightPants.pivotY -= rightLegMovement * 1.5F;
            model.rightPants.pivotZ -= rightLegMovement * 2.0F;

            float leftLegMovement = flightControl.myneheroes$getHoverProgressLeft() * (1.0f - hoverProgressBack);
            model.leftLeg.pivotY -= leftLegMovement * 1.5F;
            model.leftLeg.pivotZ -= leftLegMovement * 2.0F;
            model.leftPants.pivotY -= leftLegMovement * 1.5F;
            model.leftPants.pivotZ -= leftLegMovement * 2.0F;
        }
    }
}
