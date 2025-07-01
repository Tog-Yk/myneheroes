package net.togyk.myneheroes.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.PowerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {
    @Inject(method = "renderInWallOverlay", at = @At("HEAD"), cancellable = true)
    private static void cancelBlockOverlay(Sprite sprite, MatrixStack matrices, CallbackInfo ci) {
        PlayerEntity player = MinecraftClient.getInstance().player;

        if (player != null) {
            for (Power power : PowerData.getPowersWithoutSyncing(player)) {
                if (power.isPhasing()) {
                    ci.cancel();
                    break;
                }
            }
        }
    }
}
