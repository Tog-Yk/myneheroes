package net.togyk.myneheroes.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.togyk.myneheroes.Item.custom.AdvancedArmorItem;
import net.togyk.myneheroes.Item.custom.DyeableAdvancedArmorItem;
import net.togyk.myneheroes.Item.custom.ReactorItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.block.entity.ArmorDyeingBlockEntity;
import net.togyk.myneheroes.block.entity.ArmorLightLevelerBlockEntity;

public class ModMessages {
    public static final Identifier BLOCKPOS_PACKET_ID = Identifier.of(MyneHeroes.MOD_ID, "block_pos");
    public static final Identifier COLOR_ITEM_PACKET_ID = Identifier.of(MyneHeroes.MOD_ID, "color_item");
    public static final Identifier KEYBIND_PACKET_ID = Identifier.of(MyneHeroes.MOD_ID, "keybind");
    public static final Identifier LIGHT_LEVELER_PACKET_ID = Identifier.of(MyneHeroes.MOD_ID, "light_leveler");

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
                            ItemStack reactorItemStack = MyneHeroes.getReactorItemClass(player);
                            PlayerInventory inventory = player.getInventory();
                            if (!(reactorItemStack == ItemStack.EMPTY)) {
                                advancedArmorItem.shootRepolserAndReturnLazarEntity(player, reactorItemStack);
                            }
                        }
                    }
                } else if (payload.integer() == 2) {
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


    }

    public static void registerClientMessages() {
    }
}
