package net.togyk.myneheroes.client.screen.handeler;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.togyk.myneheroes.Item.custom.DyeableItem;
import net.togyk.myneheroes.block.ModBlocks;
import net.togyk.myneheroes.block.entity.ArmorDyeingBlockEntity;
import net.togyk.myneheroes.client.screen.ModScreenHandlerTypes;
import net.togyk.myneheroes.networking.BlockPosPayload;
import net.togyk.myneheroes.networking.ColorItemPayload;

import java.util.ArrayList;
import java.util.List;

public class ArmorDyeingScreenHandler extends ScreenHandler {
    private final ArmorDyeingBlockEntity blockEntity;
    private final ScreenHandlerContext context;

    //a list of the index from all "dyeable" layers in the items Armor
    private List<Integer> indexOptions = new ArrayList<>();
    private int selectedOption;

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
        addSlot(new Slot(inventory, 0, 16, 39) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof DyeableItem;
            }
        });
    }

    private void addPlayerInventory(PlayerInventory inventory) {
        for (int row = 0; row < 3; row++) {
            for (int colum = 0; colum < 9; colum++) {
                addSlot(new Slot(inventory, 9 + colum + row * 9, 8 + colum * 18, 96 + row * 18));
            }
        }
        addPlayerHotbar(inventory);
    }

    private void addPlayerHotbar(PlayerInventory inventory) {
        for (int colum = 0; colum < 9; colum++) {
            addSlot(new Slot(inventory, colum, 8 + colum * 18, 154));
        }
    }

    public void setContentsChangedListener(Runnable contentsChangedListener) {
        this.blockEntity.setContentsChangedListener(contentsChangedListener);
    }


    public void updateOptions() {
        indexOptions.clear();
        ItemStack stack = this.blockEntity.getInventory().getStack(0);
        if (stack.getItem() instanceof DyeableItem dyeableItem) {
            // Generate options based on the armor material
            for (int i = 0; i < dyeableItem.getColors(stack).size(); i++) {
                if (dyeableItem.layerIsDyeable(i)) {
                    indexOptions.add(i);
                }
            }
        }
    }

    public List<Integer> getOptions() {
        return indexOptions;
    }

    public int getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(int i) {
        this.selectedOption = i;
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (this.isInBounds(id)) {
            this.selectedOption = indexOptions.get(id);
        }
        return true;
    }
    private boolean isInBounds(int id) {
        return id >= 0 && id < this.indexOptions.size();
    }

    public boolean canSelect() {
        return !this.blockEntity.getInventory().isEmpty();
    }

    public boolean canDye() {
        return !this.blockEntity.getInventory().isEmpty();
    }
    public void dye(int color) {
        ClientPlayNetworking.send(new ColorItemPayload(this.blockEntity.getPos(), this.indexOptions.get(this.selectedOption), color, false));
    }

    public void dyeDefault() {
        ItemStack stack = blockEntity.getInventory().getStack(0);
        if (stack.getItem() instanceof DyeableItem) {
            ClientPlayNetworking.send(new ColorItemPayload(this.blockEntity.getPos(), this.indexOptions.get(this.selectedOption), -1, true));
        }
    }

    public int getColor() {
        ItemStack stack = this.blockEntity.getInventory().getStack(0);
        if (stack.getItem() instanceof DyeableItem dyeableItem && !indexOptions.isEmpty()) {
            return dyeableItem.getColor(stack, this.indexOptions.get(this.selectedOption));
        }
        return -0xFFFFFF;
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
