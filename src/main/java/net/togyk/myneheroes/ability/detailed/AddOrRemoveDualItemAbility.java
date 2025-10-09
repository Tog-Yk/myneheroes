package net.togyk.myneheroes.ability.detailed;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.TemporaryWeapon;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.ItemRenderableAbility;

public class AddOrRemoveDualItemAbility extends Ability implements ItemRenderableAbility {
    private final ItemConvertible itemConvertible;

    public AddOrRemoveDualItemAbility(Identifier id, ItemConvertible item, int cooldown, Settings settings) {
        super(id, cooldown, settings, null);
        this.itemConvertible = item;
    }

    @Override
    public void use(PlayerEntity player) {
        if (this.getCooldown() == 0) {
            if (itemConvertible != null) {
                Item item = itemConvertible.asItem();

                //empty hand if they already have the item
                if (player.getMainHandStack().isOf(item.asItem()) || player.getOffHandStack().isOf(item.asItem())) {
                    if (player.getOffHandStack().isOf(item.asItem())) {
                        player.getOffHandStack().setCount(0);
                    }
                    if (player.getMainHandStack().isOf(item.asItem())) {
                        player.getMainHandStack().setCount(0);
                    }
                } else {
                    //else add items in the empty hands
                    if (player.getOffHandStack().isEmpty() || player.getOffHandStack().getItem() instanceof TemporaryWeapon) {
                        player.setStackInHand(Hand.OFF_HAND, item.asItem().getDefaultStack());
                    }

                    addItemToMainHand(player, item.asItem().getDefaultStack());
                }
            }
            this.setCooldown(this.getMaxCooldown());
        }
        this.save();
    }

    private void addItemToMainHand(PlayerEntity player, ItemStack stack) {
        PlayerInventory inv = player.getInventory();

        if (player.getMainHandStack().isEmpty() || player.getOffHandStack().getItem() instanceof TemporaryWeapon) {
            player.setStackInHand(Hand.MAIN_HAND, stack);
            return;
        }


        // 2. Try to find empty slot in the hotbar (slots 0–8)
        for (int i = 0; i < 9; i++) {
            if (inv.getStack(i).isEmpty() || player.getOffHandStack().getItem() instanceof TemporaryWeapon) {
                inv.setStack(i, stack);
                inv.selectedSlot = i; // switch player to that slot
                player.playerScreenHandler.sendContentUpdates();
                return;
            }
        }

        // 3. If no empty hotbar slot → move main-hand item to inventory
        ItemStack mainHand = player.getMainHandStack();
        player.setStackInHand(Hand.MAIN_HAND, stack);
        if (!inv.insertStack(mainHand)) { // try moving it into inventory
            player.dropItem(mainHand, false);
        }

        player.playerScreenHandler.sendContentUpdates();
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
    public AddOrRemoveDualItemAbility copy() {
        return new AddOrRemoveDualItemAbility(id, itemConvertible, maxCooldown, settings);
    }
}
