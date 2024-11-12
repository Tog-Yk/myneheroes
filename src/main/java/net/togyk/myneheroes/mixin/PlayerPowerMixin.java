package net.togyk.myneheroes.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.power.ModPowers;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.PowerHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Objects;

@Mixin(ServerPlayerEntity.class)
public abstract class PlayerPowerMixin implements PowerHolder {
    @Unique
    private Power currentPower;

    /**
     * Ticking method that runs every game tick.
     */
    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo info) {
        if ((Object) this != null) {
            currentPower.tick((ServerPlayerEntity) (Object) this);
        }
    }

    /**
     * Save powers to NBT when the player data is saved.
     */
    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void savePowersToNbt(NbtCompound nbt, CallbackInfo info) {
        NbtCompound powersNbt = new NbtCompound();
        currentPower.saveToNbt(powersNbt);
        powersNbt.putString("name", Objects.requireNonNull(ModPowers.POWER.getId(currentPower)).toString());
        nbt.put(MyneHeroes.MOD_ID, powersNbt);
    }

    /**
     * Load powers from NBT when the player data is loaded.
     */
    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void loadPowersFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("Powers")) {
            NbtCompound powersNbt = nbt.getCompound("Powers");
            Identifier powerIdentifier = Identifier.of(MyneHeroes.MOD_ID, powersNbt.getString("name"));
            if (ModPowers.POWER.containsId(powerIdentifier)) {
                this.currentPower = ModPowers.POWER.get(powerIdentifier);
                assert currentPower != null;
                currentPower.loadFromNbt(powersNbt);
            } else {
                this.currentPower = null;
            }
        }
    }

    @Override
    public Power getPower() {
        return currentPower;
    }

    @Override
    public void setPower(Power power) {
        this.currentPower = power;
    }

    @Override
    public void activatePower(PlayerEntity player) {
        if (currentPower != null && player instanceof ServerPlayerEntity) {
            currentPower.SwitchState(true);
        }
    }

    @Override
    public void deactivatePower(PlayerEntity player) {
        if (currentPower != null && player instanceof ServerPlayerEntity) {
            currentPower.SwitchState(false);
        }
    }
}
