package net.togyk.myneheroes.util;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.togyk.myneheroes.networking.PlayerAbilityScrollSyncDataPayload;

public class ScrollData {
    public static int getScrolledAbilitiesOffset(PlayerEntity player){
        return ((PlayerAbilities) player).getScrolledAbilityOffset();
    }
    public static void setScrolledAbilitiesOffset(PlayerEntity player, int scrolledOffset){
        ((PlayerAbilities) player).setScrolledAbilityOffset(scrolledOffset);
        syncData(player);
    }
    public static boolean canScrollAbilitiesFurther(PlayerEntity player){
        return ((PlayerAbilities) player).canScrollAbilityFurther();
    }

    public static void scrollAbilitiesFurther(PlayerEntity player) {
        ((PlayerAbilities) player).scrollAbilityFurther();
        syncData(player);
    }
    public static void scrollAbilitiesBack(PlayerEntity player) {
        ((PlayerAbilities) player).scrollAbilityBack();
        syncData(player);
    }
    public static int getScrolledPowersOffset(PlayerEntity player) {
        return ((PlayerPowers) player).getScrolledPowerOffset();
    }
    public static void setScrolledPowersOffset(PlayerEntity player, int scrolledOffset){
        ((PlayerPowers) player).setScrolledPowerOffset(scrolledOffset);
        syncData(player);
    }

    public static void scrollPowersFurther(PlayerEntity player) {
        ((PlayerPowers) player).scrollPowerFurther();
        syncData(player);
    }
    public static void scrollPowersBack(PlayerEntity player) {
        ((PlayerPowers) player).scrollPowerBack();
        syncData(player);
    }

    private static void syncData(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, new PlayerAbilityScrollSyncDataPayload(((PlayerAbilities) serverPlayer).getScrolledAbilityOffset(), ((PlayerPowers) serverPlayer).getScrolledPowerOffset()));
        } else if (player instanceof ClientPlayerEntity clientPlayer) {
            ClientPlayNetworking.send(new PlayerAbilityScrollSyncDataPayload(((PlayerAbilities) clientPlayer).getScrolledAbilityOffset(), ((PlayerPowers) clientPlayer).getScrolledPowerOffset()));
        }
    }
}
