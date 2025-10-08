package net.togyk.myneheroes.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.togyk.myneheroes.Item.custom.AbilityLinkedWeapon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {
    @Inject(method = "insertItem", at = @At("HEAD"), cancellable = true)
    private void onInsertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() instanceof AbilityLinkedWeapon) {
            // Prevent the item from being shift-clicked or inserted anywhere
            cir.cancel();
        }
    }
}
