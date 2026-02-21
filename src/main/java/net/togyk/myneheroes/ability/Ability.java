package net.togyk.myneheroes.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.AbilityHolding;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.registry.ModRegistries;
import net.togyk.myneheroes.upgrade.AbilityUpgrade;
import net.togyk.myneheroes.upgrade.Upgrade;
import net.togyk.myneheroes.util.AbilityUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
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
    protected final int maxHoldTime;
    private int holdTime = 0;

    protected final Function<PlayerEntity, Boolean> use;
    protected Function<PlayerEntity, Boolean> hold;
    protected final Settings settings;



    public Ability(Identifier id, int cooldown, Settings settings, @Nullable Function<PlayerEntity, Boolean> use, Function<PlayerEntity, Boolean> hold, int maxHoldTime) {
        this.id = id;
        this.maxCooldown = cooldown;
        this.use = use;
        this.hold = hold;
        this.maxHoldTime = maxHoldTime;
        this.settings = settings;
    }

    public Ability(Identifier id, int cooldown, Settings settings, @Nullable Function<PlayerEntity, Boolean> use, Function<PlayerEntity, Boolean> hold) {
        this(id, cooldown, settings, use, hold, 0);
    }

    public Ability(Identifier id, int cooldown, Settings settings, Function<PlayerEntity, Boolean> use) {
        this(id, cooldown, settings, use, null);
    }

    public void pressed(PlayerEntity player) {
        if (use != null) {
            if (this.getCooldown() == 0) {
                if (this.use.apply(player)) {
                    this.setCooldown(this.getMaxCooldown());
                }
            }
            this.save();
        }
    }

    public void held(PlayerEntity player) {
        if (this.hold != null) {
            if (this.getCooldown() == 0) {
                if (this.getMaxHoldTime() != 0) {
                    int holdTime = this.getHoldTime();
                    if (holdTime > this.getMaxHoldTime()) {
                        this.setHoldTime(this.getMaxCooldown());
                        this.save();
                    }
                    if (holdTime >= this.getMaxHoldTime()) {
                        this.setCooldown(this.getMaxCooldown());
                        this.released(player);
                        return;
                    } else {
                        this.setHoldTime(holdTime + 1);
                        this.save();
                    }
                }


                if (this.hold.apply(player)) {
                    this.setCooldown(this.getMaxCooldown());
                }
            }
            this.save();
        }
    }

    public void released(PlayerEntity player) {
        this.setHoldTime(0);
        if (this.cooldownWhenReleased()) {
            this.setCooldown((this.getMaxCooldown()));
        }
        this.save();
    }

    //gets called if the player doesn't press the ability
    public void notPressed(PlayerEntity player) {
    }

    public boolean canHold(PlayerEntity player) {
        return this.hold != null && this.cooldown == 0 && (this.getMaxHoldTime() == 0 || this.getHoldTime() < this.getMaxHoldTime());
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
        } else if (holder instanceof PassiveSelectionAbility ability) {
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

    public int getHoldTime() {
        return holdTime;
    }

    public void setHoldTime(int holdTime) {
        this.holdTime = holdTime;
    }

    public int getMaxHoldTime() {
        return maxHoldTime;
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

    public Entity getEntityHolder() {
        Object holder = this.getIndirectHolder();
        if (holder instanceof ItemStack stack) {
            return stack.getHolder();
        } else if (holder instanceof Power power) {
            return power.getHolder();
        } else if (holder instanceof Upgrade upgrade) {
            return upgrade.getHolderStack().getHolder();
        }
        return null;
    }

    public boolean isHidden() {
        return settings.hidden;
    }

    public boolean cooldownWhenReleased() {
        return settings.cooldownWhenReleased;
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putString("id", this.id.toString());
        nbt.putInt("cooldown", this.cooldown);
        nbt.putInt("hold_time", this.getHoldTime());
        return nbt;
    }

    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("cooldown")) {
            this.cooldown = nbt.getInt("cooldown");
        }
        if (nbt.contains("hold_time")) {
            this.setHoldTime(nbt.getInt("hold_time"));
        }
    }

    public Identifier getIcon() {
        Identifier id = this.getId();
        return Identifier.of(id.getNamespace(),"textures/ability/"+id.getPath()+".png");
    }

    public Text getName() {
        return Text.translatable("ability."+this.getId().toTranslationKey());
    }

    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
    }

    public boolean appearsMultipleTimes() {
        return settings.appearsMultipleTimes;
    }

    public Ability copy() {
        return new Ability(id, maxCooldown, settings, use, hold, maxHoldTime);
    }

    public static class Settings{
        public boolean appearsMultipleTimes = true;
        public boolean hidden = true;
        public boolean cooldownWhenReleased = false;

        public Settings() {
        }

        public Ability.Settings appearsMultipleTimes(boolean bl) {
            this.appearsMultipleTimes = bl;
            return this;
        }

        public Ability.Settings hidden(boolean bl) {
            this.hidden = bl;
            return this;
        }

        public Ability.Settings cooldownWhenReleased(boolean bl) {
            this.cooldownWhenReleased = bl;
            return this;
        }
    }

    public Identifier getId() {
        return id;
    }

    public final boolean isIn(TagKey<Ability> tag) {
        Optional<RegistryEntryList.Named<Ability>> registryEntries = ModRegistries.ABILITY.getEntryList(tag);
        if (registryEntries.isPresent()) {
            List<Identifier> ids = registryEntries.get().stream().map(entry -> entry.value().getId()).toList();
            return ids.contains(this.getId());
        }
        return false;
    }
}
