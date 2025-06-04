package net.togyk.myneheroes.advancement.criterion;

import net.minecraft.advancement.criterion.Criteria;
import net.togyk.myneheroes.MyneHeroes;

public class ModCriteria {
    public static void register() {
        MyneHeroes.LOGGER.info("registering criteria for " + MyneHeroes.MOD_ID);
    }

    public static final PowersChangedCriterion POWERS_CHANGED_CRITERION = Criteria.register(PowersChangedCriterion.ID.toString(), new PowersChangedCriterion());
}
