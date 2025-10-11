package net.togyk.myneheroes.block.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.togyk.myneheroes.effect.ModEffects;
import net.togyk.myneheroes.gamerule.ModGamerules;
import net.togyk.myneheroes.persistent_data.ModPersistentData;
import net.togyk.myneheroes.persistent_data.TimeNearKryptoniteData;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.Powers;
import net.togyk.myneheroes.util.PowerData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class KryptoniteRadiationBlock extends RadiationBlock {
    public KryptoniteRadiationBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        Box box = new Box(pos).expand(7);
        List<PlayerEntity> players = world.getEntitiesByClass(PlayerEntity.class, box, p -> true);
        if (!players.isEmpty()) {
            TimeNearKryptoniteData timeData = ModPersistentData.getTimeNearKryptonite(world.getServer().getOverworld());
            Map<UUID, Long> timeMap = timeData.getTimeMap();

            //filter out all the players that are eligible
            List<UUID> eligiblePlayers = new ArrayList<>();
            for (PlayerEntity player : players) {
                boolean isExposedToSun = world.isDay() && world.isSkyVisible(player.getBlockPos());
                List<Power> currentPowers = PowerData.getPowers(player);
                if (hasKryptoniteDampenedPower(currentPowers)) {
                    if (player.hasStatusEffect(ModEffects.KRYPTONITE_POISON)) {
                        StatusEffectInstance effect = player.getStatusEffect(ModEffects.KRYPTONITE_POISON);
                        if (effect != null) {
                            int duration = effect.getDuration();
                            if ((duration + 1) % 24 == 0) {
                                player.addStatusEffect(new StatusEffectInstance(
                                        ModEffects.KRYPTONITE_POISON, 191, 0, true, true
                                ));
                            }
                        }
                    } else {
                        player.addStatusEffect(new StatusEffectInstance(
                                ModEffects.KRYPTONITE_POISON, 191, 0, true, true
                        ));
                    }
                    timeMap.remove(player.getUuid());
                } else if (!isExposedToSun) {
                    // Check if the player has reached the limit of powers
                    if (player.getWorld().getGameRules().getBoolean(ModGamerules.GIVE_POWERS_ABOVE_LIMIT) || PowerData.getPowers(player).size() < player.getWorld().getGameRules().getInt(ModGamerules.POWER_LIMIT)) {
                        eligiblePlayers.add(player.getUuid());
                    }
                }
            }

            //create/increase the players time near the block and check if they should become a kryptonian
            for (UUID id : eligiblePlayers) {
                if (timeMap.getOrDefault(id, 0L) >= 20L * 60 * 480) {
                    Power kryptonianPower = Powers.KRYPTONIAN.copy();
                    PowerData.addUniquePowerToLimit(world.getPlayerByUuid(id), kryptonianPower);
                    timeMap.remove(id);
                }
                timeMap.put(id, timeMap.getOrDefault(id, 0L) + 1);
            }

            timeData.markDirty();
        }

        // Re-schedule tick
        if (state.get(TICKING)) {
            world.scheduleBlockTick(pos, this, 24);
        }
    }

    private boolean hasKryptoniteDampenedPower(List<Power> powers) {
        for (Power power : powers) {
            if (power.isDampenedByKryptonite()) {
                return true;
            }
        }
        return false;
    }
}
