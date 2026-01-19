package net.togyk.myneheroes.util;

public interface PlayerHoverFlightControl {
    boolean myneheroes$canHoverFly();
    boolean myneheroes$isHoverFlying();
    void myneheroes$setHoverFlying(boolean flying);
    float myneheroes$getHoverProgress();
    float myneheroes$getHoverProgressRight();
    float myneheroes$getHoverProgressLeft();
    float myneheroes$getHoverProgressBack();
}
