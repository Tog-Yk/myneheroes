package net.togyk.myneheroes.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.togyk.myneheroes.networking.PlayerAbilityScrollSyncDataPayload;

@Environment(EnvType.CLIENT)
public class ClientScrollData {
    public static int getMaxAbilityScroll(ClientPlayerEntity player){
        return ((PlayerAbilities) player).myneheroes$maxAbilityScroll();
    }
    public static int getScrolledAbilitiesOffset(ClientPlayerEntity player){
        return ((PlayerAbilities) player).myneheroes$getScrolledAbilityOffset();
    }
    public static void setScrolledAbilitiesOffset(ClientPlayerEntity player, int scrolledOffset){
        ((PlayerAbilities) player).myneheroes$setScrolledAbilityOffset(scrolledOffset);
        syncData(player);
    }
    public static boolean canScrollAbilitiesFurther(ClientPlayerEntity player){
        return ((PlayerAbilities) player).myneheroes$canScrollAbilityFurther();
    }

    public static void scrollAbilitiesFurther(ClientPlayerEntity player) {
        ((PlayerAbilities) player).myneheroes$scrollAbilityFurther();
        syncData(player);
    }
    public static void scrollAbilitiesBack(ClientPlayerEntity player) {
        ((PlayerAbilities) player).myneheroes$scrollAbilityBack();
        syncData(player);
    }
    public static int getScrolledPowersOffset(ClientPlayerEntity player) {
        return ((PlayerPowers) player).myneheroes$getScrolledPowerOffset();
    }
    public static void setScrolledPowersOffset(ClientPlayerEntity player, int scrolledOffset){
        ((PlayerPowers) player).myneheroes$setScrolledPowerOffset(scrolledOffset);
        syncData(player);
    }

    public static void scrollPowersFurther(ClientPlayerEntity player) {
        ((PlayerPowers) player).myneheroes$scrollPowerFurther();
        syncData(player);
    }
    public static void scrollPowersBack(ClientPlayerEntity player) {
        ((PlayerPowers) player).myneheroes$scrollPowerBack();
        syncData(player);
    }

    private static void syncData(ClientPlayerEntity player) {
        ClientPlayNetworking.send(new PlayerAbilityScrollSyncDataPayload(((PlayerAbilities) player).myneheroes$getScrolledAbilityOffset(), ((PlayerPowers) player).myneheroes$getScrolledPowerOffset()));
    }
}
