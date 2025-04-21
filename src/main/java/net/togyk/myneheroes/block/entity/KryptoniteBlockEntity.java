package net.togyk.myneheroes.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.togyk.myneheroes.block.ModBlockEntityTypes;
import net.togyk.myneheroes.block.custom.KryptoniteBlock;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.Powers;
import net.togyk.myneheroes.util.PowerData;

import java.util.*;

public class KryptoniteBlockEntity extends BlockEntity {
    Map<UUID, Long> TimeNearBlockPerPlayer = new HashMap<>();

    public KryptoniteBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.KRYPTONITE_BLOCK_ENTITY, pos, state);
    }



    public void tick(ServerWorld world, BlockPos pos, BlockState state) {

        List<PlayerEntity> players = world.getEntitiesByClass(PlayerEntity.class, new Box(pos.getX() - 7, pos.getY() - 7, pos.getZ() + 7, pos.getX() + 7, pos.getY() + 7, pos.getZ() - 7), p -> true);
        if (!players.isEmpty()) {
            Set<BlockPos> visited = new HashSet<>();
            this.dfs(world, pos, visited);
            int touchingBlocks = visited.size();;
            
            //filter out all the players that are eligible
            List<UUID> eligiblePlayers = new ArrayList<>();
            for (PlayerEntity player : players) {
                boolean isExposedToSun = world.isDay() && world.isSkyVisible(player.getBlockPos());
                List<Identifier> currentPowers = PowerData.getPowers(player).stream().map(Power::getId).toList();
                if (currentPowers.contains(Powers.KRYPTONIAN.getId())) {
                    //give the player kryptonite poisoning
                    this.TimeNearBlockPerPlayer.remove(player.getUuid());
                } else if (!isExposedToSun) {
                    eligiblePlayers.add(player.getUuid());
                }
            }

            //create/increase the players time near the block and check if they should become a kryptonian
            for (UUID id : eligiblePlayers) {
                if (this.TimeNearBlockPerPlayer.getOrDefault(id, 0L) >= 20L * 60 * 480) {
                    List<Power> currentPowers = PowerData.getPowers(world.getPlayerByUuid(id));
                    Power kryptonianPower = Powers.KRYPTONIAN.copy();
                    if (kryptonianPower != null && !currentPowers.stream().map(Power::getId).toList().contains(kryptonianPower.id)) {
                        PowerData.addPower(world.getPlayerByUuid(id), kryptonianPower);
                    }
                    this.TimeNearBlockPerPlayer.remove(id);
                }
                this.TimeNearBlockPerPlayer.put(id, this.TimeNearBlockPerPlayer.getOrDefault(id, 0L) + touchingBlocks);
            }

            //decrease the players time near the block for the absent players
            for (Map.Entry<UUID, Long> e : this.TimeNearBlockPerPlayer.entrySet()) {
                if (!eligiblePlayers.contains(e.getKey())) {
                    if (e.getValue() < 1L) {
                        this.TimeNearBlockPerPlayer.remove(e.getKey());
                    } else {
                        this.TimeNearBlockPerPlayer.put(e.getKey(), e.getValue() - 1);
                    }
                }
            }
        }
    }

    private void dfs(World world, BlockPos pos, Set<BlockPos> visited) {
        visited.add(pos);
        for (Direction dir : Direction.values()) {
            BlockPos next = pos.offset(dir);
            if (!visited.contains(next) &&
                    world.getBlockState(next).getBlock() instanceof KryptoniteBlock) {
                dfs(world, next, visited);
            }
        }
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        Map<UUID, Long> map = new HashMap<>();
        if (nbt.contains("time_near_block_per_player")) {
            NbtList list = nbt.getList("TimeNearBlockPerPlayer", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < list.size(); i++) {
                NbtCompound entry = list.getCompound(i);
                UUID id = entry.getUuid("player");
                long count = entry.getLong("count");
                map.put(id, count);
            }
        }
        this.TimeNearBlockPerPlayer = map;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);

        NbtList list = new NbtList();
        for (Map.Entry<UUID, Long> e : this.TimeNearBlockPerPlayer.entrySet()) {
            NbtCompound entry = new NbtCompound();
            entry.putUuid("player", e.getKey());
            entry.putLong("count", e.getValue());
            list.add(entry);
        }

        nbt.put("time_near_block_per_player", list);
    }
}
