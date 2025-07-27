package net.togyk.myneheroes.ability;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.togyk.myneheroes.client.screen.handeler.ToolbeltAbilityScreenHandler;
import net.togyk.myneheroes.networking.ToolBeltScreenAbilityPayload;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ToolbeltAbility extends Ability implements ExtendedScreenHandlerFactory<ToolBeltScreenAbilityPayload> {
    private NbtList inventory = new NbtList();
    protected final int size;

    public ToolbeltAbility(Identifier id, Settings settings, int size) {
        super(id, 0, settings, null);
        this.size = size;
    }

    @Override
    public void Use(PlayerEntity player) {
        if (getCooldown() == 0) {
            player.openHandledScreen(this);
            save();
        }
    }

    @Override
    public ToolBeltScreenAbilityPayload getScreenOpeningData(ServerPlayerEntity player) {
        return new ToolBeltScreenAbilityPayload(this);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("ability."+this.getId());
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ToolbeltAbilityScreenHandler(syncId, player, this);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        if (inventory != null) {
            nbt.put("inventory", inventory);
        }
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("inventory")) {
            this.inventory = nbt.getList("inventory", NbtElement.COMPOUND_TYPE);
        }
    }

    public void setInventory(List<ItemStack> inventory, World world) {
        NbtList inventoryNbt = new NbtList();
        for (ItemStack stack : inventory) {
            if (stack != null) {
                inventoryNbt.add(stack.encodeAllowEmpty(world.getRegistryManager()));
            }
        }
        this.inventory = inventoryNbt;
    }

    public List<ItemStack> getInventory(World world) {
        if (inventory.isEmpty()) {
            return DefaultedList.ofSize(size, ItemStack.EMPTY);
        } else {
            List<ItemStack> inventoryList = new ArrayList<>();
            for (NbtElement nbtElement : this.inventory) {
                if (nbtElement instanceof NbtCompound nbtCompound) {
                    ItemStack stack = ItemStack.fromNbtOrEmpty(world.getRegistryManager(), nbtCompound);
                    inventoryList.add(stack);
                }
            }
            return inventoryList;
        }
    }

    public void swapItem(PlayerEntity player, int index) {
        List<ItemStack> inventory = this.getInventory(player.getWorld());
        ItemStack selectedStack = inventory.get(index);
        ItemStack playerStack = player.getStackInHand(Hand.MAIN_HAND);

        player.setStackInHand(Hand.MAIN_HAND, selectedStack);
        inventory.set(index, playerStack);
        this.setInventory(inventory, player.getWorld());
        this.save();
    }

    @Override
    public void setHolder(@Nullable AbilityHoldingAbility holder) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ToolbeltAbility copy() {
        return new ToolbeltAbility(id, settings, size);
    }
}
