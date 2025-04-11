package net.togyk.myneheroes.power.detailed;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.power.StockpilePower;

import java.util.List;

public class KryptonianPower extends StockpilePower {
    public KryptonianPower(Identifier id, int maxCharge, float damageMultiplier, float resistance, int color, List<Ability> abilities) {
        super(id, maxCharge, damageMultiplier, resistance, color, abilities);
    }

    @Override
    public void tick(PlayerEntity player) {
        World world = player.getWorld();
        BlockPos pos = player.getBlockPos();

        boolean isExposedToSun = world.isDay() && world.isSkyVisible(pos);

        if (!this.isDampened() && isExposedToSun) {
            if (this.getMaxCharge() != this.getCharge()) {
                setCharge(getCharge() + 1);
            }
        }
    }

    @Override
    public boolean allowFlying(PlayerEntity player) {
        return this.getCharge() >= (this.getMaxCharge() * 0.05F);
    }

    @Override
    public KryptonianPower copy() {
        return new KryptonianPower(this.id, this.getMaxCharge(), damageMultiplier, resistance, this.getColor(), List.copyOf(this.abilities));
    }
}
