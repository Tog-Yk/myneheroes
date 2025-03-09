package net.togyk.myneheroes.util;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.networking.PlayerPowerSyncDataPayload;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.Powers;

import java.util.List;

public class PowerData {
    public static List<Power> getPowers(PlayerEntity player) {
        return ((PlayerPowers) player).getPowers();
    }

    public static void setPowers(PlayerEntity player, List<Power> powers) {
        if (!player.getWorld().isClient) {
            ((PlayerPowers) player).setPowers(powers);
            syncData((ServerPlayerEntity) player);
        }
    }

    public static void removePower(PlayerEntity player, Power power) {
        if (!player.getWorld().isClient) {
            ((PlayerPowers) player).removePower(power);
            syncData((ServerPlayerEntity) player);
        }
    }

    public static void addPower(PlayerEntity player, Power power) {
        if (!player.getWorld().isClient) {
            ((PlayerPowers) player).addPower(power);
            syncData((ServerPlayerEntity) player);
        }
    }

    public static float getDamageMultiplier(PlayerEntity player) {
        return ((PlayerPowers) player).getDamageMultiplier();
    }

    public static float getResistance(PlayerEntity player) {
        return ((PlayerPowers) player).getResistance();
    }


    private static void syncData(ServerPlayerEntity player) {
        NbtCompound nbt = new NbtCompound();
        player.writeCustomDataToNbt(nbt);
        ServerPlayNetworking.send(player, new PlayerPowerSyncDataPayload(nbt));
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
