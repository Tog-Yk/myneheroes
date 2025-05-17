package net.togyk.myneheroes.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.togyk.myneheroes.Item.custom.ThrowableItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.entity.MeteorEntity;
import net.togyk.myneheroes.gamerule.ModGamerules;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.Powers;
import net.togyk.myneheroes.util.PowerData;

import java.util.List;
import java.util.Random;

public class ModEvents {

    public static void registerEvents() {
        MyneHeroes.LOGGER.info("registering Events for" + MyneHeroes.MOD_ID);

        MissedSwingCallback.EVENT.register((PlayerEntity player, Hand hand) -> {
            // Create an arrow entity. (This example does not consume inventory arrows.)
            ItemStack stack = player.getStackInHand(hand);
            if (player.getWorld() != null && stack.getItem() instanceof ThrowableItem throwableItem) {
                throwableItem.Throw(player.getWorld(), player, hand);
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.getOverworld() != null && server.getOverworld().getGameRules().getBoolean(ModGamerules.DO_METEOR_SPAWN)) {
                Random random = new Random();
                float randomF = random.nextFloat();
                if (randomF < 0.04 / 20 / 60) {
                    ServerWorld world = server.getOverworld();
                    List<ServerPlayerEntity> players = world.getPlayers();

                    if (!players.isEmpty()) {
                        ServerPlayerEntity player = players.get(random.nextInt(players.size()));
                        BlockPos playerPos = player.getBlockPos();

                        BlockPos pos = playerPos.add(random.nextInt(-300, 300), 0, random.nextInt(-300, 300));
                        MeteorEntity meteor = new MeteorEntity(world);
                        meteor.setPosition(Vec3d.of(pos.withY(620)));
                        meteor.setVelocity(random.nextDouble(-1, 1), 0, random.nextDouble(-1, 1), 3.0F, 0);

                        world.spawnEntity(meteor);
                    } else {
                        Vec3i pos = new Vec3i(random.nextInt(-1000, 100), 620, random.nextInt(-1000, 1000));
                        MeteorEntity meteor = new MeteorEntity(world);
                        meteor.setPosition(Vec3d.of(pos));
                        meteor.setVelocity(random.nextDouble(-1, 1), 0, random.nextDouble(-1 ,1), 3.0F, 0);

                        world.spawnEntity(meteor);
                    }
                }
            }
        });

        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, aliveAfterTeleport) -> {
            if (aliveAfterTeleport || newPlayer.getServer().getOverworld().getGameRules().getBoolean(ModGamerules.KEEP_POWERS)) {
                List<Power> oldPowers = PowerData.getPowers(oldPlayer);
                PowerData.setPowers(newPlayer, oldPowers);
            }
        });

        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, originalAmount, actualAmount, blocked) -> {
            if (entity instanceof PlayerEntity player && !blocked) {
                // Check if the damage source is lightning
                if (source.isOf(DamageTypes.LIGHTNING_BOLT)) {
                    // Check if the player has the Speed effect
                    StatusEffectInstance speedEffect = player.getStatusEffect(StatusEffects.SPEED);
                    if (speedEffect != null && speedEffect.getDuration() > 0) {
                        Power speedster = Powers.SPEEDSTER.copy();
                        List<Power> currentPowers = PowerData.getPowers(player);
                        if (!currentPowers.stream().map(Power::getId).toList().contains(speedster.id)) {
                            PowerData.addPower(player, speedster);
                        }
                    }
                }
            }
        });
    }
}
