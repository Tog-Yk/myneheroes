package net.togyk.myneheroes.util;

import net.togyk.myneheroes.ability.Ability;

import java.util.List;

public interface PlayerAbilities {
    Ability myneheroes$getFirstAbility();
    Ability myneheroes$getSecondAbility();
    Ability myneheroes$getThirdAbility();
    Ability myneheroes$getFourthAbility();
    Ability myneheroes$getAbilityBeforeFirst();
    Ability myneheroes$getFifthAbility();
    List<Ability> myneheroes$getAbilities();
    List<Ability> myneheroes$getFilteredAbilities();

    int myneheroes$getScrolledAbilityOffset();
    void myneheroes$setScrolledAbilityOffset(int scrolledAbilityOffset);
    boolean myneheroes$canScrollAbilityFurther();
    int myneheroes$maxAbilityScroll();
    void myneheroes$scrollAbilityFurther();
    void myneheroes$scrollAbilityBack();


    void myneheroes$setAbilities(List<Ability> abilities);

    void myneheroes$setIsHoldingJump(boolean jumping);
    boolean myneheroes$isHoldingJump();
}
