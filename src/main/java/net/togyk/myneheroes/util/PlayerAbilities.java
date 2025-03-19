package net.togyk.myneheroes.util;

import net.togyk.myneheroes.ability.Ability;

public interface PlayerAbilities {
    Ability getFirstAbility();
    Ability getSecondAbility();
    Ability getThirdAbility();
    Ability getFourthAbility();

    int getScrolledOffset();
    void setScrolledOffset(int scrolledOffset);
    boolean canScrollFurther();
    int maxScroll();
    void scrollFurther();
    void scrollBack();
}
