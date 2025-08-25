package net.togyk.myneheroes.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.togyk.myneheroes.damage.ModDamageTypes;

public class RadiationPoisonEffect extends StatusEffect {
    private PlayerEntity player;

    protected RadiationPoisonEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.damage(ModDamageTypes.radiation(entity.getWorld()), 1.0F);

        return true;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % 24 == 0;
    }
}
