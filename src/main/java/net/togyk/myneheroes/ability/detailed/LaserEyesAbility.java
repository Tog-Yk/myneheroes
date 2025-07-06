package net.togyk.myneheroes.ability.detailed;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.ability.StockpileLinkedAbility;
import net.togyk.myneheroes.client.render.LaserRenderer;
import net.togyk.myneheroes.power.Power;

import java.util.function.Function;

public class LaserEyesAbility extends StockpileLinkedAbility {
    private final LaserRenderer laserRenderer = new LaserRenderer();

    protected final int innerColor;
    protected final int color;

    public LaserEyesAbility(Identifier id, int cooldown, int unlocksAt, int cost, Settings settings, Function<PlayerEntity, Boolean> use, int innerColor, int color) {
        super(id, cooldown, unlocksAt, cost, settings, use);
        this.innerColor = innerColor;
        this.color = color;
    }

    public LaserRenderer getLaserRenderer() {
        return laserRenderer;
    }

    public Vec3d getStart(float tickDelta) {
        if (this.getIndirectHolder() instanceof Power power && power.getHolder() != null) {
            PlayerEntity player = power.getHolder();
            // Interpolated position
            double x = MathHelper.lerp(tickDelta, player.prevX, player.getX());
            double y = MathHelper.lerp(tickDelta, player.prevY, player.getY());
            double z = MathHelper.lerp(tickDelta, player.prevZ, player.getZ());

            // Add eye height
            return new Vec3d(x, y + player.getEyeHeight(player.getPose()), z);
        }
        return null;
    }

    public Vec3d getEnd(float tickDelta) {
        if (this.getIndirectHolder() instanceof Power power && power.getHolder() != null) {
            PlayerEntity player = power.getHolder();
            return player.getEyePos().add(player.getRotationVec(tickDelta).multiply(10));
        }
        return null;
    }

    public int getInnerColor() {
        return innerColor;
    }

    public int getColor() {
        return color;
    }

    @Override
    public LaserEyesAbility copy() {
        return new LaserEyesAbility(id, maxCooldown, unlocksAt, cost, settings, use, innerColor, color);
    }
}
