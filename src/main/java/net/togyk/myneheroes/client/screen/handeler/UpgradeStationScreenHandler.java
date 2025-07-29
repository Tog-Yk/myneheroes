package net.togyk.myneheroes.client.screen.handeler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;
import net.togyk.myneheroes.Item.custom.UpgradableItem;
import net.togyk.myneheroes.Item.custom.UpgradeItem;
import net.togyk.myneheroes.block.ModBlocks;
import net.togyk.myneheroes.block.entity.UpgradeStationBlockEntity;
import net.togyk.myneheroes.client.screen.ModScreenHandlerTypes;
import net.togyk.myneheroes.networking.BlockPosPayload;
import net.togyk.myneheroes.upgrade.Upgrade;

import java.util.ArrayList;
import java.util.List;

public class UpgradeStationScreenHandler extends ScreenHandler {
    private final UpgradeStationBlockEntity blockEntity;
    private final ScreenHandlerContext context;

    private final World world;

    //Main Constructor
    public UpgradeStationScreenHandler(int syncId, PlayerInventory playerInventory, UpgradeStationBlockEntity blockEntity) {
        super(ModScreenHandlerTypes.UPGRADE_STATION, syncId);

        this.blockEntity = blockEntity;
        this.context = ScreenHandlerContext.create(this.blockEntity.getWorld(), this.blockEntity.getPos());

        this.world = playerInventory.player.getWorld();

        blockEntity.setContentsChangedListenerForInput(this::updateInventory);
        blockEntity.setContentsChangedListenerForUpgrades(this::updateUpgrades);

        SimpleInventory input = this.blockEntity.getInput();
        checkSize(input, 1);
        input.onOpen(playerInventory.player);

        addBlockInput(input);

        SimpleInventory upgrades = this.blockEntity.getUpgrades();
        checkSize(upgrades, 6);
        upgrades.onOpen(playerInventory.player);

        addBlockUpgrades(upgrades);
        addPlayerInventory(playerInventory);
    }

    private void updateInventory() {
        if (blockEntity.getInput().isEmpty()) {
            blockEntity.getUpgrades().clear();
        } else {
            ItemStack inputStack = blockEntity.getInput().getStack(0);
            if (inputStack.getItem() instanceof UpgradableItem upgradableItem) {
                List<Upgrade> upgrades = upgradableItem.getUpgrades(inputStack);
                for (Upgrade upgrade : upgrades) {
                    ItemStack stack = upgrade.getItemStack(this.world);
                    blockEntity.getUpgrades().setStack(upgrades.indexOf(upgrade), stack);
                }
            }
        }
    }

    private void updateUpgrades() {
        if (!blockEntity.getInput().isEmpty()) {
            ItemStack inputStack = blockEntity.getInput().getStack(0);
            if (inputStack.getItem() instanceof UpgradableItem upgradableItem) {
                List<Upgrade> upgrades = new ArrayList<>();
                for (ItemStack stack : blockEntity.getUpgrades().getHeldStacks()) {
                    if (stack.getItem() instanceof UpgradeItem upgradeItem) {
                        Upgrade upgrade = upgradeItem.getUpgrade();
                        upgrade.setItemStack(stack, this.world);

                        upgrades.add(upgrade);
                    }
                }
                upgradableItem.setUpgrades(inputStack, upgrades);
            }
        }
    }

    //Client Constructor
    public UpgradeStationScreenHandler(int syncId, PlayerInventory playerInventory, BlockPosPayload payload) {
        this(syncId, playerInventory, (UpgradeStationBlockEntity) playerInventory.player.getWorld().getBlockEntity(payload.pos()));
    }


    private void addBlockInput(SimpleInventory inventory) {
        addSlot(new Slot(inventory, 0, 80, 32) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof UpgradableItem upgradableItem && upgradableItem.canBeUpgraded();
            }
        });
    }

    private void addBlockUpgrades(SimpleInventory inventory) {
        addUpgradeSlot(inventory, 0, 67, 7);
        addUpgradeSlot(inventory, 1, 93, 7);
        addUpgradeSlot(inventory, 2, 54, 32);
        addUpgradeSlot(inventory, 3, 106, 32);
        addUpgradeSlot(inventory, 4, 67, 57);
        addUpgradeSlot(inventory, 5, 93, 57);
    }

    private void addUpgradeSlot(SimpleInventory inventory, int index, int x, int y) {
        addSlot(new Slot(inventory, index, x, y) {
            @Override
            public boolean canInsert(ItemStack stack) {
                if (stack.getItem() instanceof UpgradeItem upgradeItem) {
                    Upgrade upgrade = upgradeItem.getUpgrade();
                    if (upgrade != null) {
                        upgrade.setItemStack(stack, world);
                        return canUpgrade(upgrade);
                    }
                }
                return false;
            }

            @Override
            public int getMaxItemCount(ItemStack stack) {
                return 1;
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

    public boolean canUpgrade(Upgrade upgrade) {
        return !this.blockEntity.getInput().isEmpty() && this.blockEntity.getInput().getStack(0).getItem() instanceof UpgradableItem upgradableItem && upgradableItem.canUpgrade(this.blockEntity.getInput().getStack(0), upgrade);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        Slot slot = this.slots.get(slotIndex);
        if (!slot.hasStack()) {
            return ItemStack.EMPTY;
        }

        ItemStack originalStack = slot.getStack();
        ItemStack copyStack = originalStack.copy();

        int blockInputStart = 0;
        int blockInputEnd = 1; // exclusive
        int blockUpgradeStart = blockInputEnd;
        int blockUpgradeEnd = blockUpgradeStart + 6;

        int playerInvStart = blockUpgradeEnd;
        int playerInvEnd = playerInvStart + 27;
        int hotbarStart = playerInvEnd;
        int hotbarEnd = hotbarStart + 9;

        if (slotIndex < playerInvStart) {
            // Shift-clicked from block inventory
            if (!this.insertItem(originalStack, playerInvStart, hotbarEnd, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            int countBefore = originalStack.getCount();

            // Shift-clicked from player inventory
            if (originalStack.getItem() instanceof UpgradableItem upgradableItem && upgradableItem.canBeUpgraded()) {
                if (!this.insertItem(originalStack, blockInputStart, blockInputEnd, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (originalStack.getItem() instanceof UpgradeItem upgradeItem && canUpgrade(upgradeItem.getUpgrade())) {
                if (!this.insertItem(originalStack, blockUpgradeStart, blockUpgradeEnd, false)) {
                    return ItemStack.EMPTY;
                }
            }

            // If only partially moved, don't move to hotbar/inventory
            if (originalStack.getCount() < countBefore && !originalStack.isEmpty()) {
                return ItemStack.EMPTY;
            } else if (slotIndex < hotbarStart) {
                // Move from inventory to hotbar
                if (!this.insertItem(originalStack, hotbarStart, hotbarEnd, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Move from hotbar to inventory
                if (!this.insertItem(originalStack, playerInvStart, playerInvEnd, false)) {
                    return ItemStack.EMPTY;
                }
            }
        }

        if (originalStack.isEmpty()) {
            slot.setStack(ItemStack.EMPTY);
        } else {
            slot.markDirty();
        }

        return copyStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ModBlocks.UPGRADE_STATION);
    }

    public UpgradeStationBlockEntity getBlockEntity() {
        return blockEntity;
    }
}
