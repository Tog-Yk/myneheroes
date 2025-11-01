package net.togyk.myneheroes.ability.detailed;

import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.togyk.myneheroes.util.SimpleEventResult;
import net.togyk.myneheroes.util.StockPile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;

public class ShootDiscardableProjectilePassiveAbility<T extends ProjectileEntity> extends ShootProjectilePassiveAbility<T> {
    private List<UUID> shotEntities = new ArrayList<>();

    public ShootDiscardableProjectilePassiveAbility(Identifier id, int cooldown, int cost, Settings settings, BiFunction<PlayerEntity, ShootProjectilePassiveAbility<T>, T> createProjectile) {
        super(id, cooldown, cost, settings, createProjectile);
    }

    @Override
    public SimpleEventResult onMissedInteraction(PlayerEntity player) {
        if (this.get() && this.projectileCooldown == 0 && (!(this.getIndirectHolder() instanceof StockPile stockPile) || stockPile.getCharge() >= this.getCost())) {
            if (!player.getWorld().isClient) {
                Vec3d look = player.getRotationVec(1.0F);

                T projectile = createProjectile.apply(player, this);

                player.getWorld().spawnEntity(projectile);
                shotEntities.add(projectile.getUuid());

                player.swingHand(Hand.MAIN_HAND);
            }
            player.swingHand(Hand.MAIN_HAND);
            if (this.getIndirectHolder() instanceof StockPile stockPile) {
                stockPile.setCharge(stockPile.getCharge() - this.cost);
            }
            this.projectileCooldown = projectileMaxCooldown;
            return SimpleEventResult.SUCCESS;
        }
        return SimpleEventResult.PASS;
    }

    @Override
    public SimpleEventResult onMissedAttack(PlayerEntity player) {
        if (!this.shotEntities.isEmpty()) {
            this.shotEntities.stream().map(uuid -> this.getEntityByUuid(uuid, player.getBlockPos(), player.getWorld())).filter(Optional::isPresent).map(Optional::get).forEach(Entity::discard);
            this.shotEntities = new ArrayList<>();

            return SimpleEventResult.SUCCESS;
        }
        return SimpleEventResult.PASS;
    }


    private Optional<Entity> getEntityByUuid(UUID uuid, BlockPos pos, World world) {
        Box box = new Box(pos).expand(100);
        return world.getEntitiesByClass(Entity.class, box, Predicates.alwaysTrue()).stream().filter(entity -> uuid.equals(entity.getUuid())).findFirst();
    }


    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList list = new NbtList();
        for (UUID uuid : this.shotEntities) {
            list.add(NbtString.of(uuid.toString())); // store as string for readability
        }
        nbt.put("shot_entities", list);

        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("shot_entities")) {
            List<UUID> uuids = new ArrayList<>();
            NbtList list = nbt.getList("shot_entities", NbtElement.STRING_TYPE);
            for (int i = 0; i < list.size(); i++) {
                try {
                    uuids.add(UUID.fromString(list.getString(i)));
                } catch (IllegalArgumentException ignored) {
                    // skip malformed UUID entries
                }
            }
            this.shotEntities = uuids;
        }
    }

    @Override
    public ShootDiscardableProjectilePassiveAbility<T> copy() {
        return new ShootDiscardableProjectilePassiveAbility<>(id, maxCooldown, cost, settings, createProjectile);
    }
}
