package net.togyk.myneheroes.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.Powers;
import net.togyk.myneheroes.util.PlayerPowers;
import net.togyk.myneheroes.util.PowerData;
import net.togyk.myneheroes.util.PowerUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerPowerMixin implements PlayerPowers {
    private List<Power> powers = new ArrayList<>();
    private boolean isDirty = false;

    private int scrolledPowerOffset = 0;

    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (this.isDirty) {
            PowerData.setPowers(player, powers);
            this.isDirty = false;
        }
        List<Power> powers = PowerData.getPowers(player);
        for (Power power : powers) {
            if (power != null) {
                power.tick(player);
                power.updateAttributes(player.getAttributes());
            }
        }
        if (this.scrolledPowerOffset > this.maxPowerScroll()) {
            this.scrolledPowerOffset = this.maxPowerScroll();
        } else if (scrolledPowerOffset < 0) {
            this.scrolledPowerOffset = 0;
        }
    }

    @Inject(at = @At("HEAD"), method = "readCustomDataFromNbt")
    private void readFromNbt(NbtCompound nbt, CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        List<Power> powers = new ArrayList<>();

        if (nbt.contains(MyneHeroes.MOD_ID)) {
            NbtCompound modNbt = nbt.getCompound(MyneHeroes.MOD_ID);
            if (modNbt.contains("powers")) {
                NbtList powerNbt = modNbt.getList("powers", NbtElement.COMPOUND_TYPE);
                for (NbtElement nbtElement : powerNbt) {
                    if (nbtElement instanceof NbtCompound nbtCompound) {
                        Identifier powerId = Identifier.of(nbtCompound.getString("id"));
                        Power power = Powers.get(powerId);
                        if (power != null) {
                            power.readNbt(nbtCompound);
                        }
                        power.setHolder(player);
                        powers.add(power);
                    }
                }
            }
            if (modNbt.contains("scrolled_power_offset")) {
                this.scrolledPowerOffset = modNbt.getInt("scrolled_power_offset");
            }
        }
        this.powers = powers;
        this.isDirty = true;
    }
    @Inject(at = @At("HEAD"), method = "writeCustomDataToNbt")
    private void writeToNbt(NbtCompound nbt, CallbackInfo info) {
        NbtCompound modNbt = new NbtCompound();
        if (nbt.contains(MyneHeroes.MOD_ID)) {
            modNbt = nbt.getCompound(MyneHeroes.MOD_ID);
        }

        NbtList powerNbt = new NbtList();
        for (Power power : this.powers) {
            if (power != null) {
                NbtCompound powerCompound = power.writeNbt(new NbtCompound());
                powerNbt.add(powerCompound);
            }
        }
        modNbt.put("powers",powerNbt);

        modNbt.putInt("scrolled_power_offset",this.scrolledPowerOffset);

        nbt.put(MyneHeroes.MOD_ID,modNbt);
    }

    public List<Power> getPowers() {
        return this.powers;
    }
    public void setPowers(List<Power> powers) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        for (Power power : this.powers) {
            power.removeAttributes(player.getAttributes());
            power.setHolder(null);
        }
        this.powers = powers;
        for (Power power : powers) {
            power.applyAttributes(player.getAttributes());
            power.setHolder(player);
        }
    }
    public void removePower(Power power) {
        this.powers.remove(power);
        PlayerEntity player = (PlayerEntity) (Object) this;
        power.removeAttributes(player.getAttributes());
        power.setHolder(null);
    }
    public void addPower(Power power) {
        if (!this.powers.contains(power)) {
            this.powers.add(power);
            PlayerEntity player = (PlayerEntity) (Object) this;
            power.applyAttributes(player.getAttributes());
            power.setHolder(player);
        }
    }

    public double getDamageMultiplier() {
        Double multiplier = 1.00;
        for (Power power : this.powers) {
            multiplier *= power.getDamageMultiplier();
        }
        return multiplier;
    }

    @Override
    public int getScrolledPowerOffset() {
        return Math.max(scrolledPowerOffset, 0);
    }

    @Override
    public int maxPowerScroll() {
        return this.powers.size() - 1;
    }

    @Override
    public void setScrolledPowerOffset(int scrolledOffset) {
        this.scrolledPowerOffset = scrolledOffset;
    }

    @Override
    public void scrollPowerFurther() {
        if (this.scrolledPowerOffset < maxPowerScroll()) {
            this.scrolledPowerOffset += 1;
        } else {
            this.scrolledPowerOffset = 0;
        }
    }

    @Override
    public void scrollPowerBack() {
        if (this.scrolledPowerOffset > 0) {
            this.scrolledPowerOffset -= 1;
        } else {
            this.scrolledPowerOffset = maxPowerScroll();
        }
    }
}