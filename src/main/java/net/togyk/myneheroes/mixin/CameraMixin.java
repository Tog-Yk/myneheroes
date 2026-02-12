package net.togyk.myneheroes.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.togyk.myneheroes.util.PlayerHoverFlightControl;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {
    @Shadow
    private Quaternionf rotation;

    @Inject(method = "setRotation", at = @At("RETURN"))
    private void applyRoll(float yaw, float pitch, CallbackInfo ci) {
        // Convert roll degrees → radians
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        float roll = ((PlayerHoverFlightControl) client.player).myneheroes$getRoll();

        float rollRad = (float) Math.toRadians(roll);

        // Apply roll after vanilla yaw/pitch
        this.rotation.rotateZ(rollRad);
    }
}
