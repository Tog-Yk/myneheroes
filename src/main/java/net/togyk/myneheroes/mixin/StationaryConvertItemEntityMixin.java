package net.togyk.myneheroes.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.togyk.myneheroes.Item.custom.StationaryItem;
import net.togyk.myneheroes.entity.ModEntities;
import net.togyk.myneheroes.entity.StationaryItemEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class StationaryConvertItemEntityMixin {
    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void ConversionOnTick(CallbackInfo info) {
        ItemEntity entity = (ItemEntity) (Object) this;
        ItemStack stack = entity.getStack();
        if (!entity.getWorld().isClient() && stack.getItem() instanceof StationaryItem) {
            StationaryItemEntity newItemEntity = getItemEntity(entity, stack);

            entity.getWorld().spawnEntity(newItemEntity);

            entity.setStack(ItemStack.EMPTY);
        }
    }

    private static @NotNull StationaryItemEntity getItemEntity(ItemEntity entity, ItemStack stack) {
        StationaryItemEntity newItemEntity = new StationaryItemEntity(ModEntities.STATIONARY_ITEM, entity.getWorld());
        newItemEntity.setPos(entity.getX(), entity.getY(), entity.getZ());
        if (entity.getOwner() != null) {
            newItemEntity.setOwner(entity.getOwner());
        }
        newItemEntity.setItem(stack);
        newItemEntity.setVelocity(entity.getVelocity());
        return newItemEntity;
    }
}
