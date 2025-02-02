package net.togyk.myneheroes.ability;

import net.minecraft.entity.player.PlayerEntity;
import net.togyk.myneheroes.MyneHeroes;

public class BooleanAbility extends Ability{
    private boolean bool = true;
    public BooleanAbility(String name) {
        super(name, 4);
    }

    @Override
    public void clientUse(PlayerEntity player) {
        //switch the boolean
        if (player.getWorld().isClient && getCooldown() == 0) {
            bool = !bool;
        }
    }
    public boolean get() {
        return bool;
    }
}
