package net.togyk.myneheroes.util;

import net.togyk.myneheroes.power.Power;

import java.util.List;

public interface PlayerPowers {
    List<Power> getPowers();
    void setPowers(List<Power> powers);
    void removePower(Power power);
    void addPower(Power power);
    double getDamageMultiplier();
    double getResistance();

    int getScrolledPowerOffset();
    void setScrolledPowerOffset(int scrolledOffset);
    void scrollPowerFurther();
    void scrollPowerBack();
    int maxPowerScroll();
}
