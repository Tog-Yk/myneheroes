package net.togyk.myneheroes.upgrade;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.togyk.myneheroes.registry.ModRegistries;

import java.util.List;
import java.util.Optional;

public abstract class Upgrade {
    protected final Identifier id;

    private ItemStack holderStack;
    private NbtCompound itemStack = new NbtCompound();

    protected final List<ArmorItem.Type> compatibleTypes;

    protected Upgrade(List<ArmorItem.Type> compatibleTypes, Identifier id) {
        this.compatibleTypes = compatibleTypes;
        this.id = id;
    }

    public Identifier getId() {
        return id;
    }

    public List<ArmorItem.Type> getCompatibleTypes() {
        return compatibleTypes;
    }

    public void setHolderStack(ItemStack holderStack) {
        this.holderStack = holderStack;
    }

    public ItemStack getHolderStack() {
        return holderStack;
    }

    public void setItemStack(ItemStack itemStack, World world) {
        this.itemStack = (NbtCompound) itemStack.encodeAllowEmpty(world.getRegistryManager());
    }

    public ItemStack getItemStack(World world) {
        return ItemStack.fromNbtOrEmpty(world.getRegistryManager(), this.itemStack);
    }

    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        return false;
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putString("id", this.id.toString());

        nbt.put("itemStack", this.itemStack);
        return nbt;
    }

    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("itemStack")) {
            this.itemStack = nbt.getCompound("itemStack");
        }
    }

    public abstract Upgrade copy();

    public final boolean isIn(TagKey<Upgrade> tag) {
        Optional<RegistryEntryList.Named<Upgrade>> registryEntries = ModRegistries.UPGRADE.getEntryList(tag);
        if (registryEntries.isPresent()) {
            List<Identifier> ids = registryEntries.get().stream().map(entry -> entry.value().getId()).toList();
            return ids.contains(this.getId());
        }
        return false;
    }
}
