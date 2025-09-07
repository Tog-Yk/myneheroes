package net.togyk.myneheroes.ability.detailed;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.ability.PassiveAbility;
import net.togyk.myneheroes.ability.StockpileLinkedAbility;
import net.togyk.myneheroes.util.SimpleEventResult;
import net.togyk.myneheroes.util.StockPile;

import java.util.function.BiFunction;

public class ShootProjectilePassiveAbility<T extends ProjectileEntity> extends StockpileLinkedAbility implements PassiveAbility {
    protected final BiFunction<PlayerEntity, ShootProjectilePassiveAbility<T>, T> createProjectile;
    private boolean bool = true;

    protected int projectileCooldown = 0;
    protected final int projectileMaxCooldown;

    public ShootProjectilePassiveAbility(Identifier id, int cooldown, int cost, Settings settings, BiFunction<PlayerEntity, ShootProjectilePassiveAbility<T>, T> createProjectile) {
        super(id, 2, cost, cost, settings, null);
        this.createProjectile = createProjectile;
        this.projectileMaxCooldown = cooldown;
    }

    @Override
    public void tick(PlayerEntity player) {
        if (this.projectileCooldown != 0) {
            this.projectileCooldown = this.projectileCooldown - 1;
        }
        if (this.projectileCooldown < 0) {
            this.projectileCooldown = 0;
        }
        super.tick(player);
    }

    @Override
    public void use(PlayerEntity player) {
        //switch the boolean
        if (getCooldown() == 0) {
            this.bool = !this.bool;
            this.setCooldown(this.getMaxCooldown());
        }
        this.save();
    }

    @Override
    public SimpleEventResult onMissedInteraction(PlayerEntity player) {
        if (this.get() && this.projectileCooldown == 0 && (!(this.getIndirectHolder() instanceof StockPile stockPile) || stockPile.getCharge() >= this.getCost())) {
            if (!player.getWorld().isClient) {
                T projectile = createProjectile.apply(player, this);
                player.getWorld().spawnEntity(projectile);
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

    public boolean get() {
        return bool;
    }

    public void set(boolean bool) {
        this.bool = bool;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putBoolean("bool", this.bool);
        nbt.putInt("projectile_cooldown", this.projectileCooldown);
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("bool")) {
            this.bool = nbt.getBoolean("bool");
        }
        if (nbt.contains("projectile_cooldown")) {
            this.projectileCooldown = nbt.getInt("projectile_cooldown");
        }
        super.readNbt(nbt);
    }

    @Override
    public ShootProjectilePassiveAbility<T> copy() {
        return new ShootProjectilePassiveAbility<>(id, maxCooldown, cost, settings, createProjectile);
    }
}
