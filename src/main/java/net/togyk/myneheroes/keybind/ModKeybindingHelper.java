package net.togyk.myneheroes.keybind;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.networking.KeybindPayload;
import net.togyk.myneheroes.util.ClientScrollData;
import net.togyk.myneheroes.util.PlayerAbilities;

public class ModKeybindingHelper {
    public static void registerModKeybingHelper() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (ModKeyBindings.useFirstAbility.isPressed()) {
                if (MinecraftClient.getInstance().player != null){
                    Ability ability = ((PlayerAbilities) MinecraftClient.getInstance().player).myneheroes$getFirstAbility();
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
                    Ability ability = ((PlayerAbilities) MinecraftClient.getInstance().player).myneheroes$getSecondAbility();
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
                    Ability ability = ((PlayerAbilities) MinecraftClient.getInstance().player).myneheroes$getThirdAbility();
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
                    Ability ability = ((PlayerAbilities) MinecraftClient.getInstance().player).myneheroes$getFourthAbility();
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
                    if (ClientScrollData.getScrolledAbilitiesOffset(client.player) > 0) {
                        ClientScrollData.scrollAbilitiesBack(client.player);
                    }
                }
            }
            if (ModKeyBindings.abilitiesScrollDown.isPressed()) {
                if (client.player != null){
                    if (ClientScrollData.canScrollAbilitiesFurther(client.player)) {
                        ClientScrollData.scrollAbilitiesFurther(client.player);
                    }
                }
            }

            if (ModKeyBindings.powersScrollUp.isPressed()) {
                if (client.player != null){
                    ClientScrollData.scrollPowersFurther(client.player);
                }
            }
            if (ModKeyBindings.powersScrollDown.isPressed()) {
                if (client.player != null){
                    ClientScrollData.scrollPowersBack(client.player);
                }
            }
        });
    }
}
