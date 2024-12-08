package net.togyk.myneheroes.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.AdvancedArmorItem;
import net.togyk.myneheroes.Item.custom.ReactorItem;
import net.togyk.myneheroes.MyneHeroes;

public class ModMessages {
    public static final Identifier KEYBIND_PACKET_ID = Identifier.of(MyneHeroes.MOD_ID, "keybind");

    public static void registerServerMessages() {
        PayloadTypeRegistry.playC2S().register(KeybindPayload.ID, KeybindPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(KeybindPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                // logic for pressing a keybind
                if (payload.integer() == 1) {
                    ServerPlayerEntity player = context.player();
                    if (player != null) {
                        ItemStack chestplateStack = player.getEquippedStack(EquipmentSlot.CHEST);
                        if (chestplateStack != null && chestplateStack.getItem() instanceof AdvancedArmorItem advancedArmorItem) {
                            ItemStack reactorItemStack = ItemStack.EMPTY;
                            PlayerInventory inventory = player.getInventory();
                            for (int i = 0; i < inventory.size(); i++) {
                                ItemStack stack = inventory.getStack(i);
                                if (stack.getItem() instanceof ReactorItem) {
                                    reactorItemStack = stack;
                                    break;
                                }
                            }
                            if (!(reactorItemStack == ItemStack.EMPTY)) {
                                advancedArmorItem.ShootRepolserAndReturnLazarEntity(player, reactorItemStack);
                            }
                        }
                    }
                } else if (payload.integer() == 2) {

                }
                //
            });
        });
    }

    public static void registerClientMessages() {
    }
}
