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
import net.togyk.myneheroes.client.screen.handeler.ArmorDyeingScreenHandler;
import net.togyk.myneheroes.networking.BlockPosPayload;
import org.jetbrains.annotations.Nullable;

public class ArmorDyeingBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPosPayload> {
    public static final Text SCREEN_NAME = Text.translatable("container." + MyneHeroes.MOD_ID + ".armor_dyeing_station");

    Runnable contentsChangedListener = () -> {
    };

    private final SimpleInventory inventory = new SimpleInventory(1) {
        @Override
        public void markDirty() {
            super.markDirty();
            update();
        }
    };

    private final InventoryStorage inventoryStorage = InventoryStorage.of(inventory, null);

    public ArmorDyeingBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.ARMOR_DYEING_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, this.inventory.getHeldStacks(), registryLookup);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.writeNbt(nbt, this.inventory.getHeldStacks(), registryLookup);
        super.writeNbt(nbt, registryLookup);
    }

    @Override
    public BlockPosPayload getScreenOpeningData(ServerPlayerEntity player) {
        return new BlockPosPayload(this.pos);
    }

    @Override
    public Text getDisplayName() {
        return SCREEN_NAME;
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ArmorDyeingScreenHandler(syncId, playerInventory, this);
    }

    private void update() {
        markDirty();
        if (world != null)
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        contentsChangedListener.run();
    }

    public InventoryStorage getInventoryProvider(Direction direction) {
        return inventoryStorage;
    }

    public SimpleInventory getInventory() {
        return this.inventory;
    }

    public void setContentsChangedListener(Runnable contentsChangedListener) {
        this.contentsChangedListener = contentsChangedListener;
    }
}
