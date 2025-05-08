package net.togyk.myneheroes.block.screen.handeler;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.togyk.myneheroes.Item.custom.LightableItem;
import net.togyk.myneheroes.block.ModBlocks;
import net.togyk.myneheroes.block.entity.ArmorLightLevelerBlockEntity;
import net.togyk.myneheroes.block.screen.ModScreenHandlerTypes;
import net.togyk.myneheroes.networking.BlockPosPayload;
import net.togyk.myneheroes.networking.LightLevelerPayload;

import java.util.ArrayList;
import java.util.List;

public class ArmorLightLevelerScreenHandler extends ScreenHandler {
    private final ArmorLightLevelerBlockEntity blockEntity;
    private final ScreenHandlerContext context;

    //a list of the index from all "dyeable" layers in the items Armor
    private List<Integer> indexOptions = new ArrayList<>();
    private int selectedOption;

    //Main Constructor
    public ArmorLightLevelerScreenHandler(int syncId, PlayerInventory playerInventory, ArmorLightLevelerBlockEntity blockEntity) {
        super(ModScreenHandlerTypes.ARMOR_LIGHT_LEVELER, syncId);

        this.blockEntity = blockEntity;
        this.context = ScreenHandlerContext.create(this.blockEntity.getWorld(), this.blockEntity.getPos());

        SimpleInventory inventory = this.blockEntity.getInventory();
        checkSize(inventory, 1);
        inventory.onOpen(playerInventory.player);

        addBlockInventory(inventory);
        addPlayerInventory(playerInventory);
    }

    //Client Constructor
    public ArmorLightLevelerScreenHandler(int syncId, PlayerInventory playerInventory, BlockPosPayload payload) {
        this(syncId, playerInventory, (ArmorLightLevelerBlockEntity) playerInventory.player.getWorld().getBlockEntity(payload.pos()));
    }


    private void addBlockInventory(SimpleInventory inventory) {
        addSlot(new Slot(inventory, 0, 16, 33) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof LightableItem;
            }
        });
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

    public void setContentsChangedListener(Runnable contentsChangedListener) {
        this.blockEntity.setContentsChangedListener(contentsChangedListener);
    }


    public void updateOptions() {
        indexOptions.clear();
        ItemStack stack = this.blockEntity.getInventory().getStack(0);
        if (stack.getItem() instanceof LightableItem lightableItem) {
            // Generate options based on the armor material
            for (int i = 0; i < lightableItem.getLightLevels(stack).size(); i++) {
                if (lightableItem.layerIsLightable(i)) {
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
        selectedOption = i;
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

    public boolean canLevel() {
        return !this.blockEntity.getInventory().isEmpty();
    }

    public int getLightlevel() {
        ItemStack stack = this.blockEntity.getInventory().getStack(0);
        if (stack.getItem() instanceof LightableItem lightableItem) {
            return lightableItem.getLightLevel(stack, this.indexOptions.get(this.selectedOption));
        }
        return -1;
    }

    public void level(int lightLevel) {
        ClientPlayNetworking.send(new LightLevelerPayload(this.blockEntity.getPos(), this.indexOptions.get(this.selectedOption), lightLevel));
    }

    public void levelDefault() {
        ItemStack stack = blockEntity.getInventory().getStack(0);
        if (stack.getItem() instanceof LightableItem lightableItem) {
            int lightLevel =  lightableItem.getDefaultLightLevel(this.indexOptions.get(this.selectedOption));
            ClientPlayNetworking.send(new LightLevelerPayload(this.blockEntity.getPos(), this.indexOptions.get(this.selectedOption), lightLevel));
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
        return canUse(this.context, player, ModBlocks.ARMOR_LIGHT_LEVELING_STATION);
    }

    public ArmorLightLevelerBlockEntity getBlockEntity() {
        return blockEntity;
    }
}
