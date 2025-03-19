package net.togyk.myneheroes.keybind;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.networking.KeybindPayload;
import net.togyk.myneheroes.util.AbilityScrollData;
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
            if (ModKeyBindings.unlockAbility.isPressed()) {
                if (MinecraftClient.getInstance().player != null){
                    ClientPlayNetworking.send(new KeybindPayload(10));
                }
            }
            if (ModKeyBindings.useFirstAbility.isPressed()) {
                if (MinecraftClient.getInstance().player != null){
                    Ability ability = ((PlayerAbilities) MinecraftClient.getInstance().player).getFirstAbility();
                    if (ability != null) {
                        //send two things
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
                        //send two things
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
                        //send two things
                        //to do everything what has to be done on the client side
                        ability.Use(MinecraftClient.getInstance().player);
                        //to send a message to the server to do everything what has to be done on the server side
                        ClientPlayNetworking.send(new KeybindPayload(2));
                    }
                }
            }
            if (ModKeyBindings.useForthAbility.isPressed()) {
                if (MinecraftClient.getInstance().player != null){
                    Ability ability = ((PlayerAbilities) MinecraftClient.getInstance().player).getFourthAbility();
                    if (ability != null) {
                        //send two things
                        //to do everything what has to be done on the client side
                        ability.Use(MinecraftClient.getInstance().player);
                        //to send a message to the server to do everything what has to be done on the server side
                        ClientPlayNetworking.send(new KeybindPayload(3));
                    }
                }
            }
            if (ModKeyBindings.abilitiesScrollUp.isPressed()) {
                if (client.player != null){
                    if (AbilityScrollData.getScrolledOffset(client.player) > 0) {
                        AbilityScrollData.scrollBack(client.player);
                    }
                }
            }
            if (ModKeyBindings.abilitiesScrollDown.isPressed()) {
                if (client.player != null){
                    if (AbilityScrollData.canScrollFurther(client.player)) {
                        AbilityScrollData.scrollFurther(client.player);
                    }
                }
            }
        });
    }
}
