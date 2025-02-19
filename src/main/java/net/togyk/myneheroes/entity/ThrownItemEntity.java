package net.togyk.myneheroes.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.togyk.myneheroes.Item.ModItems;

public class ThrownItemEntity extends PersistentProjectileEntity {
    private static final TrackedData<ItemStack> STACK = DataTracker.registerData(ThrownItemEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    public ThrownItemEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }
    public ThrownItemEntity(EntityType<? extends ThrownItemEntity> entityType, World world, LivingEntity owner, ItemStack stack) {
        super(entityType, owner, world, stack, null);
        this.setDisplayStack(stack);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return ModItems.VIBRANIUM_SHIELD.getDefaultStack();
    }

    public ItemStack getDisplayStack() {
        return this.getDataTracker().get(STACK);
    }

    /**
     * Sets the item stack contained in this item entity to {@code stack}.
     */
    public void setDisplayStack(ItemStack stack) {
        this.getDataTracker().set(STACK, stack);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        if (STACK.equals(data)) {
            this.getDisplayStack().setHolder(this);
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(STACK, ItemStack.EMPTY);
    }

}