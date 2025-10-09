package net.togyk.myneheroes.ability.detailed;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.TemporaryWeapon;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.ItemRenderableAbility;

public class AddOrRemoveItemAbility extends Ability implements ItemRenderableAbility {
    private final ItemConvertible itemConvertible;

    public AddOrRemoveItemAbility(Identifier id, ItemConvertible item, int cooldown, Settings settings) {
        super(id, cooldown, settings, null);
        this.itemConvertible = item;
    }

    @Override
    public void use(PlayerEntity player) {
        if (this.getCooldown() == 0) {
            if (itemConvertible != null) {
                Item item = itemConvertible.asItem();

                //empty hand if they already have the item
                if (player.getMainHandStack().isOf(item.asItem())) {
                    if (player.getMainHandStack().isOf(item.asItem())) {
                        player.getMainHandStack().setCount(0);
                    }
                } else {
                    //else add items in the hand
                    addItemToMainHand(player, item.asItem().getDefaultStack());
                }
            }
            this.setCooldown(this.getMaxCooldown());
        }
        this.save();
    }

    private void addItemToMainHand(PlayerEntity player, ItemStack stack) {
        if (player.getMainHandStack().isEmpty() || player.getMainHandStack().getItem() instanceof TemporaryWeapon) {
            player.setStackInHand(Hand.MAIN_HAND, stack);
            return;
        }


        // 3. If no empty hotbar slot → move main-hand item to inventory
        ItemStack stackInHand = player.getMainHandStack().copyAndEmpty();

        player.setStackInHand(Hand.MAIN_HAND, stack);

        // Try to insert into inventory
        if (!player.getInventory().insertStack(stackInHand)) {
            player.dropItem(stackInHand, false);
        }
        player.dropStack(player.getMainHandStack());
    }

    @Override
    public ItemStack getItem() {
        if (this.itemConvertible != null) {
            return this.itemConvertible.asItem().getDefaultStack();
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public AddOrRemoveItemAbility copy() {
        return new AddOrRemoveItemAbility(id, itemConvertible, maxCooldown, settings);
    }
}
