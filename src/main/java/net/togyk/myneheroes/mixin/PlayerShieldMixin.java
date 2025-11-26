package net.togyk.myneheroes.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.togyk.myneheroes.Item.custom.KatanaItem;
import net.togyk.myneheroes.util.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerShieldMixin {

    @Inject(method = "damageShield", at = @At("HEAD"))
    protected void damageShield(float amount, CallbackInfo ci) {
        PlayerEntity player = ((PlayerEntity) (Object) this);

        ItemStack stack = player.getActiveItem();
        if (stack.isIn(ModTags.Items.SHIELDS)) {
            if (!player.getWorld().isClient) {
                player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
            }

            if (amount >= 3.0F) {
                int i = 1 + MathHelper.floor(amount);
                Hand hand = player.getActiveHand();
                stack.damage(i, player, LivingEntity.getSlotForHand(hand));
                if (stack.isEmpty()) {
                    if (hand == Hand.MAIN_HAND) {
                        player.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    } else {
                        player.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                    }

                    player.setStackInHand(hand, ItemStack.EMPTY);
                    player.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + player.getWorld().random.nextFloat() * 0.4F);
                }
            }
        }
        if (stack.getItem() instanceof KatanaItem katanaItem) {
            katanaItem.onBlocked(player, amount);
        }
    }
}
