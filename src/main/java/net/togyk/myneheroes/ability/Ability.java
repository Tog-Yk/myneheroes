package net.togyk.myneheroes.ability;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;

public class Ability {
    private final String abilityName;
    private final int maxCooldown;
    private int cooldown;

    public final Identifier icon;
    public Ability(String name,int cooldown) {
        abilityName = name;
        icon = Identifier.of(MyneHeroes.MOD_ID,"textures/ability/"+name+".png");
        maxCooldown = cooldown;
    }

    public void onUse() {
        if (cooldown == 0) {
            cooldown = maxCooldown;
            MyneHeroes.LOGGER.info("used ability");
        }
    }

    public void tick() {
        if (cooldown != 0) {
            cooldown -= 1;
        }
    }

    public String getAbilityName() {
        return abilityName;
    }

    public void setCooldown(int integer) {
        cooldown = integer;
    }

    public int getMaxCooldown() {
        return maxCooldown;
    }

    public NbtCompound getNbt(NbtCompound nbtCompound) {
        nbtCompound.putInt("cooldown", cooldown);
        return nbtCompound;
    }
}
