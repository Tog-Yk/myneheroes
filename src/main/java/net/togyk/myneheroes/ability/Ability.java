package net.togyk.myneheroes.ability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.AbilityHolding;
import org.jetbrains.annotations.Nullable;

public abstract class Ability {
    private Power HolderPower;
    private ItemStack HolderItem;

    public final Identifier id;
    private final String abilityName;
    private final int maxCooldown;
    public int cooldown;

    public final Identifier icon;
    public final Identifier disabled_icon;

    public Ability(Identifier id, String name,int cooldown) {
        this.id = id;
        this.abilityName = name;
        this.icon = Identifier.of(MyneHeroes.MOD_ID,"textures/ability/"+name+".png");
        this.disabled_icon = Identifier.of(MyneHeroes.MOD_ID,"textures/ability/"+name+"_disabled.png");
        this.maxCooldown = cooldown;
    }

    public void Use(PlayerEntity player) {
        if (this.getCooldown() == 0) {
            this.setCooldown(this.getMaxCooldown());
        }
        if (this.HolderItem != null && this.HolderItem.getItem() instanceof AbilityHolding holding) {
            holding.saveAbility(this.HolderItem, this);
        } else if (this.HolderPower != null) {
            this.HolderPower.saveAbility(this);
        }
    }

    public void tick() {
        if (this.getCooldown() != 0) {
            this.setCooldown(this.getCooldown() - 1);
        }
        if (this.getCooldown() < 0) {
            this.setCooldown(0);
        }
        if (this.HolderItem != null && this.HolderItem.getItem() instanceof AbilityHolding holding) {
            holding.saveAbility(this.HolderItem, this);
        } else if (this.HolderPower != null) {
            this.HolderPower.saveAbility(this);
        }
    }

    public String getName() {
        return abilityName;
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

    public abstract Ability copy();

    public Identifier getId() {
        return id;
    }
}
