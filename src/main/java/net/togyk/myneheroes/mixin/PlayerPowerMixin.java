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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.max;

@Mixin(PlayerEntity.class)
public abstract class PlayerPowerMixin implements PlayerPowers {
    private List<Power> powers = new ArrayList<>();

    @Inject(at = @At("HEAD"), method = "readCustomDataFromNbt")
    private void readFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains(MyneHeroes.MOD_ID)) {
            NbtList powerNbt = nbt.getList(MyneHeroes.MOD_ID, NbtElement.COMPOUND_TYPE);
            for (NbtElement nbtElement : powerNbt) {
                if (nbtElement instanceof NbtCompound nbtCompound) {
                    Identifier powerId = Identifier.of(nbtCompound.getString("power"));
                    Power power = Powers.get(powerId);
                    power.readNbt(nbtCompound);
                    powers.add(power);
                }
            }
        }
    }
    @Inject(at = @At("HEAD"), method = "writeCustomDataToNbt")
    private void writeToNbt(NbtCompound nbt, CallbackInfo info) {
        NbtList powerNbt = new NbtList();
        for (Power power : powers) {
            NbtCompound powerCompound = power.getNbt();
            Identifier powerId = Powers.getFirstMatchingId(power);
            if (powerId != null) {
                powerCompound.putString("power", powerId.toString());
                powerNbt.add(powerCompound);
            }
        }
        nbt.put(MyneHeroes.MOD_ID,powerNbt);
    }

    public List<Power> getPowers() {
        return this.powers;
    }
    public void setPowers(List<Power> powers) {
        this.powers = powers;
    }
    public void removePower(Power power) {
        this.powers.remove(power);
    }
    public void addPower(Power power) {
        if (!this.powers.contains(power)) {
            this.powers.add(power);
        }
    }

    public float getDamageMultiplier() {
        List<Integer> multipliers = new ArrayList<>();
        for (Power power : this.powers) {
            multipliers.add(power.getDamageMultiplier());
        }
        if (!multipliers.isEmpty()) {
            return max(multipliers);
        } else {
            return 1.0F;
        }
    }
}