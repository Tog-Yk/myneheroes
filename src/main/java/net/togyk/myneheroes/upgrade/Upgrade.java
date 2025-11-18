package net.togyk.myneheroes.upgrade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import net.minecraft.util.ErrorReporter;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.togyk.myneheroes.registry.ModRegistries;
import net.togyk.myneheroes.util.AbilityUtil;

import java.util.List;
import java.util.Optional;

public abstract class Upgrade {
    public static final Codec<Upgrade> CODEC = Codec.PASSTHROUGH
            .xmap(
                    dynamic -> AbilityUtil.nbtToUpgrade((NbtCompound) dynamic.convert(NbtOps.INSTANCE).getValue()),
                    upgrade -> new Dynamic<>(NbtOps.INSTANCE, upgrade.writeNbt(new NbtCompound()))
            );

    public static final PacketCodec<ByteBuf, Upgrade> PACKET_CODEC = new PacketCodec<>() {
        public Upgrade decode(ByteBuf buf) {
            return AbilityUtil.nbtToUpgrade(PacketCodecs.NBT_COMPOUND.decode(buf));
        }

        public void encode(ByteBuf buf, Upgrade upgrade) {
            PacketCodecs.NBT_COMPOUND.encode(buf, upgrade.writeNbt(new NbtCompound()));
        }
    };

    protected final Identifier id;

    private ItemStack holderStack;
    private ItemStack itemStack = null;

    protected final List<EquipmentType> compatibleTypes;

    protected Upgrade(List<EquipmentType> compatibleTypes, Identifier id) {
        this.compatibleTypes = compatibleTypes;
        this.id = id;
    }

    public Identifier getId() {
        return id;
    }

    public List<EquipmentType> getCompatibleTypes() {
        return compatibleTypes;
    }

    public void setHolderStack(ItemStack holderStack) {
        this.holderStack = holderStack;
    }

    public ItemStack getHolderStack() {
        return holderStack;
    }

    public void setItemStack(ItemStack itemStack, World world) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack(World world) {
        return this.itemStack;
    }

    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        return false;
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putString("id", this.id.toString());


        nbt.put("itemStack", ItemStack.CODEC, this.itemStack);

        return nbt;
    }

    public void readNbt(NbtCompound nbt) {
        if (nbt.get("itemStack", ItemStack.CODEC).isPresent()) {
            this.itemStack = nbt.get("itemStack", ItemStack.CODEC).get();
        }
    }

    public ErrorReporter.Context getErrorReporterContext() {
        return new Upgrade.ErrorReporterContext(this);
    }

    record ErrorReporterContext(Upgrade upgrade) implements ErrorReporter.Context {
        @Override
        public String getName() {
            return this.upgrade.getId().toString();
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
