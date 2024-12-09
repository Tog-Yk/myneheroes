package net.togyk.myneheroes.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.AdvancedArmorItem;
import net.togyk.myneheroes.Item.custom.ReactorItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.util.PlayerAbilities;

public class ModMessages {
    public static final Identifier KEYBIND_PACKET_ID = Identifier.of(MyneHeroes.MOD_ID, "keybind");

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
    }

    public static void registerClientMessages() {
    }
}
