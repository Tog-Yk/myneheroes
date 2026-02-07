package net.togyk.myneheroes.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.togyk.myneheroes.Item.custom.EquipCallbackItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Equipment.class)
public interface EquipmentMixin {
    @Inject(
            method = "equipAndSwap",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"
            )
    )
    private static void beforeSwap(Item item, World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack heldStack = player.getStackInHand(hand);
        EquipmentSlot slot = player.getPreferredEquipmentSlot(heldStack);
        ItemStack oldArmor = player.getEquippedStack(slot);

        // Trigger unequip for OLD armor BEFORE swapping
        if (!oldArmor.isEmpty() && oldArmor.getItem() instanceof EquipCallbackItem oldItem) {
            oldItem.onUnequipped(player, oldArmor, slot);
        }
    }
}
