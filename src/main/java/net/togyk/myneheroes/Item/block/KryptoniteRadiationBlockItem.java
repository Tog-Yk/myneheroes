package net.togyk.myneheroes.Item.block;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.togyk.myneheroes.Item.custom.WorldTickableItem;
import net.togyk.myneheroes.effect.ModEffects;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.PowerData;

import java.util.List;

public class KryptoniteRadiationBlockItem extends RadiationBlockItem implements WorldTickableItem {
    public KryptoniteRadiationBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    protected void applyRadiation(LivingEntity target) {
        if (target instanceof PlayerEntity player) {
            List<Power> currentPowers = PowerData.getPowers(player);
            if (this.hasKryptoniteDampenedPower(currentPowers)) {
                if (target.hasStatusEffect(ModEffects.KRYPTONITE_POISON)) {
                    StatusEffectInstance effect = target.getStatusEffect(ModEffects.KRYPTONITE_POISON);
                    if (effect != null) {
                        int duration = effect.getDuration();
                        if ((duration + 1) % 24 == 0) {
                            target.addStatusEffect(new StatusEffectInstance(
                                    ModEffects.KRYPTONITE_POISON, 47, 0, true, true
                            ));
                        }
                    }
                } else {
                    target.addStatusEffect(new StatusEffectInstance(
                            ModEffects.KRYPTONITE_POISON, 47, 0, true, true
                    ));
                }
            }
        }
    }

    private boolean hasKryptoniteDampenedPower(List<Power> powers) {
        for (Power power : powers) {
            if (power.isDampenedByKryptonite()) {
                return true;
            }
        }
        return false;
    }
}
