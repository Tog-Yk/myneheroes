package net.togyk.myneheroes.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.togyk.myneheroes.Item.custom.StationaryItem;
import net.togyk.myneheroes.entity.ModEntities;
import net.togyk.myneheroes.entity.SationaryItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(ServerPlayerEntity.class)
public class StationaryItemPlayerMixin {
    protected final Random random = Random.create();

    @ModifyVariable(
            method = "dropItem",
            at = @At("STORE")
    )
    private ItemEntity modifyItemEntities(ItemEntity entity, @Local ItemStack itemStack) {
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof StationaryItem) {
            ItemEntity newItemEntity = new SationaryItemEntity(ModEntities.STATIONARY_ITEM, entity.getWorld());
            newItemEntity.setPos(entity.getX(), entity.getY(), entity.getZ());
            newItemEntity.setStack(itemStack);
            newItemEntity.setNeverDespawn();
            newItemEntity.setPickupDelay(40);

            //movement
            PlayerEntity player = (PlayerEntity) (Object) this;

            float f = 0.3F;
            float g = MathHelper.sin(player.getPitch() * (float) (Math.PI / 180.0));
            float h = MathHelper.cos(player.getPitch() * (float) (Math.PI / 180.0));
            float i = MathHelper.sin(player.getYaw() * (float) (Math.PI / 180.0));
            float j = MathHelper.cos(player.getYaw() * (float) (Math.PI / 180.0));
            float k = this.random.nextFloat() * (float) (Math.PI * 2);
            float l = 0.02F * this.random.nextFloat();
            newItemEntity.setVelocity(
                    (double)(-i * h * 0.3F) + Math.cos((double)k) * (double)l,
                    (double) (-g * 0.3F + 0.1F + (this.random.nextFloat() - this.random.nextFloat()) * 0.1F),
                    (double)(j * h * 0.3F) + Math.sin((double)k) * (double)l
            );

            return newItemEntity;
        }
        return entity;
    }
}
