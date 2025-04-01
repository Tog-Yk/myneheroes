package net.togyk.myneheroes.ability;


import net.minecraft.util.Identifier;

public interface StockpileAbility{
    float getCharge();
    void setCharge(float charge);
    float getMaxCharge();
    Identifier getChargeIcon();
}
