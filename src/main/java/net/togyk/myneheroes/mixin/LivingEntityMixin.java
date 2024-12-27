package net.togyk.myneheroes.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.damage.ModDamageTypes;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.PlayerPowers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "damage", at = @At("HEAD"))
    private void modifyDamageAfterApply(DamageSource source, float amount, CallbackInfoReturnable info) {
        LivingEntity entity = (LivingEntity) (Object) this;

        // Check if the source is a player
        if (source.getAttacker() instanceof LivingEntity attacker && attacker.isPlayer() && source.getType().equals(attacker.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).get(Identifier.ofVanilla("player_attack")))) {
            // Multiply the damage taken
            float multiplier = ((PlayerPowers) attacker).getDamageMultiplier(); // Your custom multiplier
            if (multiplier != 1.0F) {
                float multipliedDamage = amount * multiplier;

                World world = attacker.getWorld();
                entity.damage(ModDamageTypes.of(world, ModDamageTypes.POWERFUL_PUNCH_TYPE_KEY, attacker), multipliedDamage);
            }
        }
    }
}