package net.togyk.myneheroes.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.togyk.myneheroes.Item.custom.DyeableItem;
import net.togyk.myneheroes.Item.custom.LightableItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Abilities;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.SelectionAbility;
import net.togyk.myneheroes.ability.ToolbeltAbility;
import net.togyk.myneheroes.ability.PassiveSelectionAbility;
import net.togyk.myneheroes.event.MissedInteractionCallback;
import net.togyk.myneheroes.event.MissedSwingCallback;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.Powers;
import net.togyk.myneheroes.util.PlayerAbilities;
import net.togyk.myneheroes.block.entity.ArmorDyeingBlockEntity;
import net.togyk.myneheroes.block.entity.ArmorLightLevelerBlockEntity;
import net.togyk.myneheroes.util.PlayerPowers;

import java.util.ArrayList;
import java.util.List;

public class ModMessages {
    public static final Identifier BLOCKPOS_PACKET_ID = Identifier.of(MyneHeroes.MOD_ID, "block_pos");
    public static final Identifier COLOR_ITEM_PACKET_ID = Identifier.of(MyneHeroes.MOD_ID, "color_item");
    public static final Identifier KEYBIND_PACKET_ID = Identifier.of(MyneHeroes.MOD_ID, "keybind");
    public static final Identifier LIGHT_LEVELER_PACKET_ID = Identifier.of(MyneHeroes.MOD_ID, "light_leveler");
    public static final Identifier MISSED_SWING_PACKET_ID = Identifier.of(MyneHeroes.MOD_ID, "missed_swing");
    public static final Identifier MISSED_INTERACTION_PACKET_ID = Identifier.of(MyneHeroes.MOD_ID, "missed_interaction");
    public static final Identifier PLAYER_POWER_SYNC_DATA_PACKET_ID = Identifier.of(MyneHeroes.MOD_ID, "power_sync_data");
    public static final Identifier PLAYER_ABILITY_SCROLLED_SYNC_DATA_PACKET_ID = Identifier.of(MyneHeroes.MOD_ID, "ability_scrolled_sync_data");

