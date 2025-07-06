package net.togyk.myneheroes.keybind;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.event.MouseScrollCallback;
import net.togyk.myneheroes.networking.AbilityKeybindPayload;
import net.togyk.myneheroes.util.ClientScrollData;
import net.togyk.myneheroes.util.PlayerAbilities;

public class ModKeybindingHelper {
    private static boolean firstWasPressed = false;
    private static boolean secondWasPressed = false;
    private static boolean thirdWasPressed = false;
    private static boolean fourthWasPressed = false;
    private static boolean AbilitiesBlockedForScrolling = false;

    public static void registerModKeybingHelper() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            PlayerEntity player = client.player;

            if (player != null) {
                Ability ability = ((PlayerAbilities) player).myneheroes$getFirstAbility();
                if (ability != null) {
                    if (ModKeyBinds.useFirstAbility.isPressed()) {
                        if (!AbilitiesBlockedForScrolling) {
                            if (ability.canHold(player)) {
                                ability.hold(player);
                                ClientPlayNetworking.send(new AbilityKeybindPayload(0, true));
                            }
                        }
                    } else if (firstWasPressed) {
                        if (!AbilitiesBlockedForScrolling) {
                            ability.Use(player);
                            ClientPlayNetworking.send(new AbilityKeybindPayload(0, false));
                        } else {
                            AbilitiesBlockedForScrolling = false;
                        }
                    }
                }

                firstWasPressed = ModKeyBinds.useFirstAbility.isPressed();


                ability = ((PlayerAbilities) player).myneheroes$getSecondAbility();
                if (ability != null) {
                    if (ModKeyBinds.useSecondAbility.isPressed()) {
                        if (AbilitiesBlockedForScrolling) {
                            if (ability.canHold(player)) {
                                ability.hold(player);
                                ClientPlayNetworking.send(new AbilityKeybindPayload(1, true));
                            }
                        }
                    } else if (secondWasPressed) {
                        if (!AbilitiesBlockedForScrolling) {
                            ability.Use(player);
                            ClientPlayNetworking.send(new AbilityKeybindPayload(1, false));
                        } else {
                            AbilitiesBlockedForScrolling = false;
                        }
                    }
                }

                secondWasPressed = ModKeyBinds.useSecondAbility.isPressed();


                ability = ((PlayerAbilities) player).myneheroes$getThirdAbility();
                if (ability != null) {
                    if (ModKeyBinds.useThirdAbility.isPressed()) {
                        if (!AbilitiesBlockedForScrolling) {
                            if (ability.canHold(player)) {
                                ability.hold(player);
                                ClientPlayNetworking.send(new AbilityKeybindPayload(2, true));
                            }
                        }
                    } else if (thirdWasPressed) {
                        if (!AbilitiesBlockedForScrolling) {
                            ability.Use(player);
                            ClientPlayNetworking.send(new AbilityKeybindPayload(2, false));
                        } else {
                            AbilitiesBlockedForScrolling = false;
                        }
                    }
                }

                thirdWasPressed = ModKeyBinds.useThirdAbility.isPressed();


                ability = ((PlayerAbilities) player).myneheroes$getFourthAbility();
                if (ability != null) {
                    if (ModKeyBinds.useFourthAbility.isPressed()) {
                        if (!AbilitiesBlockedForScrolling) {
                            if (ability.canHold(player)) {
                                ability.hold(player);
                                ClientPlayNetworking.send(new AbilityKeybindPayload(3, true));
                            }
                        }
                    } else if (fourthWasPressed) {
                        if (!AbilitiesBlockedForScrolling) {
                            ability.Use(player);
                            ClientPlayNetworking.send(new AbilityKeybindPayload(3, false));
                        } else {
                            AbilitiesBlockedForScrolling = false;
                        }
                    }
                }

                fourthWasPressed = ModKeyBinds.useFourthAbility.isPressed();
            }

            if (ModKeyBinds.powersScrollUp.isPressed()) {
                if (client.player != null){
                    ClientScrollData.scrollPowersFurther(client.player);
                }
            }
            if (ModKeyBinds.powersScrollDown.isPressed()) {
                if (client.player != null){
                    ClientScrollData.scrollPowersBack(client.player);
                }
            }
        });

        MouseScrollCallback.EVENT.register((
                (client, mouseX, mouseY, hScroll, vScroll) -> {
                    PlayerEntity player = client.player;
                    if (client.currentScreen == null && player != null) {
                        if (ModKeyBinds.useFirstAbility.isPressed() || ModKeyBinds.useSecondAbility.isPressed() || ModKeyBinds.useThirdAbility.isPressed() || ModKeyBinds.useFourthAbility.isPressed()) {
                            if (client.player != null) {
                                int current = ClientScrollData.getScrolledAbilitiesOffset(client.player);
                                int maxScroll = ClientScrollData.getMaxAbilityScroll(client.player);
                                int target = Math.clamp((int) (current + -vScroll), 0, maxScroll);
                                if (target != current) {
                                    AbilitiesBlockedForScrolling = true;
                                    ClientScrollData.setScrolledAbilitiesOffset(client.player, target);
                                    return true;
                                }
                            }
                            return true;
                        }
                    }
                    return false;
                }
        ));
    }
}
