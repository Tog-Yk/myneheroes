package net.togyk.myneheroes.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.Item.custom.StationaryItem;
import net.togyk.myneheroes.Item.custom.ThrowableShieldItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.entity.LaserEntity;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.Powers;
import net.togyk.myneheroes.util.PlayerPowers;

import java.util.*;

import static java.lang.Math.max;

public class ModEvents {

    public static void registerEvents() {
        MyneHeroes.LOGGER.info("registering Events for" + MyneHeroes.MOD_ID);

        Map<ServerPlayerEntity, Long> timeInNether = new HashMap<>();
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerWorld world : server.getWorlds()) {
                if (world.getRegistryKey().equals(ServerWorld.NETHER)) {

                    for (ServerPlayerEntity player : world.getPlayers()) {
                        timeInNether.put(player, timeInNether.getOrDefault(player, 0L) + 1);

                        if (timeInNether.get(player) >= 2 * 60 * 20L) {
                            Power kryptonianPower = Powers.get(Identifier.of(MyneHeroes.MOD_ID, "kryptonian"));
                            if (kryptonianPower != null) {
                                ((PlayerPowers) player).addPower(kryptonianPower);
                                MyneHeroes.LOGGER.info("congrats on becoming a kryptonian");
                            }
                            timeInNether.put(player,0L);
                        }
                    }
                } else {
                    for (ServerPlayerEntity player : world.getPlayers()) {
                        timeInNether.put(player, max(timeInNether.getOrDefault(player, 0L) - 1, 0L));
                    }
                }
            }

            // count down if players are no longer in the Nether
            timeInNether.keySet().removeIf(player -> player.getWorld() != server.getWorld(ServerWorld.NETHER));
        });

        MissedSwingCallback.EVENT.register((PlayerEntity player, Hand hand) -> {
            // Create an arrow entity. (This example does not consume inventory arrows.)
            ItemStack stack = player.getStackInHand(hand);
            if (stack.getItem() instanceof ThrowableShieldItem shieldItem) {
                Vec3d look = player.getRotationVec(1.0F);

                PersistentProjectileEntity projectile = new LaserEntity(shieldItem.getProjectileEntityType(), player.getWorld());
                projectile.setOwner(player);
                projectile.setPosition(player.getX(), player.getEyeY(), player.getZ());
                projectile.setVelocity(look.x, look.y, look.z, 3.0F, 0.0F);
                projectile.applyDamageModifier(2.0F);

                player.getWorld().spawnEntity(projectile);
            }
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            // Check if the entity that was clicked is your custom projectile.
            if (!entity.getWorld().isClient && entity instanceof StationaryItem stationaryItem) {
                // Send a normal chat message to the player (false means not as an action bar message).
                player.sendMessage(Text.literal("You clicked my projectile!"), false);

                return stationaryItem.interactEntity(player, hand);
            }
            // Otherwise, let other handlers process the interaction.
            return ActionResult.PASS;
        });

    }
}
