package net.togyk.myneheroes.Item.block;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.togyk.myneheroes.Item.custom.WorldTickableItem;
import net.togyk.myneheroes.effect.ModEffects;
import net.togyk.myneheroes.util.ModTags;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RadiationBlockItem extends BlockItem implements WorldTickableItem {
    private final int range;
    private final boolean usesDFS;
    private final RegistryEntry<StatusEffect> effect;

    public RadiationBlockItem(int range, boolean usesDFS, RegistryEntry<StatusEffect> effect, Block block, Settings settings) {
        super(block, settings);
        this.range = range;
        this.usesDFS = usesDFS;
        this.effect = effect;
    }

    public RadiationBlockItem(Block block, Settings settings) {
        this(5, true, ModEffects.RADIATION_POISON, block, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (selected && !world.isClient) {
            if (usesDFS()) {
                radiateEntitiesInRangeUsingDFS(world, entity.getBlockPos(), this.getRange());
            } else {
                radiateEntitiesInRange(world, entity.getBlockPos(), this.getRange());
            }
        }
    }

    @Override
    public void worldTick(ItemStack stack, World world, ItemEntity entity) {
        if (!world.isClient) {
            if (usesDFS()) {
                radiateEntitiesInRangeUsingDFS(world, entity.getBlockPos(), this.getRange());
            } else {
                radiateEntitiesInRange(world, entity.getBlockPos(), this.getRange());
            }
        }
    }

    public boolean canGiveEffect(LivingEntity entity) {
        if (!entity.hasStatusEffect(this.getEffect())) return true;

        StatusEffectInstance effect = entity.getStatusEffect(this.getEffect());
        if (effect != null) {
            int duration = effect.getDuration();
            return (duration + 1) % 24 == 0;
        }
        return false;
    }

    public RegistryEntry<StatusEffect> getEffect() {
        return this.effect;
    }

    public void radiateEntitiesInRangeUsingDFS(World world, BlockPos origin, int range) {
        radiateEntitiesInRangeUsingDFS(world, origin, origin, range, new HashSet<>(), new HashSet<>());
    }

    public void radiateEntitiesInRangeUsingDFS(World world, BlockPos origin, BlockPos pos, int range, Set<BlockPos> visited, Set<LivingEntity> visitedEntities) {
        visited.add(pos);
        radiateEntitiesInRange(world, pos, 0, visitedEntities);
        for (Direction dir : Direction.values()) {
            BlockPos next = pos.offset(dir);
            if (next.isWithinDistance(origin, range) && !visited.contains(next) && world.getBlockState(next).isIn(ModTags.Blocks.RADIATION_GOES_THROUGH)) {
                radiateEntitiesInRangeUsingDFS(world, origin, next, range, visited, visitedEntities);
            }
        }
    }

    public void radiateEntitiesInRange(World world, BlockPos pos, int range) {
        radiateEntitiesInRange(world, pos, range, new HashSet<>());
    }

    public void radiateEntitiesInRange(World world, BlockPos pos, int range, Set<LivingEntity> excludedEntities) {
        Box area = new Box(pos).expand(range); // radiation radius
        List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, area, e -> true);

        for (LivingEntity entity : entities) {
            // Entity is exposed to radiation
            if (!excludedEntities.contains(entity)) {
                this.onEntityFound(entity);
                if (this.canGiveEffect(entity)) {
                    entity.addStatusEffect(new StatusEffectInstance(
                            this.getEffect(), 72, 0, true, true
                    ));
                }
                excludedEntities.add(entity);
            }
        }
    }

    public boolean usesDFS() {
        return this.usesDFS;
    }

    public int getRange() {
        return this.range;
    }

    public void onEntityFound(LivingEntity entity) {
    }
}
