package net.togyk.myneheroes.power.detailed;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.power.StockpilePower;

import java.util.List;

public class KryptonianPower extends StockpilePower {
    public KryptonianPower(Identifier id, int maxCharge, int color, List<Ability> abilities, Settings settings, attributeModifiers attributeModifiers) {
        super(id, maxCharge, color, abilities, settings, attributeModifiers);
    }

    @Override
    public void tick(PlayerEntity player) {
        World world = player.getWorld();
        BlockPos pos = player.getBlockPos();

        boolean isExposedToSun = world.isDay() && world.isSkyVisible(pos);

        if (!this.isDampened()) {
            if (isExposedToSun && this.getMaxCharge() != this.getCharge()) {
                setCharge(getCharge() + 1);
            }
        } else {
            setCharge(getCharge() - 25);
        }
    }

    @Override
    public KryptonianPower copy() {
        return new KryptonianPower(this.id, this.getMaxCharge(), this.getColor(), List.copyOf(this.abilities), settings, attributeModifiers);
    }
}
