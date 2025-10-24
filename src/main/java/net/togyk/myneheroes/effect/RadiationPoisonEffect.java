package net.togyk.myneheroes.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.damage.ModDamageTypes;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.Powers;
import net.togyk.myneheroes.registry.ModRegistries;
import net.togyk.myneheroes.util.ModTags;
import net.togyk.myneheroes.util.PowerData;

import java.util.Optional;
import java.util.Random;

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

    //
    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity player) {
            this.player = player;
        }
        super.onApplied(entity, amplifier);
    }

    @Override
    public void onRemoved(AttributeContainer attributeContainer) {
        if (player != null) {
            Random random = new Random();
            if (random.nextFloat() <= 1f) {
                MyneHeroes.LOGGER.info(String.valueOf(Powers.MUTANT_REGENERATION.isIn(ModTags.Powers.RADIATION_OBTAINABLE)));

                Power power = getRandomPowerFromTag(random, ModTags.Powers.RADIATION_OBTAINABLE);
                if (power != null) {
                    PowerData.addUniquePowerToLimit(player, power);
                }
            }
        }
        super.onRemoved(attributeContainer);
    }


    private Power getRandomPowerFromTag(Random random, TagKey<Power> tag) {
        // Get all the powers in the tag
        Optional<RegistryEntryList.Named<Power>> tagEntryList = ModRegistries.POWER.getEntryList(tag);

        if (tagEntryList.isPresent()) {
            RegistryEntryList.Named<Power> entries = tagEntryList.get();
            int size = entries.size();
            if (size > 0) {
                RegistryEntry<Power> entry = entries.get(random.nextInt(size));
                return entry.value().copy();
            }
        }

        return null;
    }
}
