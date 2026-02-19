package net.togyk.myneheroes.keybind;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.ScrollableAbility;
import net.togyk.myneheroes.event.MouseScrollCallback;
import net.togyk.myneheroes.networking.AbilityKeybindPayload;
import net.togyk.myneheroes.networking.AbilityScrollPayload;
import net.togyk.myneheroes.util.ClientScrollData;
import net.togyk.myneheroes.util.PlayerAbilities;

import java.util.List;

public class ModKeybindingHelper {
    private static boolean firstWasPressed = false;
    private static boolean secondWasPressed = false;
    private static boolean thirdWasPressed = false;
    private static boolean fourthWasPressed = false;
    private static boolean AbilitiesBlockedForScrolling = false;

    private static int powerScrollCooldown = 0;

    public static void registerModKeybingHelper() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            PlayerEntity player = client.player;

            if (player != null) {
                Ability ability = ((PlayerAbilities) player).myneheroes$getFirstAbility();
                if (ability != null) {
                    if (ModKeyBinds.useFirstAbility.isPressed()) {
                        if (!AbilitiesBlockedForScrolling) {
                            if (ability.canHold(player)) {
                                ability.held(player);
                                ClientPlayNetworking.send(new AbilityKeybindPayload(0, 1));
                            }
                            if (!firstWasPressed) {
                                if (!AbilitiesBlockedForScrolling) {
                                    ability.pressed(player);
                                    ClientPlayNetworking.send(new AbilityKeybindPayload(0, 0));
                                }
                            }
                        }
                    } else if (firstWasPressed) {
                        if (AbilitiesBlockedForScrolling) {
                            AbilitiesBlockedForScrolling = false;
                        } else {
                            ability.released(player);
                            ClientPlayNetworking.send(new AbilityKeybindPayload(0, 3));
                        }
                    } else {
                        ability.notPressed(player);
                        ClientPlayNetworking.send(new AbilityKeybindPayload(0, 2));
                    }
                }

                firstWasPressed = ModKeyBinds.useFirstAbility.isPressed();


                ability = ((PlayerAbilities) player).myneheroes$getSecondAbility();
                if (ability != null) {
                    if (ModKeyBinds.useSecondAbility.isPressed()) {
                        if (!AbilitiesBlockedForScrolling) {
                            if (ability.canHold(player)) {
                                ability.held(player);
                                ClientPlayNetworking.send(new AbilityKeybindPayload(1, 1));
                            }
                            if (!secondWasPressed) {
                                if (!AbilitiesBlockedForScrolling) {
                                    ability.pressed(player);
                                    ClientPlayNetworking.send(new AbilityKeybindPayload(1, 0));
                                }
                            }
                        }
                    } else if (secondWasPressed) {
                        if (AbilitiesBlockedForScrolling)  {
                            AbilitiesBlockedForScrolling = false;
                        } else {
                            ability.released(player);
                            ClientPlayNetworking.send(new AbilityKeybindPayload(1, 3));
                        }
                    } else {
                        ability.notPressed(player);
                        ClientPlayNetworking.send(new AbilityKeybindPayload(1, 2));
                    }
                }

                secondWasPressed = ModKeyBinds.useSecondAbility.isPressed();


                ability = ((PlayerAbilities) player).myneheroes$getThirdAbility();
                if (ability != null) {
                    if (ModKeyBinds.useThirdAbility.isPressed()) {
                        if (!AbilitiesBlockedForScrolling) {
                            if (ability.canHold(player)) {
                                ability.held(player);
                                ClientPlayNetworking.send(new AbilityKeybindPayload(2, 1));
                            }
                            if (!thirdWasPressed) {
                                if (!AbilitiesBlockedForScrolling) {
                                    ability.pressed(player);
                                    ClientPlayNetworking.send(new AbilityKeybindPayload(2, 0));
                                }
                            }
                        }
                    } else if (thirdWasPressed) {
                        if (AbilitiesBlockedForScrolling) {
                            AbilitiesBlockedForScrolling = false;
                        } else {
                            ability.released(player);
                            ClientPlayNetworking.send(new AbilityKeybindPayload(2, 3));
                        }
                    } else {
                        ability.notPressed(player);
                        ClientPlayNetworking.send(new AbilityKeybindPayload(2, 2));
                    }
                }

                thirdWasPressed = ModKeyBinds.useThirdAbility.isPressed();


                ability = ((PlayerAbilities) player).myneheroes$getFourthAbility();
                if (ability != null) {
                    if (ModKeyBinds.useFourthAbility.isPressed()) {
                        if (!AbilitiesBlockedForScrolling) {
                            if (ability.canHold(player)) {
                                ability.held(player);
                                ClientPlayNetworking.send(new AbilityKeybindPayload(3, 1));
                            }
                            if (!fourthWasPressed) {
                                if (!AbilitiesBlockedForScrolling) {
                                    ability.pressed(player);
                                    ClientPlayNetworking.send(new AbilityKeybindPayload(3, 0));
                                }
                            }
                        }
                    } else if (fourthWasPressed) {
                        if (AbilitiesBlockedForScrolling) {
                            AbilitiesBlockedForScrolling = false;
                        } else {
                            ability.released(player);
                            ClientPlayNetworking.send(new AbilityKeybindPayload(3, 3));
                        }
                    } else {
                        ability.notPressed(player);
                        ClientPlayNetworking.send(new AbilityKeybindPayload(3, 2));
                    }
                }

                fourthWasPressed = ModKeyBinds.useFourthAbility.isPressed();

                //call the release method for every ability you can't press
                List<Ability> abilities = ((PlayerAbilities) player).myneheroes$getAbilities();
                for (int i = 0; i < abilities.size(); i++) {
                    if (i < ((PlayerAbilities) player).myneheroes$getScrolledAbilityOffset() || i > ((PlayerAbilities) player).myneheroes$getScrolledAbilityOffset() + 3) {
                        ability = abilities.get(i);
                        ability.notPressed(player);
                    }
                }
                if (powerScrollCooldown == 0) {

                    if (ModKeyBinds.powersScrollUp.isPressed()) {
                        ClientScrollData.scrollPowersFurther(client.player);
                        powerScrollCooldown = 5;
                    }
                    if (ModKeyBinds.powersScrollDown.isPressed()) {
                        ClientScrollData.scrollPowersBack(client.player);
                        powerScrollCooldown = 5;
                    }
                } else {
                    powerScrollCooldown--;
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
                        ability.notPressed(player);
                    }
                }
            }
        });

        MouseScrollCallback.EVENT.register((
                (client, mouseX, mouseY, hScroll, vScroll) -> {
                    PlayerEntity player = client.player;
                    if (client.currentScreen == null && player != null && !AbilitiesBlockedForScrolling) {
                        if (ModKeyBinds.useFirstAbility.isPressed()) {
                            Ability ability = ((PlayerAbilities) player).myneheroes$getFirstAbility();
                            if (ability instanceof ScrollableAbility scrollableAbility) {
                                ClientPlayNetworking.send(new AbilityScrollPayload(0, mouseX, mouseY, vScroll, hScroll));
                                if (scrollableAbility.scroll(mouseX, mouseY, vScroll, hScroll)) return true;
                            }
                        }
                        if (ModKeyBinds.useSecondAbility.isPressed()) {
                            Ability ability = ((PlayerAbilities) player).myneheroes$getSecondAbility();
                            if (ability instanceof ScrollableAbility scrollableAbility) {
                                ClientPlayNetworking.send(new AbilityScrollPayload(1, mouseX, mouseY, vScroll, hScroll));
                                if (scrollableAbility.scroll(mouseX, mouseY, vScroll, hScroll)) return true;
                            }
                        }
                        if (ModKeyBinds.useThirdAbility.isPressed()) {
                            Ability ability = ((PlayerAbilities) player).myneheroes$getThirdAbility();
                            if (ability instanceof ScrollableAbility scrollableAbility) {
                                ClientPlayNetworking.send(new AbilityScrollPayload(2, mouseX, mouseY, vScroll, hScroll));
                                if (scrollableAbility.scroll(mouseX, mouseY, vScroll, hScroll)) return true;
                            }
                        }
                        if (ModKeyBinds.useFourthAbility.isPressed()) {
                            Ability ability = ((PlayerAbilities) player).myneheroes$getFourthAbility();
                            if (ability instanceof ScrollableAbility scrollableAbility) {
                                ClientPlayNetworking.send(new AbilityScrollPayload(3, mouseX, mouseY, vScroll, hScroll));
                                return scrollableAbility.scroll(mouseX, mouseY, vScroll, hScroll);
                            }
                        }
                    }
                    return false;
                }
        ));

        MouseScrollCallback.EVENT.register((
                (client, mouseX, mouseY, hScroll, vScroll) -> {
                    PlayerEntity player = client.player;
                    if (client.currentScreen == null && player != null) {
                        if (ModKeyBinds.scrollAbilitiesWithoutUsing.isPressed() || ModKeyBinds.useFirstAbility.isPressed() || ModKeyBinds.useSecondAbility.isPressed() || ModKeyBinds.useThirdAbility.isPressed() || ModKeyBinds.useFourthAbility.isPressed()) {
                            if (client.player != null) {
                                int current = ClientScrollData.getScrolledAbilitiesOffset(client.player);
                                int maxScroll = ClientScrollData.getMaxAbilityScroll(client.player);
                                int target = Math.clamp((int) (current + -vScroll), 0, maxScroll);
                                if (target != current) {
                                    if (!ModKeyBinds.scrollAbilitiesWithoutUsing.isPressed()) {
                                        AbilitiesBlockedForScrolling = true;
                                    }
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
