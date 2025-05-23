package net.togyk.myneheroes.ability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class BooleanAbility extends Ability{
    private boolean bool = true;
    public BooleanAbility(Identifier id, Settings settings) {
        super(id, 10, settings, (player) -> false);
    }

    @Override
    public void Use(PlayerEntity player) {
        //switch the boolean
        if (getCooldown() == 0) {
            bool = !bool;
            this.setCooldown(this.getMaxCooldown());
        }
        this.save();
    }

    public boolean get() {
        return bool;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putBoolean("bool", this.get());
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("bool")) {
            this.bool = nbt.getBoolean("bool");
        }
        super.readNbt(nbt);
    }

    @Override
    public BooleanAbility copy() {
        return new BooleanAbility(this.id, settings);
    }
}
