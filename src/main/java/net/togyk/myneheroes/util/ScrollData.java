package net.togyk.myneheroes.util;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.togyk.myneheroes.networking.PlayerAbilityScrollSyncDataPayload;

public class ScrollData {
    public static int getScrolledAbilitiesOffset(PlayerEntity player){
        return ((PlayerAbilities) player).getScrolledAbilityOffset();
    }
    public static void setScrolledAbilitiesOffset(PlayerEntity player, int scrolledOffset){
        ((PlayerAbilities) player).setScrolledAbilityOffset(scrolledOffset);
        if (player instanceof ClientPlayerEntity clientPlayer) {
            syncData(clientPlayer);
        }
    }
    public static boolean canScrollAbilitiesFurther(PlayerEntity player){
        return ((PlayerAbilities) player).canScrollAbilityFurther();

    }

    public static void scrollAbilitiesFurther(PlayerEntity player) {
        if (player.getWorld().isClient) {
            ((PlayerAbilities) player).scrollAbilityFurther();
            syncData((ClientPlayerEntity) player);
        }
    }
    public static void scrollAbilitiesBack(PlayerEntity player) {
        if (player.getWorld().isClient) {
            ((PlayerAbilities) player).scrollAbilityBack();
            syncData((ClientPlayerEntity) player);
        }
    }
    public static int getScrolledPowersOffset(PlayerEntity player){
        return ((PlayerPowers) player).getScrolledPowerOffset();
    }
    public static void setScrolledPowersOffset(PlayerEntity player, int scrolledOffset){
        ((PlayerPowers) player).setScrolledPowerOffset(scrolledOffset);
        if (player instanceof ClientPlayerEntity clientPlayer) {
            syncData(clientPlayer);
        }
    }

    public static void scrollPowersFurther(PlayerEntity player) {
        if (player.getWorld().isClient) {
            ((PlayerPowers) player).scrollPowerFurther();
            syncData((ClientPlayerEntity) player);
        }
    }
    public static void scrollPowersBack(PlayerEntity player) {
        if (player.getWorld().isClient) {
            ((PlayerPowers) player).scrollPowerBack();
            syncData((ClientPlayerEntity) player);
        }
    }

    private static void syncData(ClientPlayerEntity player) {
        ClientPlayNetworking.send(new PlayerAbilityScrollSyncDataPayload(((PlayerAbilities) player).getScrolledAbilityOffset(), ((PlayerPowers) player).getScrolledPowerOffset()));
    }
}