    public static void registerServerMessages() {
        PayloadTypeRegistry.playC2S().register(AbilityKeybindPayload.ID, AbilityKeybindPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(AbilityKeybindPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                // logic for pressing a keybind
                if (payload.abilityIndex() == 0) {
                    if (context.player() != null) {
                        Ability ability = ((PlayerAbilities) context.player()).myneheroes$getFirstAbility();
                        if (ability != null) {
                            switch (payload.type()) {
                                case 0: ability.use(context.player());
                                case 1: ability.usePressed(context.player());
                                case 2: ability.useReleased(context.player());
                            }
                        }
                    }
                } else if (payload.abilityIndex() == 1) {
                    if (context.player() != null) {
                        Ability ability = ((PlayerAbilities) context.player()).myneheroes$getSecondAbility();
                        if (ability != null) {
                            switch (payload.type()) {
                                case 0: ability.use(context.player());
                                case 1: ability.usePressed(context.player());
                                case 2: ability.useReleased(context.player());
                            }
                        }
                    }
                } else if (payload.abilityIndex() == 2) {
                    if (context.player() != null) {
                        Ability ability = ((PlayerAbilities) context.player()).myneheroes$getThirdAbility();
                        if (ability != null) {
                            switch (payload.type()) {
                                case 0: ability.use(context.player());
                                case 1: ability.usePressed(context.player());
                                case 2: ability.useReleased(context.player());
                            }
                        }
                    }
                } else if (payload.abilityIndex() == 3) {
                    if (context.player() != null) {
                        Ability ability = ((PlayerAbilities) context.player()).myneheroes$getFourthAbility();
                        if (ability != null) {
                            switch (payload.type()) {
                                case 0: ability.use(context.player());
                                case 1: ability.usePressed(context.player());
                                case 2: ability.useReleased(context.player());
                            }
                        }
                    }
                }
            });
        });

        PayloadTypeRegistry.playC2S().register(ColorItemPayload.ID, ColorItemPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ColorItemPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                // logic for dyeing an item
                World world = context.player().getWorld();
                if (world != null) {
                    BlockEntity blockEntity = world.getBlockEntity(payload.pos());
                    if (blockEntity instanceof ArmorDyeingBlockEntity armorDyeingBlockEntity) {
                        SimpleInventory inventory = armorDyeingBlockEntity.getInput();
                        ItemStack stack = inventory.getStack(0);

                        SimpleInventory fuel = armorDyeingBlockEntity.getFuel();

                        if (stack.getItem() instanceof DyeableItem dyeableItem) {
                            if (payload.setDefault()) {
                                dyeableItem.setDefaultColor(stack, payload.index());
                            } else if (!fuel.isEmpty()) {
                                dyeableItem.setColor(stack, payload.index(), payload.color());

                                fuel.getStack(0).decrement(1);
                                fuel.markDirty();
                            }
                            inventory.markDirty();
                        }
                    }
                }
            });
        });

        PayloadTypeRegistry.playC2S().register(LightLevelerPayload.ID, LightLevelerPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(LightLevelerPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                // logic for dyeing an item
                World world = context.player().getWorld();
                if (world != null) {
                    BlockEntity blockEntity = world.getBlockEntity(payload.pos());
                    if (blockEntity instanceof ArmorLightLevelerBlockEntity armorLightLevelerBlockEntity) {
                        SimpleInventory inventory = armorLightLevelerBlockEntity.getInventory();
                        ItemStack stack = inventory.getStack(0);
                        if (stack.getItem() instanceof LightableItem lightableItem) {
                            lightableItem.setLightLevel(stack, payload.index(), payload.lightLevel());
                            inventory.markDirty();
                        }
                    }
                }
            });
        });

        PayloadTypeRegistry.playC2S().register(PlayerSwingPayload.ID, PlayerSwingPayload.CODEC);


        ServerPlayNetworking.registerGlobalReceiver(PlayerSwingPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                MissedSwingCallback.EVENT.invoker().onMissedSwing(context.player(), Hand.MAIN_HAND);
            });
        });

        PayloadTypeRegistry.playC2S().register(PlayerInteractionPayload.ID, PlayerInteractionPayload.CODEC);


        ServerPlayNetworking.registerGlobalReceiver(PlayerInteractionPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                MissedInteractionCallback.EVENT.invoker().onMissedInteraction(context.player());
            });
        });


        PayloadTypeRegistry.playC2S().register(PlayerAbilityScrollSyncDataPayload.ID, PlayerAbilityScrollSyncDataPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(PlayerAbilityScrollSyncDataPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                ((PlayerAbilities) context.player()).myneheroes$setScrolledAbilityOffset(payload.scrolledAbilities());
                ((PlayerPowers) context.player()).myneheroes$setScrolledPowerOffset(payload.scrolledPowers());
            });
        });



        PayloadTypeRegistry.playS2C().register(PlayerAbilityScrollSyncDataPayload.ID, PlayerAbilityScrollSyncDataPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(PlayerPowerSyncDataPayload.ID, PlayerPowerSyncDataPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(UseSelectedAbilityPayload.ID, UseSelectedAbilityPayload.CODEC);


        ServerPlayNetworking.registerGlobalReceiver(UseSelectedAbilityPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                List<Ability> abilities = ((PlayerAbilities) context.player()).myneheroes$getFilteredAbilities();
                Ability ability = abilities.get(payload.abilityIndex());
                if (Abilities.contains(ability.getId()) && ability instanceof SelectionAbility selectionAbility) {
                    selectionAbility.UseAbility(context.player(), payload.index());
                }
            });
        });

        PayloadTypeRegistry.playC2S().register(SwapFromToolbeltPayload.ID, SwapFromToolbeltPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SwapFromToolbeltPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                List<Ability> abilities = ((PlayerAbilities) context.player()).myneheroes$getFilteredAbilities();
                Ability ability = abilities.get(payload.toolbeltIndex());
                if (Abilities.contains(ability.getId()) && ability instanceof ToolbeltAbility toolbelt) {
                    toolbelt.swapItem(context.player(), payload.index());
                }
            });
        });

        PayloadTypeRegistry.playC2S().register(SelectPassivePayload.ID, SelectPassivePayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SelectPassivePayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                List<Ability> abilities = ((PlayerAbilities) context.player()).myneheroes$getFilteredAbilities();
                Ability ability = abilities.get(payload.abilityIndex());
                if (Abilities.contains(ability.getId()) && ability instanceof PassiveSelectionAbility passiveSelectionAbility) {
                    passiveSelectionAbility.setSelectedPassive(payload.index());
                }
            });
        });
    }

    public static void registerClientMessages() {
        ClientPlayNetworking.registerGlobalReceiver(PlayerPowerSyncDataPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                List<Power> powers = new ArrayList<>();
                if (payload.nbt().contains(MyneHeroes.MOD_ID)) {
                    NbtCompound modNbt = payload.nbt().getCompound(MyneHeroes.MOD_ID);
                    if (modNbt.contains("powers")) {
                        NbtList powerNbt = modNbt.getList("powers", NbtElement.COMPOUND_TYPE);
                        for (NbtElement nbtElement : powerNbt) {
                            if (nbtElement instanceof NbtCompound nbtCompound) {
                                Identifier powerId = Identifier.of(nbtCompound.getString("id"));
                                Power power = Powers.get(powerId);
                                power.readNbt(nbtCompound);
                                powers.add(power);
                            }
                        }
                    }
                }
                ((PlayerPowers) context.player()).myneheroes$setPowers(powers);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(PlayerAbilityScrollSyncDataPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                ((PlayerAbilities) context.player()).myneheroes$setScrolledAbilityOffset(payload.scrolledAbilities());
                ((PlayerPowers) context.player()).myneheroes$setScrolledPowerOffset(payload.scrolledPowers());
            });
        });
    }
}
