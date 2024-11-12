package net.togyk.myneheroes.power;

import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

public interface PowerHolder {
    @Nullable
    Power getPower();
    void setPower(Power power);
    void activatePower(PlayerEntity player);
    void deactivatePower(PlayerEntity player);
}