package net.togyk.myneheroes.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.Item.custom.AbilityHolding;
import net.togyk.myneheroes.upgrade.AbilityUpgrade;
import net.togyk.myneheroes.upgrade.Upgrade;
import net.togyk.myneheroes.util.AbilityUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class Ability {
    public static final Codec<Ability> CODEC = Codec.PASSTHROUGH
            .xmap(
                    dynamic -> AbilityUtil.nbtToAbility((NbtCompound) dynamic.convert(NbtOps.INSTANCE).getValue()),
                    ability -> new Dynamic<>(NbtOps.INSTANCE, ability.writeNbt(new NbtCompound()))
            );

    public static final PacketCodec<ByteBuf, Ability> PACKET_CODEC = new PacketCodec<>() {
        public Ability decode(ByteBuf buf) {
            return AbilityUtil.nbtToAbility(PacketCodecs.NBT_COMPOUND.decode(buf));
        }

        public void encode(ByteBuf buf, Ability ability) {
            PacketCodecs.NBT_COMPOUND.encode(buf, ability.writeNbt(new NbtCompound()));
        }
    };

    private Power HolderPower;
    private ItemStack HolderItem;
    private AbilityHoldingAbility HolderAbility;
    private Upgrade HolderUpgrade;

    public final Identifier id;
    protected final int maxCooldown;
    private int cooldown;

    public final Identifier icon;
    public final Identifier disabled_icon;

    protected final Function<PlayerEntity, Boolean> use;
    protected final Function<PlayerEntity, Boolean> hold;
    protected final Settings settings;

    public Ability(Identifier id, int cooldown, Settings settings, Function<PlayerEntity, Boolean> use) {
        this.id = id;
        this.icon = Identifier.of(id.getNamespace(),"textures/ability/"+id.getPath()+".png");
        this.disabled_icon = Identifier.of(id.getNamespace(),"textures/ability/"+id.getPath()+"_disabled.png");
        this.maxCooldown = cooldown;
        this.use = use;
        this.hold = null;
        this.settings = settings;
    }

    public Ability(Identifier id, int cooldown, Settings settings, Function<PlayerEntity, Boolean> use, Function<PlayerEntity, Boolean> hold) {
        this.id = id;
        this.icon = Identifier.of(id.getNamespace(),"textures/ability/"+id.getPath()+".png");
        this.disabled_icon = Identifier.of(id.getNamespace(),"textures/ability/"+id.getPath()+"_disabled.png");
        this.maxCooldown = cooldown;
        this.use = use;
        this.hold = hold;
        this.settings = settings;
    }

    public void Use(PlayerEntity player) {
        if (this.getCooldown() == 0) {
            if (this.use.apply(player)) {
                this.setCooldown(this.getMaxCooldown());
            }
        }
        this.save();
    }

    public void hold(PlayerEntity player) {
        if (this.hold != null) {
            if (this.getCooldown() == 0) {
                if (this.hold.apply(player)) {
                    this.setCooldown(this.getMaxCooldown());
                }
            }
            this.save();
        }
    }

    public boolean canHold(PlayerEntity player) {
        return this.hold != null;
    }

    public void tick(PlayerEntity player) {
        if (this.getCooldown() != 0) {
            this.setCooldown(this.getCooldown() - 1);
        }
        if (this.getCooldown() < 0) {
            this.setCooldown(0);
        }
        this.save();
    }

    public void save() {
        Object holder = this.getHolder();
        if (holder instanceof ItemStack stack) {
            if (stack.getItem() instanceof AbilityHolding holding) {
                holding.saveAbility(stack, this);
            }
        } else if (holder instanceof Power power) {
            power.saveAbility(this);
        } else if (holder instanceof SelectionAbility ability) {
            ability.saveAbility(this);
        } else if (holder instanceof AbilityUpgrade upgrade) {
            upgrade.saveAbility(this);
        }
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public void setCooldown(int integer) {
        this.cooldown = integer;
    }

    public int getMaxCooldown() {
        return maxCooldown;
    }

    public void setHolder(@Nullable ItemStack holder) {
        this.HolderItem = holder;
        this.HolderPower = null;
        this.HolderAbility = null;
        this.HolderUpgrade = null;
    }

    public void setHolder(@Nullable Power holder) {
        this.HolderItem = null;
        this.HolderPower = holder;
        this.HolderAbility = null;
        this.HolderUpgrade = null;
    }

    public void setHolder(@Nullable AbilityHoldingAbility holder) {
        this.HolderItem = null;
        this.HolderPower = null;
        this.HolderAbility = holder;
        this.HolderUpgrade = null;
    }

    public void setHolder(@Nullable Upgrade upgrade) {
        this.HolderItem = null;
        this.HolderPower = null;
        this.HolderAbility = null;
        this.HolderUpgrade = upgrade;
    }

    /*
    returns a power or an ItemStack based on the current holder
    returns null if there is no holder set
     */
    public Object getIndirectHolder() {
        if (HolderItem != null) {
            return HolderItem;
        } else if (HolderAbility != null) {
            return HolderAbility.getIndirectHolder();
        } else if (HolderPower != null) {
            return HolderPower;
        } else {
            return HolderUpgrade;
        }
    }

    /*
    returns a Power, ItemStack or SelectionAbility based on the current holder
    returns null if there is no holder set
     */
    public Object getHolder() {
        if (HolderItem != null) {
            return HolderItem;
        } else if (HolderAbility != null) {
            return HolderAbility;
        } else if (HolderPower != null) {
            return HolderPower;
        } else {
            return HolderUpgrade;
        }
    }

    public boolean Usable() {
        return settings.usable;
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putString("id", this.id.toString());
        nbt.putInt("cooldown", this.cooldown);
        return nbt;
    }

    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("cooldown")) {
            this.cooldown = nbt.getInt("cooldown");
        }
    }

    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
    }

    public boolean appearsMultipleTimes() {
        return settings.appearsMultipleTimes;
    }

    public Ability copy() {
        return new Ability(id, maxCooldown, settings, use);
    }

    public static class Settings{
        public boolean appearsMultipleTimes = true;
        public boolean usable = true;

        public Settings() {
        }

        public Ability.Settings appearsMultipleTimes(boolean bl) {
            this.appearsMultipleTimes = bl;
            return this;
        }

        public Ability.Settings usable(boolean bl) {
            this.usable = bl;
            return this;
        }
    }

    public Identifier getId() {
        return id;
    }
}
