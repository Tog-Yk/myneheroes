package net.togyk.myneheroes.ability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.AbilityHolding;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class Ability {
    private Power HolderPower;
    private ItemStack HolderItem;

    public final Identifier id;
    protected final int maxCooldown;
    public int cooldown;

    public final Identifier icon;
    public final Identifier disabled_icon;

    protected final Function<PlayerEntity, Boolean> use;
    protected final Settings settings;

    public Ability(Identifier id, int cooldown, Settings settings, Function<PlayerEntity, Boolean> use) {
        this.id = id;
        this.icon = Identifier.of(id.getNamespace(),"textures/ability/"+id.getPath()+".png");
        this.disabled_icon = Identifier.of(id.getNamespace(),"textures/ability/"+id.getPath()+"_disabled.png");
        this.maxCooldown = cooldown;
        this.use = use;
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

    public void tick() {
        if (this.getCooldown() != 0) {
            this.setCooldown(this.getCooldown() - 1);
        }
        if (this.getCooldown() < 0) {
            this.setCooldown(0);
        }
        this.save();
    }

    public void save() {
        if (this.getHolderItem() instanceof ItemStack stack && stack.getItem() instanceof AbilityHolding holding) {
            holding.saveAbility(stack, this);
        } else if (this.getHolderItem() instanceof Power power) {
            power.saveAbility(this);
        }
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int integer) {
        cooldown = integer;
    }

    public int getMaxCooldown() {
        return maxCooldown;
    }

    public void setHolder(@Nullable ItemStack holder) {
        this.HolderItem = holder;
        this.HolderPower = null;
    }

    public void setHolder(@Nullable Power holder) {
        this.HolderItem = null;
        this.HolderPower = holder;
    }

    /*
    returns a power or an ItemStack based on the current holder
    returns null if there is no holder set
     */
    public Object getHolderItem() {
        if (HolderItem != null) {
            return HolderItem;
        } else {
            return HolderPower;
        }
    }

    public boolean Usable() {
        return settings.usable;
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putString("id", this.id.toString());
        nbt.putInt("cooldown", this.getCooldown());
        return nbt;
    }

    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("cooldown")) {
            this.setCooldown(nbt.getInt("cooldown"));
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
