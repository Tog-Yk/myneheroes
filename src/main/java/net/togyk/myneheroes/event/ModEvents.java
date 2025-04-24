package net.togyk.myneheroes.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
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
import net.togyk.myneheroes.entity.ModEntities;

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
            Random random = new Random();
            float randomF = random.nextFloat();
            if (server.getOverworld() != null && randomF < 0.04 / 20 / 60) {
                ServerWorld world = server.getOverworld();
                List<ServerPlayerEntity> players = world.getPlayers();

                if (!players.isEmpty()) {
                    ServerPlayerEntity player = players.get(random.nextInt(players.size()));
                    BlockPos playerPos = player.getBlockPos();

                    BlockPos pos = playerPos.add(random.nextInt(-320, 320), 0, random.nextInt(-320, 320));
                    MeteorEntity projectile = new MeteorEntity(ModEntities.METEOR, world);
                    projectile.setPosition(Vec3d.of(pos.withY(620)));
                    projectile.setVelocity(random.nextDouble(), 0, random.nextDouble(), 3.0F, 0.5F);

                    world.spawnEntity(projectile);
                } else {
                    Vec3i pos = new Vec3i(random.nextInt(-1000, 100), 620, random.nextInt(-1000, 1000));
                    MeteorEntity projectile = new MeteorEntity(world);
                    projectile.setPosition(Vec3d.of(pos));
                    projectile.setVelocity(random.nextDouble(), 0, random.nextDouble(), 3.0F, 0.5F);

                    world.spawnEntity(projectile);
                }
            }
        });
    }
}
