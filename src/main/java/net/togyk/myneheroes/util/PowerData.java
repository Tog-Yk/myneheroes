package net.togyk.myneheroes.util;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.advancement.criterion.ModCriteria;
import net.togyk.myneheroes.gamerule.ModGamerules;
import net.togyk.myneheroes.networking.PlayerPowerSyncDataPayload;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.Powers;

import java.util.List;

public class PowerData {
    public static List<Power> getPowers(PlayerEntity player) {
        if (!player.getWorld().isClient) {
            syncData((ServerPlayerEntity) player);
        }
        return ((PlayerPowers) player).myneheroes$getPowers();
    }

    public static List<Power> getPowersWithoutSyncing(PlayerEntity player) {
        return ((PlayerPowers) player).myneheroes$getPowers();
    }

    public static void setPowers(PlayerEntity player, List<Power> powers) {
        ((PlayerPowers) player).myneheroes$setPowers(powers);
        if (!player.getWorld().isClient) {
            syncData((ServerPlayerEntity) player);
        }
    }

    public static void removePower(PlayerEntity player, Power power) {
        if (!player.getWorld().isClient) {
            ((PlayerPowers) player).myneheroes$removePower(power);
            syncData((ServerPlayerEntity) player);
        }
    }

    public static void addPower(PlayerEntity player, Power power) {
        if (!player.getWorld().isClient) {
            ((PlayerPowers) player).myneheroes$addPower(power);
            syncData((ServerPlayerEntity) player);
        }
    }

    public static void addUniquePowerToLimit(PlayerEntity player, Power power) {
        if (!player.getWorld().isClient) {
            List<Power> currentPowers = PowerData.getPowers(player);
            if (player.getWorld().getGameRules().getBoolean(ModGamerules.GIVE_POWERS_ABOVE_LIMIT) || currentPowers.size() < player.getWorld().getGameRules().getInt(ModGamerules.POWER_LIMIT)) {
                if (!currentPowers.stream().map(Power::getId).toList().contains(power.id)) {
                    ((PlayerPowers) player).myneheroes$addPower(power);
                }
            }
            syncData((ServerPlayerEntity) player);
        }
    }

    public static double getDamageMultiplier(PlayerEntity player) {
        return ((PlayerPowers) player).myneheroes$getDamageMultiplier();
    }


    private static void syncData(ServerPlayerEntity player) {
        NbtCompound nbt = new NbtCompound();
        player.writeCustomDataToNbt(nbt);
        for (ServerPlayerEntity player1 : player.getServer().getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player1, new PlayerPowerSyncDataPayload(nbt, player.getUuid()));
        }
        for (Power power : ((PlayerPowers) player).myneheroes$getPowers()) {
            ModCriteria.POWERS_CHANGED_CRITERION.trigger(player, power.getId());
        }
    }

    public static Power nbtToPower(NbtCompound nbt) {
        Identifier powerId = Identifier.of(nbt.getString("id"));
        Power power = Powers.get(powerId);
        if (power != null) {
            power.readNbt(nbt);
        }
        return power;
    }
}
