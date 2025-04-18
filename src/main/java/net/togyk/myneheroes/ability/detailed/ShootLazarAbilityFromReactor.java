package net.togyk.myneheroes.ability.detailed;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.Item.custom.ReactorItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.entity.LaserEntity;
import net.togyk.myneheroes.entity.ModEntities;
import org.jetbrains.annotations.NotNull;

public class ShootLazarAbilityFromReactor extends Ability {
    public ShootLazarAbilityFromReactor(Identifier id, String name, int cooldown) {
        super(id, name, cooldown);
    }


    @Override
    public void Use(PlayerEntity player) {
        if (getCooldown() == 0) {
            ItemStack reactorStack = MyneHeroes.getReactorItemClass(player);
            if (reactorStack.getItem() instanceof ReactorItem reactor) {
                int reactorPower = reactor.getStoredPowerOrDefault(reactorStack, 0);
                if (reactorPower >= 50) {
                    reactor.setStoredPower(reactorStack, reactorPower - 50);
                    // shoot a lazar
                    Vec3d look = player.getRotationVec(1.0F);

                    LaserEntity projectile = getLaserEntity(player, look);

                    player.getWorld().spawnEntity(projectile);
                    player.swingHand(Hand.MAIN_HAND);

                    this.setCooldown(this.getMaxCooldown());
                }
            }
        }

        this.save();
    }

    private static @NotNull LaserEntity getLaserEntity(PlayerEntity player, Vec3d look) {
        LaserEntity projectile = new LaserEntity(ModEntities.LASER, player.getWorld());
        projectile.setOwner(player);
        projectile.setPosition(player.getX(), player.getEyeY(), player.getZ());
        projectile.setVelocity(look.x, look.y, look.z, 3.0F, 0.0F);
        projectile.applyDamageModifier(2.0F);
        projectile.setColor(0x3300FFFF);
        projectile.setInnerColor(0xFFF0FFFF);
        return projectile;
    }

    @Override
    public ShootLazarAbilityFromReactor copy() {
        return new ShootLazarAbilityFromReactor(this.id, this.getName(), this.getMaxCooldown());
    }
}
