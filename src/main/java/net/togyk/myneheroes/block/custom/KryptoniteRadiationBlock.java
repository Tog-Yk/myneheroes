package net.togyk.myneheroes.block.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import net.togyk.myneheroes.effect.ModEffects;
import net.togyk.myneheroes.gamerule.ModGamerules;
import net.togyk.myneheroes.persistent_data.ModPersistentData;
import net.togyk.myneheroes.persistent_data.TimeNearKryptoniteData;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.Powers;
import net.togyk.myneheroes.util.PowerData;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class KryptoniteRadiationBlock extends RadiationBlock {
    public KryptoniteRadiationBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityFound(LivingEntity entity) {
        if (entity instanceof PlayerEntity player) {
            if (player.getWorld().getGameRules().getBoolean(ModGamerules.GIVE_POWERS_ABOVE_LIMIT) || PowerData.getPowers(player).size() < player.getWorld().getGameRules().getInt(ModGamerules.POWER_LIMIT)) {
                World world = player.getWorld();

                TimeNearKryptoniteData timeData = ModPersistentData.getTimeNearKryptonite(world.getServer().getOverworld());
                Map<UUID, Long> timeMap = timeData.getTimeMap();

                UUID id = player.getUuid();

                boolean isExposedToSun = world.isDay() && world.isSkyVisible(player.getBlockPos());
                List<Power> currentPowers = PowerData.getPowers(player);
                if (hasKryptoniteDampenedPower(currentPowers)) {
                    timeMap.remove(player);
                } else if (!isExposedToSun) {

                    if (timeMap.getOrDefault(id, 0L) >= 20L * 60 * 480) {
                        Power kryptonianPower = Powers.KRYPTONIAN.copy();
                        PowerData.addUniquePowerToLimit(world.getPlayerByUuid(id), kryptonianPower);
                        timeMap.remove(id);
                    } else {
                        timeMap.put(id, timeMap.getOrDefault(id, 0L) + 24);
                    }
                }
            }
        }
    }

    @Override
    public boolean canGiveEffect(LivingEntity entity) {
        return entity instanceof PlayerEntity player && hasKryptoniteDampenedPower(PowerData.getPowers(player));
    }

    @Override
    public RegistryEntry<StatusEffect> getEffect() {
        return ModEffects.KRYPTONITE_POISON;
    }

    @Override
    public int getRange() {
        return 7;
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
