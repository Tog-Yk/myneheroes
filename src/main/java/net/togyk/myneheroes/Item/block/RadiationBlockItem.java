package net.togyk.myneheroes.Item.block;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.togyk.myneheroes.Item.custom.WorldTickableItem;
import net.togyk.myneheroes.effect.ModEffects;

import java.util.List;

public class RadiationBlockItem extends BlockItem implements WorldTickableItem {
    public RadiationBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (selected && !world.isClient) {
            Box area = new Box(entity.getBlockPos()).expand(5); // radiation radius
            List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, area, e -> true);

            for (LivingEntity target : entities) {
                this.applyRadiation(target);
            }
        }
    }

    @Override
    public void worldTick(ItemStack stack, World world, ItemEntity entity) {
        if (!world.isClient) {
            Box area = new Box(entity.getBlockPos()).expand(5); // radiation radius
            List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, area, e -> true);

            for (LivingEntity target : entities) {
                this.applyRadiation(target);
            }
        }
    }

    protected void applyRadiation(LivingEntity target) {
        if (target.hasStatusEffect(ModEffects.RADIATION_POISON)) {
            StatusEffectInstance effect = target.getStatusEffect(ModEffects.RADIATION_POISON);
            if (effect != null) {
                int duration = effect.getDuration();
                if ((duration + 1) % 24 == 0) {
                    target.addStatusEffect(new StatusEffectInstance(
                            ModEffects.RADIATION_POISON, 47, 0, true, true
                    ));
                }
            }
        } else {
            target.addStatusEffect(new StatusEffectInstance(
                    ModEffects.RADIATION_POISON, 47, 0, true, true
            ));
        }
    }
}
