package net.togyk.myneheroes.keybind;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.event.MouseScrollCallback;
import net.togyk.myneheroes.networking.AbilityKeybindPayload;
import net.togyk.myneheroes.util.ClientScrollData;
import net.togyk.myneheroes.util.PlayerAbilities;

import java.util.List;

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
                                ability.usePressed(player);
                                ClientPlayNetworking.send(new AbilityKeybindPayload(0, 1));
                            }
                        }
                    } else if (firstWasPressed) {
                        if (!AbilitiesBlockedForScrolling) {
                            ability.use(player);
                            ClientPlayNetworking.send(new AbilityKeybindPayload(0, 0));
                        } else {
                            AbilitiesBlockedForScrolling = false;
                        }
                    } else {
                        ability.useReleased(player);
                        ClientPlayNetworking.send(new AbilityKeybindPayload(0, 2));
                    }
                }

                firstWasPressed = ModKeyBinds.useFirstAbility.isPressed();


                ability = ((PlayerAbilities) player).myneheroes$getSecondAbility();
                if (ability != null) {
                    if (ModKeyBinds.useSecondAbility.isPressed()) {
                        if (!AbilitiesBlockedForScrolling) {
                            if (ability.canHold(player)) {
                                ability.usePressed(player);
                                ClientPlayNetworking.send(new AbilityKeybindPayload(1, 1));
                            }
                        }
                    } else if (secondWasPressed) {
                        if (!AbilitiesBlockedForScrolling) {
                            ability.use(player);
                            ClientPlayNetworking.send(new AbilityKeybindPayload(1, 0));
                        } else {
                            AbilitiesBlockedForScrolling = false;
                        }
                    } else {
                        ability.useReleased(player);
                        ClientPlayNetworking.send(new AbilityKeybindPayload(1, 2));
                    }
                }

                secondWasPressed = ModKeyBinds.useSecondAbility.isPressed();


                ability = ((PlayerAbilities) player).myneheroes$getThirdAbility();
                if (ability != null) {
                    if (ModKeyBinds.useThirdAbility.isPressed()) {
                        if (!AbilitiesBlockedForScrolling) {
                            if (ability.canHold(player)) {
                                ability.usePressed(player);
                                ClientPlayNetworking.send(new AbilityKeybindPayload(2, 1));
                            }
                        }
                    } else if (thirdWasPressed) {
                        if (!AbilitiesBlockedForScrolling) {
                            ability.use(player);
                            ClientPlayNetworking.send(new AbilityKeybindPayload(2, 0));
                        } else {
                            AbilitiesBlockedForScrolling = false;
                        }
                    } else {
                        ability.useReleased(player);
                        ClientPlayNetworking.send(new AbilityKeybindPayload(2, 2));
                    }
                }

                thirdWasPressed = ModKeyBinds.useThirdAbility.isPressed();


                ability = ((PlayerAbilities) player).myneheroes$getFourthAbility();
                if (ability != null) {
                    if (ModKeyBinds.useFourthAbility.isPressed()) {
                        if (!AbilitiesBlockedForScrolling) {
                            if (ability.canHold(player)) {
                                ability.usePressed(player);
                                ClientPlayNetworking.send(new AbilityKeybindPayload(3, 1));
                            }
                        }
                    } else if (fourthWasPressed) {
                        if (!AbilitiesBlockedForScrolling) {
                            ability.use(player);
                            ClientPlayNetworking.send(new AbilityKeybindPayload(3, 0));
                        } else {
                            AbilitiesBlockedForScrolling = false;
                        }
                    } else {
                        ability.useReleased(player);
                        ClientPlayNetworking.send(new AbilityKeybindPayload(3, 2));
                    }
                }

                fourthWasPressed = ModKeyBinds.useFourthAbility.isPressed();

                //call the release method for every ability you can't press
                List<Ability> abilities = ((PlayerAbilities) player).myneheroes$getAbilities();
                for (int i = 0; i < abilities.size(); i++) {
                    if (i < ((PlayerAbilities) player).myneheroes$getScrolledAbilityOffset() || i > ((PlayerAbilities) player).myneheroes$getScrolledAbilityOffset() + 3) {
                        ability = abilities.get(i);
                        ability.useReleased(player);
                    }
                }

                if (ModKeyBinds.powersScrollUp.isPressed()) {
                    ClientScrollData.scrollPowersFurther(client.player);
                }
                if (ModKeyBinds.powersScrollDown.isPressed()) {
                    ClientScrollData.scrollPowersBack(client.player);
                }
            }
        });

        //call the release method for every ability you can't press on the server
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (PlayerEntity player : server.getPlayerManager().getPlayerList()) {
                //call the release method for every ability you can't press
                List<Ability> abilities = ((PlayerAbilities) player).myneheroes$getAbilities();
                for (int i = 0; i < abilities.size(); i++) {
                    if (i < ((PlayerAbilities) player).myneheroes$getScrolledAbilityOffset() || i > ((PlayerAbilities) player).myneheroes$getScrolledAbilityOffset() + 3) {
                        Ability ability = abilities.get(i);
                        ability.useReleased(player);
                    }
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
