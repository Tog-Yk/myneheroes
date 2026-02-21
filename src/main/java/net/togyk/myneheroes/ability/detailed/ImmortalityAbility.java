package net.togyk.myneheroes.ability.detailed;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.PassiveAbility;

public class ImmortalityAbility extends Ability implements PassiveAbility {
    public ImmortalityAbility(Identifier id, int cooldown, Settings settings) {
        super(id, cooldown, settings, null);
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    @Override
    public boolean onDeath(PlayerEntity player, DamageSource source) {
        if (this.getCooldown() == 0) {
            this.setCooldown(this.getMaxCooldown());
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 450, 1, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 400, 0, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 200, 0, false, false));

            if (player instanceof ServerPlayerEntity serverPlayer) {
                for (int index = 0; index < 20; index++) {
                    spawnSmoke(serverPlayer);
                }
            }
            if (player instanceof ServerPlayerEntity serverPlayer) {
                for (int index = 0; index < 40; index++) {
                    spawnSoulFire(serverPlayer);
                }
            }

            return false;
        }
        return true;
    }

    public static void spawnSmoke(ServerPlayerEntity player) {
        World world = player.getWorld();

        // Get player position (at chest height)
        Vec3d pos = player.getPos().add(0, 1.2, 0);

        // Slight random offset for realism
        double offsetX = (world.getRandom().nextDouble() - 0.5) * 0.5;
        double offsetZ = (world.getRandom().nextDouble() - 0.5) * 0.5;

        // Spawn the smoke particle (you can change type)
        player.getServerWorld().spawnParticles(
                ParticleTypes.CAMPFIRE_COSY_SMOKE,
                pos.x + offsetX,
                pos.y,
                pos.z + offsetZ,
                1,       // count
                offsetX * 4, -0.3, offsetZ * 4, // spread
                0.01     // speed
        );
    }

    public static void spawnSoulFire(ServerPlayerEntity player) {
        World world = player.getWorld();

        // Get player position (at chest height)
        Vec3d pos = player.getPos().add(0, 1.2, 0);

        // Slight random offset for realism
        double offsetX = (world.getRandom().nextDouble() - 0.5) * 0.5;
        double offsetY = (world.getRandom().nextDouble() - 0.5) * 0.5;
        double offsetZ = (world.getRandom().nextDouble() - 0.5) * 0.5;

        // Spawn the smoke particle (you can change type)
        player.getServerWorld().spawnParticles(
                ParticleTypes.SOUL_FIRE_FLAME,
                pos.x + offsetX,
                pos.y + offsetY,
                pos.z + offsetZ,
                1,       // count
                offsetX, offsetY, offsetZ, // direction
                0.5     // speed
        );
    }

    @Override
    public Ability copy() {
        return new ImmortalityAbility(id, maxCooldown, settings);
    }
}
