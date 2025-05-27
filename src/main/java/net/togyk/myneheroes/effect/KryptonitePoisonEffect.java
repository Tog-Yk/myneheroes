package net.togyk.myneheroes.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.PowerData;

import java.util.List;

public class KryptonitePoisonEffect extends StatusEffect {
    private PlayerEntity player;

    protected KryptonitePoisonEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.getHealth() > 1.0F) {
            entity.damage(entity.getDamageSources().magic(), 1.0F);
        }

        return true;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % 24 == 0;
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity player) {
            this.player = player;
            List<Power> powers = PowerData.getPowers(player);
            for (Power power : powers) {
                if (power.isDampenedByKryptonite()) {
                    power.setIsDampened(true);
                }
            }
        }
        super.onApplied(entity, amplifier);
    }

    @Override
    public void onRemoved(AttributeContainer attributeContainer) {
        if (player != null) {
            List<Power> powers = PowerData.getPowers(player);
            for (Power power : powers) {
                if (power.isDampenedByKryptonite()) {
                    power.setIsDampened(false);
                }
            }
        }
        super.onRemoved(attributeContainer);
    }
}
