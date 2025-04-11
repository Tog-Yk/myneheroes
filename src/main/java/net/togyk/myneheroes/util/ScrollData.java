package net.togyk.myneheroes.util;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.togyk.myneheroes.networking.PlayerAbilityScrollSyncDataPayload;

public class ScrollData {
    public static int getScrolledAbilitiesOffset(PlayerEntity player){
        return ((PlayerAbilities) player).getScrolledOffset();
    }
    public static void setScrolledAbilitiesOffset(PlayerEntity player, int scrolledOffset){
        ((PlayerAbilities) player).setScrolledOffset(scrolledOffset);
        if (player instanceof ClientPlayerEntity clientPlayer) {
            syncData(clientPlayer);
        }
    }
    public static boolean canScrollAbilitiesFurther(PlayerEntity player){
        return ((PlayerAbilities) player).canScrollFurther();

    }

    public static void scrollAbilitiesFurther(PlayerEntity player) {
        if (player.getWorld().isClient) {
            ((PlayerAbilities) player).scrollFurther();
            syncData((ClientPlayerEntity) player);
        }
    }
    public static void scrollAbilitiesBack(PlayerEntity player) {
        if (player.getWorld().isClient) {
            ((PlayerAbilities) player).scrollBack();
            syncData((ClientPlayerEntity) player);
        }
    }
    public static int getScrolledPowersOffset(PlayerEntity player){
        return ((PlayerAbilities) player).getScrolledOffset();
    }
    public static void setScrolledPowersOffset(PlayerEntity player, int scrolledOffset){
        ((PlayerAbilities) player).setScrolledOffset(scrolledOffset);
        if (player instanceof ClientPlayerEntity clientPlayer) {
            syncData(clientPlayer);
        }
    }

    public static void scrollPowersFurther(PlayerEntity player) {
        if (player.getWorld().isClient) {
            ((PlayerPowers) player).scrollFurther();
            syncData((ClientPlayerEntity) player);
        }
    }
    public static void scrollPowersBack(PlayerEntity player) {
        if (player.getWorld().isClient) {
            ((PlayerPowers) player).scrollBack();
            syncData((ClientPlayerEntity) player);
        }
    }

    private static void syncData(ClientPlayerEntity player) {
        ClientPlayNetworking.send(new PlayerAbilityScrollSyncDataPayload(((PlayerAbilities) player).getScrolledOffset(), ((PlayerPowers) player).getScrolledOffset()));
    }
}
