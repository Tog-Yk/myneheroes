package net.togyk.myneheroes.util;

import net.togyk.myneheroes.power.Power;

import java.util.List;

public interface PlayerPowers {
    List<Power> myneheroes$getPowers();
    void myneheroes$setPowers(List<Power> powers);
    void myneheroes$removePower(Power power);
    void myneheroes$addPower(Power power);
    double myneheroes$getDamageMultiplier();

    int myneheroes$getScrolledPowerOffset();
    void myneheroes$setScrolledPowerOffset(int scrolledOffset);
    void myneheroes$scrollPowerFurther();
    void myneheroes$scrollPowerBack();
    int myneheroes$maxPowerScroll();
}
