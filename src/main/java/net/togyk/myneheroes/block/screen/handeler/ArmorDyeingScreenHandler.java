package net.togyk.myneheroes.block.screen.handeler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.togyk.myneheroes.block.ModBlocks;
import net.togyk.myneheroes.block.entity.ArmorDyeingBlockEntity;
import net.togyk.myneheroes.block.screen.ModScreenHandlerTypes;
import net.togyk.myneheroes.networking.BlockPosPayload;

public class ArmorDyeingScreenHandler extends ScreenHandler {
    private final ArmorDyeingBlockEntity blockEntity;
    private final ScreenHandlerContext context;
    //Main Constructor
    public ArmorDyeingScreenHandler(int syncId, PlayerInventory playerInventory, ArmorDyeingBlockEntity blockEntity) {
        super(ModScreenHandlerTypes.ARMOR_DYEING, syncId);

        this.blockEntity = blockEntity;
        this.context = ScreenHandlerContext.create(this.blockEntity.getWorld(), this.blockEntity.getPos());

        SimpleInventory inventory = this.blockEntity.getInventory();
        checkSize(inventory, 1);
        inventory.onOpen(playerInventory.player);

        addBlockInventory(inventory);
        addPlayerInventory(playerInventory);
    }

    //Client Constructor
    public ArmorDyeingScreenHandler(int syncId, PlayerInventory playerInventory, BlockPosPayload payload) {
        this(syncId, playerInventory, (ArmorDyeingBlockEntity) playerInventory.player.getWorld().getBlockEntity(payload.pos()));
    }


    private void addBlockInventory(SimpleInventory inventory) {
        addSlot(new Slot(inventory, 0, 30, 35));
    }

    private void addPlayerInventory(PlayerInventory inventory) {
        for (int row = 0; row < 3; row++) {
            for (int colum = 0; colum < 9; colum++) {
                addSlot(new Slot(inventory, 9 + colum + row * 9, 8 + colum * 18, 84 + row * 18));
            }
        }
        addPlayerHotbar(inventory);
    }

    private void addPlayerHotbar(PlayerInventory inventory) {
        for (int colum = 0; colum < 9; colum++) {
            addSlot(new Slot(inventory, colum, 8 + colum * 18, 142));
        }
    }


    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.blockEntity.getInventory().onClose(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.getSlot(slotIndex);
        if (slot != null && slot.hasStack()) {
            ItemStack inSlot = slot.getStack();
            newStack = inSlot.copy();

            if (slotIndex < this.blockEntity.getInventory().size()) {
                if (!insertItem(inSlot, this.blockEntity.getInventory().size(), this.slots.size(), true)){
                    return ItemStack.EMPTY;
                }
            } else if (!insertItem(inSlot, 0, this.blockEntity.getInventory().size(), false)){
                return ItemStack.EMPTY;
            }
            if (inSlot.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ModBlocks.ARMOR_DYEING_STATION);
    }

    public ArmorDyeingBlockEntity getBlockEntity() {
        return blockEntity;
    }
}
