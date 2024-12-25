package net.togyk.myneheroes.ability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.Item.custom.ReactorItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.entity.LaserEntity;
import net.togyk.myneheroes.entity.ModEntities;

public class LasersFromEyesAbility extends Ability{
    public LasersFromEyesAbility(String name, int cooldown) {
        super(name, cooldown);
    }

    @Override
    public void clientUse(PlayerEntity player) {
    }

    @Override
    public void serverUse(PlayerEntity player) {
        if (getCooldown() == 0) {
            // shoot laser
            Vec3d look = player.getRotationVec(1.0F);

            LaserEntity projectile = new LaserEntity(ModEntities.LASER, player.getWorld());
            projectile.setOwner(player);
            projectile.setPosition(player.getX(), player.getEyeY(), player.getZ());
            projectile.setVelocity(look.x, look.y, look.z, 3.0F, 0.0F);
            projectile.applyDamageModifier(2.0F);

            player.getWorld().spawnEntity(projectile);
        }
        super.serverUse(player);
    }
}
