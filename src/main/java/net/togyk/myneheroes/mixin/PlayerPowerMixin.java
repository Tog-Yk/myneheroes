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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.max;
import static java.util.Collections.min;

@Mixin(PlayerEntity.class)
public abstract class PlayerPowerMixin implements PlayerPowers {
    private List<Power> powers = new ArrayList<>();
    private boolean isDirty = false;


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
                power.tick();
            }
        }

        if (!powers.isEmpty()) {
            player.getAbilities().allowFlying = true;
        } else if (!(player.isInCreativeMode() || player.isSpectator())) {
            player.getAbilities().allowFlying = false;
            player.getAbilities().flying = false;
        }
    }

    @Inject(at = @At("HEAD"), method = "readCustomDataFromNbt")
    private void readFromNbt(NbtCompound nbt, CallbackInfo info) {
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
                        powers.add(power);
                    }
                }
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
        for (Power power : PowerData.getPowers((PlayerEntity) (Object) this)) {
            if (power != null) {
                NbtCompound powerCompound = power.writeNbt(new NbtCompound());
                powerNbt.add(powerCompound);
            }
        }
        modNbt.put("powers",powerNbt);
        nbt.put(MyneHeroes.MOD_ID,modNbt);
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
        List<Float> multipliers = new ArrayList<>();
        for (Power power : this.powers) {
            multipliers.add(power.getDamageMultiplier());
        }
        if (!multipliers.isEmpty()) {
            return max(multipliers);
        } else {
            return 1.0F;
        }
    }

    public float getResistance() {
        List<Float> multipliers = new ArrayList<>();
        for (Power power : this.powers) {
            if (power != null) {
                multipliers.add(power.getResistance());
            }
        }
        if (!multipliers.isEmpty()) {
            return min(multipliers);
        } else {
            return 1.0F;
        }
    }
}