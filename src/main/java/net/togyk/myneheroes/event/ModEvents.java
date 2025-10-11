package net.togyk.myneheroes.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.togyk.myneheroes.Item.ModItems;
import net.togyk.myneheroes.Item.custom.ThrowableItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.PassiveAbility;
import net.togyk.myneheroes.block.custom.KryptoniteRadiationBlock;
import net.togyk.myneheroes.block.custom.RadiationBlock;
import net.togyk.myneheroes.damage.ModDamageTypes;
import net.togyk.myneheroes.entity.MeteorEntity;
import net.togyk.myneheroes.gamerule.ModGamerules;
import net.togyk.myneheroes.persistent_data.ModPersistentData;
import net.togyk.myneheroes.persistent_data.TimeNearKryptoniteData;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.Powers;
import net.togyk.myneheroes.util.PlayerAbilities;
import net.togyk.myneheroes.util.PowerData;
import net.togyk.myneheroes.util.SimpleEventResult;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class ModEvents {

    public static void registerEvents() {
        MyneHeroes.LOGGER.info("registering Events for" + MyneHeroes.MOD_ID);

        MissedSwingCallback.EVENT.register((PlayerEntity player, Hand hand) -> {
            // Create an arrow entity. (This example does not consume inventory arrows.)
            ItemStack stack = player.getStackInHand(hand);
            if (player.getWorld() != null && stack.getItem() instanceof ThrowableItem throwableItem) {
                throwableItem.Throw(player.getWorld(), player, hand);
            } else {
                for (Ability ability : ((PlayerAbilities) player).myneheroes$getAbilities()) {
                    if (ability instanceof PassiveAbility passiveAbility) {
                        if (passiveAbility.onMissedAttack(player) == SimpleEventResult.SUCCESS) {
                            break;
                        }
                    }
                }
            }
        });

        MissedInteractionCallback.EVENT.register(player -> {
            for (Ability ability : ((PlayerAbilities) player).myneheroes$getAbilities()) {
                if (ability instanceof PassiveAbility passiveAbility) {
                    if (passiveAbility.onMissedInteraction(player) == SimpleEventResult.SUCCESS) {
                        return SimpleEventResult.SUCCESS;
                    }
                }
            }
            return SimpleEventResult.PASS;
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
                    // Check if the player has reached the limit of powers
                    // Check if the player has the Speed effect
                    StatusEffectInstance speedEffect = player.getStatusEffect(StatusEffects.SPEED);
                    if (speedEffect != null && speedEffect.getDuration() > 0) {
                        Power speedster = Powers.SPEEDSTER.copy();
                        PowerData.addUniquePowerToLimit(player, speedster);
                    }
                }
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            int tickCount = server.getTicks();
            if (tickCount % 20 == 0) {
                for (PlayerEntity player : server.getPlayerManager().getPlayerList()) {
                    List<Power> powers = PowerData.getPowers(player);
                    if (powers.size() > server.getOverworld().getGameRules().getInt(ModGamerules.POWER_LIMIT)) {
                        player.damage(ModDamageTypes.powerExhaustion(player.getWorld(), null), 2);
                    }
                }
            }
        });

        //decrease kryptonian progress if the player isn't near kryptonite
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            TimeNearKryptoniteData timeData = ModPersistentData.getTimeNearKryptonite(server.getOverworld());
            Map<UUID, Long> timeMap = timeData.getTimeMap();
            for (PlayerEntity player : server.getPlayerManager().getPlayerList()) {
                World world = player.getWorld();
                boolean isExposedToSun = world.isDay() && world.isSkyVisible(player.getBlockPos());
                if ((!isPlayerNearBlock(player, world, KryptoniteRadiationBlock.class, 7) || isExposedToSun) && timeMap.containsKey(player.getUuid())) {
                    if (timeMap.get(player.getUuid()) < 1L) {
                        timeMap.remove(player.getUuid());
                    } else {
                        timeMap.put(player.getUuid(), timeMap.get(player.getUuid()) - 1);
                    }
                }
            }
            timeData.markDirty();
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (hitResult != null) {
                return ActionResult.PASS;
            }

            if (!entity.getWorld().isClient && entity instanceof CaveSpiderEntity) {
                ItemStack handStack = player.getStackInHand(hand);
                if (!handStack.isEmpty() && handStack.getItem() == Items.GLASS_BOTTLE) {
                    if (!player.isCreative()) {
                        handStack.decrement(1);
                    }
                    ItemStack bottleStack = ModItems.BOTTLE_OF_SPIDER_VENOM.getDefaultStack();
                    if (handStack.isEmpty()) {
                        player.setStackInHand(hand, bottleStack);
                    } else {
                        if (!player.giveItemStack(bottleStack)) {
                            //doesn't fit in the inventory
                            player.dropItem(bottleStack, false);
                        }
                    }
                    return ActionResult.SUCCESS;
                }
            }
            return ActionResult.PASS;
        });

        ServerChunkEvents.CHUNK_GENERATE.register((serverWorld, chunk) -> {
            //*
            ChunkPos chunkPos = chunk.getPos();
            for (int x = chunkPos.getStartX(); x <= chunkPos.getEndX(); x++) {
                for (int z = chunkPos.getStartZ(); z <= chunkPos.getEndZ(); z++) {
                    for (int y = serverWorld.getBottomY(); y <= serverWorld.getTopY(); y++) {
                        BlockPos pos = new BlockPos(x, y, z);
                        //*
                        BlockState state = chunk.getBlockState(pos);

                        if (state.getBlock() instanceof RadiationBlock radiationBlock && !state.get(RadiationBlock.TICKING)) {
                            // start ticking
                            serverWorld.scheduleBlockTick(pos, radiationBlock, 24);
                            chunk.setBlockState(pos, state.with(RadiationBlock.TICKING, true), false);
                        }
                        //*/
                    }
                }
            }
            //*/
        });
    }


    public static boolean isPlayerNearBlock(PlayerEntity player, World world, Class<? extends Block> targetBlock, int radius) {
        BlockPos playerPos = player.getBlockPos();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos checkPos = playerPos.add(x, y, z);
                    Block block = world.getBlockState(checkPos).getBlock();

                    if (targetBlock.isInstance(block)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
