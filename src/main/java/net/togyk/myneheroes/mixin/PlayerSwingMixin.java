package net.togyk.myneheroes.mixin;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.event.MissedSwingCallback;
import net.togyk.myneheroes.networking.ModMessages;
import net.togyk.myneheroes.networking.PlayerSwingPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class PlayerSwingMixin {
    @Inject(method = "doAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;hasLimitedAttackSpeed()Z",
                    shift = At.Shift.BEFORE
            ))
    private void onMissSwing(CallbackInfoReturnable ci) {
        // Run only on the server side because arrow spawning is a server action.
        MinecraftClient client = (MinecraftClient) (Object) this;
        PlayerEntity player = client.player;
        if (player.getWorld().isClient()) {
            MissedSwingCallback.EVENT.invoker().onMissedSwing(player, Hand.MAIN_HAND);
            ClientPlayNetworking.send(new PlayerSwingPayload());
        }
    }
}
