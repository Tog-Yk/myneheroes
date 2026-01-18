package net.togyk.myneheroes.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.togyk.myneheroes.networking.SetHoverFlyingPayload;
import net.togyk.myneheroes.util.PlayerHoverFlightControl;
import net.togyk.myneheroes.util.PlayerPowers;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerToggleHoverFlyingMixin implements PlayerPowers {
    @Redirect(
            method = "tickMovement",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/player/PlayerAbilities;flying:Z",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void myneheroes$interceptCreativeFlightToggle(PlayerAbilities abilities, boolean value) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        PlayerHoverFlightControl flightControl = ((PlayerHoverFlightControl) player);

        // Only override STARTING flight
        if (!abilities.flying && flightControl.myneheroes$canHoverFly()) {
            flightControl.myneheroes$setHoverFlying(!flightControl.myneheroes$isHoverFlying());

            ClientPlayNetworking.send(new SetHoverFlyingPayload(flightControl.myneheroes$isHoverFlying()));

            // Ensure vanilla flight never starts
            abilities.flying = false;
        } else {
            // Normal vanilla behavior (stopping flight etc.)
            abilities.flying = !abilities.flying;
        }
    }

    @Redirect(
            method = "tickMovement",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/player/PlayerAbilities;allowFlying:Z",
                    opcode = Opcodes.GETFIELD
            )
    )
    private boolean myneheroes$redirectAllowFlying(PlayerAbilities abilities) {
        PlayerHoverFlightControl flightControl = (PlayerHoverFlightControl) (Object) this;
        return abilities.allowFlying || flightControl.myneheroes$canHoverFly();
    }
}