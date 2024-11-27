package net.togyk.myneheroes.util;

import net.togyk.myneheroes.ability.Ability;

import java.util.List;

public interface AbilityInterface {
    void cycleSelectedColum(Boolean backwards);
    void cycleSelectedDisplacement(Boolean backwards);

    Ability getFirstSelectedAbility();
    Ability getSecondSelectedAbility();
    Ability getThirdSelectedAbility();
    Ability getFourthSelectedAbility();

    List<List<Ability>> getAbilities();
}
