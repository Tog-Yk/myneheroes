package net.togyk.myneheroes.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.event.MissedInteractionCallback;
import net.togyk.myneheroes.event.MissedSwingCallback;
import net.togyk.myneheroes.networking.PlayerInteractionPayload;
import net.togyk.myneheroes.networking.PlayerSwingPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class PlayerSwingMixin {
    @Inject(method = "doAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;hasLimitedAttackSpeed()Z",
                    shift = At.Shift.BEFORE
            ))
    private void onMissSwing(CallbackInfoReturnable<Boolean> cir) {
        // Run only on the server side because arrow spawning is a server action.
        MinecraftClient client = (MinecraftClient) (Object) this;
        PlayerEntity player = client.player;
        if (player.getWorld().isClient()) {
            MissedSwingCallback.EVENT.invoker().onMissedSwing(player, Hand.MAIN_HAND);
            ClientPlayNetworking.send(new PlayerSwingPayload());
        }
    }
    @Inject(method = "doItemUse", at = @At("HEAD"))
    private void onMissInteraction(CallbackInfo ci) {
        // Run only on the server side because arrow spawning is a server action.
        MinecraftClient client = (MinecraftClient) (Object) this;
        PlayerEntity player = client.player;
        if (player.getWorld().isClient() && client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.MISS) {
            MissedInteractionCallback.EVENT.invoker().onMissedInteraction(player);
            ClientPlayNetworking.send(new PlayerInteractionPayload());
        }
    }
}
