package net.togyk.myneheroes.ability.detailed;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.ability.StockpileLinkedAbility;
import net.togyk.myneheroes.entity.LaserEntity;
import net.togyk.myneheroes.entity.ModEntities;
import net.togyk.myneheroes.power.StockpilePower;

public class LazarsFromEyesAbility extends StockpileLinkedAbility {

    public LazarsFromEyesAbility(Identifier id, String name, int cooldown, int unlocksAt, int cost) {
        super(id, name, cooldown, unlocksAt, cost);
    }

    @Override
    public void Use(PlayerEntity player) {
        if (this.getCooldown() == 0 && this.getHolderItem() instanceof StockpilePower power && power.getCharge() >= this.getCost() && !player.getWorld().isClient) {
            // shoot laser
            Vec3d look = player.getRotationVec(1.0F);

            LaserEntity projectile = new LaserEntity(ModEntities.LASER, player.getWorld());
            projectile.setColor(0x55FF0000);
            projectile.setOwner(player);
            projectile.setPosition(player.getX(), player.getEyeY(), player.getZ());
            projectile.setVelocity(look.x, look.y, look.z, 3.0F, 0.0F);
            projectile.applyDamageModifier(2.0F);

            player.getWorld().spawnEntity(projectile);
        }
        super.Use(player);
    }

    @Override
    public LazarsFromEyesAbility copy() {
        return new LazarsFromEyesAbility(this.id, this.getName(), this.getMaxCooldown(), this.getUnlocksAt(), this.getCost());
    }
}
