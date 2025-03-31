package net.togyk.myneheroes.util;

import net.togyk.myneheroes.ability.Ability;

import java.util.List;

public interface PlayerAbilities {
    Ability getFirstAbility();
    Ability getSecondAbility();
    Ability getThirdAbility();
    Ability getFourthAbility();
    List<Ability> getAbilities();

    int getScrolledOffset();
    void setScrolledOffset(int scrolledOffset);
    boolean canScrollFurther();
    int maxScroll();
    void scrollFurther();
    void scrollBack();
}
