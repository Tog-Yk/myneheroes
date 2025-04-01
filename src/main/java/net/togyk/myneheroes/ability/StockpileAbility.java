package net.togyk.myneheroes.ability;


import net.minecraft.util.Identifier;

public interface StockpileAbility{
    int getCharge();
    void setCharge(int charge);
    int getMaxCharge();
    Identifier getChargeIcon();
}
