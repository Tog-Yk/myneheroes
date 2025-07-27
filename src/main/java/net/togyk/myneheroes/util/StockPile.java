package net.togyk.myneheroes.util;

import net.minecraft.util.Identifier;

public interface StockPile {
    Identifier getStockPileId();

    float getCharge();
    void setCharge(float charge);

    float getMaxCharge();
    Identifier getChargeIcon();
}
