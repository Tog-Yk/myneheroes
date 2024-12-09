package net.togyk.myneheroes.ability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;

public class Ability {
    private final String abilityName;
    private final int maxCooldown;
    public int cooldown;

    public final Identifier icon;
    public final Identifier disabled_icon;

    public final boolean isClientSideUse = true;

    public Ability(String name,int cooldown) {
        abilityName = name;
        icon = Identifier.of(MyneHeroes.MOD_ID,"textures/ability/"+name+".png");
        disabled_icon = Identifier.of(MyneHeroes.MOD_ID,"textures/ability/"+name+"_disabled.png");
        maxCooldown = cooldown;
    }

    public void clientUse(PlayerEntity player) {
        if (player.getWorld().isClient && cooldown == 0) {
        }
    }
    public void serverUse(PlayerEntity player) {
        if (!player.getWorld().isClient && cooldown == 0) {
            cooldown = maxCooldown;
        }
    }

    public void tick() {
        if (cooldown != 0) {
            cooldown -= 1;
        }
    }

    public String getName() {
        return abilityName;
    }

    public void setCooldown(int integer) {
        cooldown = integer;
    }

    public int getMaxCooldown() {
        return maxCooldown;
    }
}
