package net.togyk.myneheroes.ability;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.Item.custom.ReactorItem;
import net.togyk.myneheroes.MyneHeroes;

public class ShootLazarAbilityFromReactor extends Ability{
    public ShootLazarAbilityFromReactor(String name, int cooldown) {
        super(name, cooldown);
    }

    @Override
    public void clientUse(PlayerEntity player) {
        // Use the player's main hand to mimic shooting
        player.swingHand(Hand.MAIN_HAND);
    }

    @Override
    public void serverUse(PlayerEntity player) {
        if (cooldown == 0) {
            ItemStack reactorStack = MyneHeroes.getReactorItemClass(player);
            if (reactorStack.getItem() instanceof ReactorItem reactor) {
                int reactorPower = reactor.getStoredPowerOrDefault(reactorStack, 0);
                if (reactorPower >= 50) {
                    reactor.setStoredPower(reactorStack, reactorPower - 50);
                    // shoot a lazar
                    Vec3d look = player.getRotationVec(1.0F);

                    ArrowEntity projectile = new ArrowEntity(EntityType.ARROW, player.getWorld());
                    projectile.setPosition(player.getX(), player.getEyeY(), player.getZ());
                    projectile.setVelocity(look.x, look.y, look.z, 3.0F, 0.0F);
                    projectile.applyDamageModifier(2.0F);

                    player.getWorld().spawnEntity(projectile);
                }
            }
            cooldown = getMaxCooldown();
        }
    }
}
