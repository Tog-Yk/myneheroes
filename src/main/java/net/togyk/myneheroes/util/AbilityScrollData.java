package net.togyk.myneheroes.util;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.togyk.myneheroes.networking.PlayerAbilityScrollSyncDataPayload;

public class AbilityScrollData {
    public static int getScrolledOffset(PlayerEntity player){
        return ((PlayerAbilities) player).getScrolledOffset();
    }
    public static void setScrolledOffset(PlayerEntity player, int scrolledOffset){
        ((PlayerAbilities) player).setScrolledOffset(scrolledOffset);
        if (player instanceof ClientPlayerEntity clientPlayer) {
            syncData(clientPlayer);
        }
    }
    public static boolean canScrollFurther(PlayerEntity player){
        return ((PlayerAbilities) player).canScrollFurther();

    }
    public static int maxScroll(PlayerEntity player) {
        return ((PlayerAbilities) player).maxScroll();
    }
    public static void scrollFurther(PlayerEntity player) {
        if (player.getWorld().isClient) {
            ((PlayerAbilities) player).scrollFurther();
            syncData((ClientPlayerEntity) player);
        }
    }
    public static void scrollBack(PlayerEntity player) {
        if (player.getWorld().isClient) {
            ((PlayerAbilities) player).scrollBack();
            syncData((ClientPlayerEntity) player);
        }
    }

    private static void syncData(ClientPlayerEntity player) {
        ClientPlayNetworking.send(new PlayerAbilityScrollSyncDataPayload(((PlayerAbilities) player).getScrolledOffset()));
    }
}
