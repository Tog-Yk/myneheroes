package net.togyk.myneheroes.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.togyk.myneheroes.event.MouseScrollCallback;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Mouse.class)
public class MouseMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onMouseScroll", at =  @At("HEAD"), cancellable = true)
    private void onMouseScrolled(long window, double horizontal, double vertical, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        double mouseX = client.mouse.getX();
        double mouseY = client.mouse.getY();

        boolean cancel = MouseScrollCallback.EVENT.invoker().onMouseScroll(client, mouseX, mouseY, horizontal, vertical);

        if (cancel) {
            ci.cancel();
        }
    }
}
