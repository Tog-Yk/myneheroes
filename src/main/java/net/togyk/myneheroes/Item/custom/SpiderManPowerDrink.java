package net.togyk.myneheroes.Item.custom;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.Powers;

import java.util.List;

public class SpiderManPowerDrink extends PowerDrinkItem {
    public SpiderManPowerDrink(List<RegistryEntry<StatusEffect>> sideEffects, Settings settings) {
        super(null, sideEffects, settings);
    }

    @Override
    protected Power getPower(PlayerEntity player) {
        return player.hasStatusEffect(StatusEffects.WEAVING) ? Powers.SPIDER_ORGANIC_WEBBING : Powers.SPIDER;
    }
}
