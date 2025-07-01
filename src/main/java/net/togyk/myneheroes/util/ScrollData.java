package net.togyk.myneheroes.util;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.networking.PlayerAbilityScrollSyncDataPayload;

public class ScrollData {
    public static int getScrolledAbilitiesOffset(PlayerEntity player){
        return ((PlayerAbilities) player).myneheroes$getScrolledAbilityOffset();
    }
    public static void setScrolledAbilitiesOffset(PlayerEntity player, int scrolledOffset){
        ((PlayerAbilities) player).myneheroes$setScrolledAbilityOffset(scrolledOffset);
        syncData(player);
    }
    public static boolean canScrollAbilitiesFurther(PlayerEntity player){
        return ((PlayerAbilities) player).myneheroes$canScrollAbilityFurther();
    }

    public static void scrollAbilitiesFurther(PlayerEntity player) {
        ((PlayerAbilities) player).myneheroes$scrollAbilityFurther();
        syncData(player);
    }
    public static void scrollAbilitiesBack(PlayerEntity player) {
        ((PlayerAbilities) player).myneheroes$scrollAbilityBack();
        syncData(player);
    }
    public static int getScrolledPowersOffset(PlayerEntity player) {
        return ((PlayerPowers) player).myneheroes$getScrolledPowerOffset();
    }
    public static void setScrolledPowersOffset(PlayerEntity player, int scrolledOffset){
        ((PlayerPowers) player).myneheroes$setScrolledPowerOffset(scrolledOffset);
        syncData(player);
    }

    public static void scrollPowersFurther(PlayerEntity player) {
        ((PlayerPowers) player).myneheroes$scrollPowerFurther();
        syncData(player);
    }
    public static void scrollPowersBack(PlayerEntity player) {
        ((PlayerPowers) player).myneheroes$scrollPowerBack();
        syncData(player);
    }

    private static void syncData(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, new PlayerAbilityScrollSyncDataPayload(((PlayerAbilities) serverPlayer).myneheroes$getScrolledAbilityOffset(), ((PlayerPowers) serverPlayer).myneheroes$getScrolledPowerOffset()));
        } else {
            MyneHeroes.LOGGER.info("AAAAA");
        }
    }
}
