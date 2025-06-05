package net.togyk.myneheroes.block.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.block.ModBlockEntityTypes;
import net.togyk.myneheroes.client.screen.handeler.UpgradeStationScreenHandler;
import net.togyk.myneheroes.networking.BlockPosPayload;
import org.jetbrains.annotations.Nullable;

public class UpgradeStationBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPosPayload> {
    public static final Text SCREEN_NAME_1 = Text.translatable("container." + MyneHeroes.MOD_ID + ".upgrade_station_1");
    public static final Text SCREEN_NAME_2 = Text.translatable("container." + MyneHeroes.MOD_ID + ".upgrade_station_2");

    Runnable contentsChangedListenerInput = () -> {
    };
    Runnable contentsChangedListenerUpgrades = () -> {
    };

    private final SimpleInventory input = new SimpleInventory(1) {
        @Override
        public void markDirty() {
            super.markDirty();
            updateInput();
        }
    };

    private final InventoryStorage inventoryStorage = InventoryStorage.of(input, null);

    private final SimpleInventory upgrades = new SimpleInventory(6) {
        @Override
        public void markDirty() {
            super.markDirty();
            updateUpgrades();
        }
    };

    private final InventoryStorage upgradeStorage = InventoryStorage.of(upgrades, null);

    public UpgradeStationBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.UPGRADE_STATION_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if (nbt.contains("input")) {
            Inventories.readNbt(nbt.getCompound("input"), this.input.getHeldStacks(), registryLookup);
        }
        if (nbt.contains("upgrades")) {
            Inventories.readNbt(nbt.getCompound("upgrades"), this.upgrades.getHeldStacks(), registryLookup);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        NbtCompound inputNbt = new NbtCompound();
        Inventories.writeNbt(inputNbt, this.input.getHeldStacks(), registryLookup);
        nbt.put("input", inputNbt);

        NbtCompound upgradesNbt = new NbtCompound();
        Inventories.writeNbt(upgradesNbt, this.upgrades.getHeldStacks(), registryLookup);
        nbt.put("upgrades", upgradesNbt);
    }

    @Override
    public BlockPosPayload getScreenOpeningData(ServerPlayerEntity player) {
        return new BlockPosPayload(this.pos);
    }

    @Override
    public Text getDisplayName() {
        return SCREEN_NAME_1;
    }

    public Text getDisplayName2() {
        return SCREEN_NAME_2;
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new UpgradeStationScreenHandler(syncId, playerInventory, this);
    }

    private void updateInput() {
        markDirty();
        if (world != null)
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        contentsChangedListenerInput.run();
    }

    private void updateUpgrades() {
        markDirty();
        if (world != null)
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        contentsChangedListenerUpgrades.run();
    }

    public InventoryStorage getInventoryProvider(Direction direction) {
        return inventoryStorage;
    }

    public SimpleInventory getUpgrades() {
        return this.upgrades;
    }

    public InventoryStorage getUpgradeInventoryProvider(Direction direction) {
        return upgradeStorage;
    }

    public SimpleInventory getInput() {
        return this.input;
    }

    public void setContentsChangedListenerForInput(Runnable contentsChangedListener) {
        this.contentsChangedListenerInput = contentsChangedListener;
    }

    public void setContentsChangedListenerForUpgrades(Runnable contentsChangedListener) {
        this.contentsChangedListenerUpgrades = contentsChangedListener;
    }
}
