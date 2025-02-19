package net.togyk.myneheroes.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.togyk.myneheroes.Item.ModItems;

public class ThrownItemEntity extends PersistentProjectileEntity {
    public ThrownItemEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }
    public ThrownItemEntity(EntityType<? extends ThrownItemEntity> entityType, World world, LivingEntity owner, ItemStack stack) {
        super(entityType, owner, world, stack, null);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return ModItems.VIBRANIUM_SHIELD.getDefaultStack();
    }
}
