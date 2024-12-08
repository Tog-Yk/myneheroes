package net.togyk.myneheroes.keybind;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.togyk.myneheroes.Item.custom.AdvancedArmorItem;
import net.togyk.myneheroes.networking.KeybindPayload;

public class ModKeybindingHelper {
    public static void registerModKeybingHelper() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (ModKeyBindings.toggleHudKeybinding.wasPressed()) {
                if (MinecraftClient.getInstance().player != null) {
                    ItemStack helmetStack = MinecraftClient.getInstance().player.getEquippedStack(EquipmentSlot.HEAD);
                    if (helmetStack != null && helmetStack.getItem() instanceof AdvancedArmorItem advancedArmorItem) {
                        advancedArmorItem.ToggleHud(helmetStack);
                    }
                }
            }
            // Access the Use Item/Place Block keybind
            if (ModKeyBindings.shootRepolser.wasPressed()) {
                if (MinecraftClient.getInstance().player != null){
                    ClientPlayNetworking.send(new KeybindPayload(1));
                }
            }
        });
    }
}
