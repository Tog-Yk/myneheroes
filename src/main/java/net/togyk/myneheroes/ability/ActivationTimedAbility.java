package net.togyk.myneheroes.ability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class ActivationTimedAbility extends Ability {
    protected final int maxActivatedTime;
    private boolean activated = false;
    protected int activatedTime = 0;

    public ActivationTimedAbility(Identifier id, int cooldown, int maxActivatedTime, Settings settings) {
        super(id, cooldown, settings, (player) -> false);
        this.maxActivatedTime = maxActivatedTime;
    }


    @Override
    public void pressed(PlayerEntity player) {
        //switch the boolean
        if (this.isActivated()) {
            this.setActivated(false);
            this.setActivatedTime(0);
            this.setCooldown(this.getMaxCooldown());
        } else if (getCooldown() == 0) {
            this.setActivated(true);
        }
        this.save();
    }

    @Override
    public void tick(PlayerEntity player) {
        if (this.isActivated()) {
            int activatedTime = this.getActivatedTime();
            if (activatedTime >= this.getMaxActivatedTime()) {
                this.setActivated(false);
                this.setActivatedTime(0);
                this.setCooldown(this.getMaxCooldown());
                this.save();
            } else {
                this.setActivatedTime(activatedTime + 1);
                this.save();
            }
            if (activatedTime > this.getMaxActivatedTime()) {
                this.setActivatedTime(this.getMaxCooldown());
                this.save();
            }
        } else {
            int cooldown = this.getCooldown();
            if (cooldown > 0) {
                this.setCooldown(cooldown - 1);
                this.save();
            }
            if (cooldown < 0) {
                this.setCooldown(0);
                this.save();
            }
        }
    }

    public int getMaxActivatedTime() {
        return maxActivatedTime;
    }

    public int getActivatedTime() {
        return activatedTime;
    }

    public void setActivatedTime(int activatedTime) {
        this.activatedTime = activatedTime;
    }

    public boolean isActivated() {
        return this.activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putBoolean("activated", this.activated);
        nbt.putInt("activated_time", this.activatedTime);
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("activated")) {
            this.activated = nbt.getBoolean("activated");
        }
        if (nbt.contains("activated_time")) {
            this.activatedTime = nbt.getInt("activated_time");
        }
        super.readNbt(nbt);
    }


    @Override
    public ActivationTimedAbility copy() {
        return new ActivationTimedAbility(id, maxCooldown, maxActivatedTime, settings);
    }
}
