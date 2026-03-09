package net.togyk.myneheroes.ability.detailed;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.ability.HudAbility;
import net.togyk.myneheroes.client.HudType;

import java.util.List;

public class SpiderSenseHudAbility extends HudAbility {
    public SpiderSenseHudAbility(Identifier id, Settings settings) {
        super(id, settings, HudType.SPIDER_SENSE);
    }

    @Override
    public boolean get() {
        Entity holder = this.getEntityHolder();
        if (holder instanceof PlayerEntity player) {
            List<LivingEntity> nearbyEntities = player.getWorld().getEntitiesByClass(
                    LivingEntity.class,
                    player.getBoundingBox().expand(5),
                    (entity) -> !entity.isRemoved() && (entity instanceof Monster || entity instanceof PlayerEntity && entity != player) && isOutsideFOV(player, entity, 70)
            );

            List<ProjectileEntity> nearbyProjectiles = player.getWorld().getEntitiesByClass(
                    ProjectileEntity.class,
                    player.getBoundingBox().expand(20),
                    (entity) -> !entity.isRemoved() && isMovingToPlayer(entity, player)
            );

            return !nearbyProjectiles.isEmpty() || !nearbyEntities.isEmpty();
        }
        return false;
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    @Override
    public HudAbility copy() {
        return new SpiderSenseHudAbility(id, settings);
    }

    private boolean isMovingToPlayer(ProjectileEntity projectile, PlayerEntity player) {
        Vec3d projectileVelocity = projectile.getVelocity().normalize();
        Vec3d toPlayer = player.getPos().subtract(projectile.getPos()).normalize();

        double dot = projectileVelocity.dotProduct(toPlayer);

        return dot > 0.9;
    }

    private boolean isOutsideFOV(PlayerEntity player, Entity entity, double fovDegrees) {
        Vec3d look = player.getRotationVec(1.0F).normalize();

        Vec3d toEntity = entity.getPos()
                .add(0, entity.getHeight() * 0.5, 0)
                .subtract(player.getEyePos())
                .normalize();

        double dot = look.dotProduct(toEntity);

        double threshold = Math.cos(Math.toRadians(fovDegrees / 2.0));

        return dot < threshold;
    }
}
