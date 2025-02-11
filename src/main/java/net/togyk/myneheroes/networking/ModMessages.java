package net.togyk.myneheroes.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.togyk.myneheroes.Item.custom.DyeableAdvancedArmorItem;
import net.togyk.myneheroes.Item.custom.ReactorItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.event.MissedSwingCallback;
import net.togyk.myneheroes.util.PlayerAbilities;
import net.togyk.myneheroes.block.entity.ArmorDyeingBlockEntity;
import net.togyk.myneheroes.block.entity.ArmorLightLevelerBlockEntity;

public class ModMessages {
    public static final Identifier BLOCKPOS_PACKET_ID = Identifier.of(MyneHeroes.MOD_ID, "block_pos");
    public static final Identifier COLOR_ITEM_PACKET_ID = Identifier.of(MyneHeroes.MOD_ID, "color_item");
    public static final Identifier KEYBIND_PACKET_ID = Identifier.of(MyneHeroes.MOD_ID, "keybind");
    public static final Identifier LIGHT_LEVELER_PACKET_ID = Identifier.of(MyneHeroes.MOD_ID, "light_leveler");
    public static final Identifier MISSED_SWING_PACKET_ID = Identifier.of(MyneHeroes.MOD_ID, "missed_swing");

    public static void registerServerMessages() {
        PayloadTypeRegistry.playC2S().register(KeybindPayload.ID, KeybindPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(KeybindPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                // logic for pressing a keybind
                if (payload.integer() == 0) {
                    if (MinecraftClient.getInstance().player != null) {
                        Ability firstAbility = ((PlayerAbilities) context.player()).getFirstAbility();
                        firstAbility.serverUse(context.player());
                    }
                } else if (payload.integer() == 1) {
                    if (MinecraftClient.getInstance().player != null) {
                        Ability firstAbility = ((PlayerAbilities) context.player()).getSecondAbility();
                        firstAbility.serverUse(context.player());
                    }
                }else if (payload.integer() == 2) {
                    if (MinecraftClient.getInstance().player != null) {
                        Ability firstAbility = ((PlayerAbilities) context.player()).getThirdAbility();
                        firstAbility.serverUse(context.player());
                    }
                }else if (payload.integer() == 3) {
                    if (MinecraftClient.getInstance().player != null) {
                        Ability firstAbility = ((PlayerAbilities) context.player()).getFourthAbility();
                        firstAbility.serverUse(context.player());
                    }
                } else if (payload.integer() == 5) {
                    ServerPlayerEntity player = context.player();
                    if (player != null) {
                        ItemStack reactorStack = MyneHeroes.getReactorItemClass(player);
                        if (reactorStack != ItemStack.EMPTY && reactorStack.getItem() instanceof ReactorItem reactorItem) {
                            int currentFuel = reactorItem.getStoredFuelOrDefault(reactorStack,0);
                            reactorItem.setStoredFuel(reactorStack,currentFuel+10);
                        }
                    }
                }
                //
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
                        SimpleInventory inventory = armorDyeingBlockEntity.getInventory();
                        ItemStack stack = inventory.getStack(0);
                        if (stack.getItem() instanceof DyeableAdvancedArmorItem armorItem) {
                            armorItem.setColor(stack, payload.index(), payload.color());
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
                        if (stack.getItem() instanceof DyeableAdvancedArmorItem armorItem) {
                            armorItem.setLightLevel(stack, payload.index(), payload.lightLevel());
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
    }

    public static void registerClientMessages() {
    }
}
