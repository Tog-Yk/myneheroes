package net.togyk.myneheroes.util;

import net.togyk.myneheroes.ability.Ability;

import java.util.List;

public interface PlayerAbilities {
    Ability getFirstAbility();
    Ability getSecondAbility();
    Ability getThirdAbility();
    Ability getFourthAbility();
    Ability getAbilityBeforeFirst();
    Ability getFifthAbility();
    List<Ability> getAbilities();
    List<Ability> getFilteredAbilities();

    int getScrolledAbilityOffset();
    void setScrolledAbilityOffset(int scrolledAbilityOffset);
    boolean canScrollAbilityFurther();
    int maxAbilityScroll();
    void scrollAbilityFurther();
    void scrollAbilityBack();
}
