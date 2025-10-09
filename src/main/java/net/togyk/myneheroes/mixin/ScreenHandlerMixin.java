package net.togyk.myneheroes.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.togyk.myneheroes.util.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {
    @Inject(method = "insertItem", at = @At("HEAD"), cancellable = true)
    private void onInsertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast, CallbackInfoReturnable<Boolean> cir) {
        if (stack.isIn(ModTags.Items.CANT_BE_PLACED_IN_CHESTS)) {
            // Prevent the item from being shift-clicked or inserted anywhere
            cir.cancel();
        }
    }
}
