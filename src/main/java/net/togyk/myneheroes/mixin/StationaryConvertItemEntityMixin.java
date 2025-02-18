package net.togyk.myneheroes.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.togyk.myneheroes.Item.custom.StationaryItem;
import net.togyk.myneheroes.entity.ModEntities;
import net.togyk.myneheroes.entity.SationaryItemEntity;
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
        if (!entity.getWorld().isClient() && !(entity instanceof SationaryItemEntity) && stack.getItem() instanceof StationaryItem) {
            ItemEntity newItemEntity = getItemEntity(entity, stack);

            entity.getWorld().spawnEntity(newItemEntity);

            entity.setStack(ItemStack.EMPTY);
        }
    }

    private static @NotNull ItemEntity getItemEntity(ItemEntity entity, ItemStack stack) {
        ItemEntity newItemEntity = new SationaryItemEntity(ModEntities.STATIONARY_ITEM, entity.getWorld());
        newItemEntity.setPos(entity.getX(), entity.getY(), entity.getZ());
        if (entity.getOwner() != null) {
            newItemEntity.setOwner(entity.getOwner().getUuid());
        }
        newItemEntity.setStack(stack);
        newItemEntity.setNeverDespawn();
        newItemEntity.setPickupDelay(40);
        newItemEntity.setVelocity(entity.getVelocity());
        return newItemEntity;
    }
}
