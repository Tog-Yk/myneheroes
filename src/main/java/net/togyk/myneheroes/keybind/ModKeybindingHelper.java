package net.togyk.myneheroes.keybind;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.networking.KeybindPayload;
import net.togyk.myneheroes.networking.UseSelectedAbilityPayload;
import net.togyk.myneheroes.util.ScrollData;
import net.togyk.myneheroes.util.PlayerAbilities;

public class ModKeybindingHelper {
    public static void registerModKeybingHelper() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Access the Use Item/Place Block keybind
            if (ModKeyBindings.fuelReactor.isPressed()) {
                if (MinecraftClient.getInstance().player != null){
                    ClientPlayNetworking.send(new KeybindPayload(5));
                }
            }
            if (ModKeyBindings.useFirstAbility.isPressed()) {
                if (MinecraftClient.getInstance().player != null){
                    Ability ability = ((PlayerAbilities) MinecraftClient.getInstance().player).getFirstAbility();
                    if (ability != null) {
                        //to do everything what has to be done on the client side
                        ability.Use(MinecraftClient.getInstance().player);
                        //to send a message to the server to do everything what has to be done on the server side
                        ClientPlayNetworking.send(new KeybindPayload(0));
                    }
                }
            }
            if (ModKeyBindings.useSecondAbility.isPressed()) {
                if (MinecraftClient.getInstance().player != null){
                    Ability ability = ((PlayerAbilities) MinecraftClient.getInstance().player).getSecondAbility();
                    if (ability != null) {
                        //to do everything what has to be done on the client side
                        ability.Use(MinecraftClient.getInstance().player);
                        //to send a message to the server to do everything what has to be done on the server side
                        ClientPlayNetworking.send(new KeybindPayload(1));
                    }
                }
            }
            if (ModKeyBindings.useThirdAbility.isPressed()) {
                if (MinecraftClient.getInstance().player != null){
                    Ability ability = ((PlayerAbilities) MinecraftClient.getInstance().player).getThirdAbility();
                    if (ability != null) {
                        //to do everything what has to be done on the client side
                        ability.Use(MinecraftClient.getInstance().player);
                        //to send a message to the server to do everything what has to be done on the server side
                        ClientPlayNetworking.send(new KeybindPayload(2));
                    }
                }
            }
            if (ModKeyBindings.useFourthAbility.isPressed()) {
                if (MinecraftClient.getInstance().player != null){
                    Ability ability = ((PlayerAbilities) MinecraftClient.getInstance().player).getFourthAbility();
                    if (ability != null) {
                        //to do everything what has to be done on the client side
                        ability.Use(MinecraftClient.getInstance().player);
                        //to send a message to the server to do everything what has to be done on the server side
                        ClientPlayNetworking.send(new KeybindPayload(3));
                    }
                }
            }
            if (ModKeyBindings.abilitiesScrollUp.isPressed()) {
                if (client.player != null){
                    if (ScrollData.getScrolledAbilitiesOffset(client.player) > 0) {
                        ScrollData.scrollAbilitiesBack(client.player);
                    }
                }
            }
            if (ModKeyBindings.abilitiesScrollDown.isPressed()) {
                if (client.player != null){
                    if (ScrollData.canScrollAbilitiesFurther(client.player)) {
                        ScrollData.scrollAbilitiesFurther(client.player);
                    }
                }
            }

            if (ModKeyBindings.powersScrollUp.isPressed()) {
                if (client.player != null){
                    ScrollData.scrollPowersFurther(client.player);
                }
            }
            if (ModKeyBindings.powersScrollDown.isPressed()) {
                if (client.player != null){
                    ScrollData.scrollPowersBack(client.player);
                }
            }
        });
    }
}
