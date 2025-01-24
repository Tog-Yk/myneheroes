package net.togyk.myneheroes.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.damage.ModDamageTypes;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.Powers;
import net.togyk.myneheroes.util.PlayerPowers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.max;

@Mixin(PlayerEntity.class)
public abstract class PlayerAppliedAttributeMixin implements PlayerPowers {
    @ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0)
    private float modifyDamageAfterApply(float amount) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        // Check if the source is a player
        if (player != null) {
            // Multiply the damage taken
            float multiplier = ((PlayerPowers) player).getResistance(); // Your custom multiplier
            if (multiplier != 1.0F) {
                return amount * multiplier;
            }
        }
        return amount;
    }
}