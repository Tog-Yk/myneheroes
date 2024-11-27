package net.togyk.myneheroes.ability;

import net.togyk.myneheroes.MyneHeroes;

public class TickedAbility extends Ability{
    private boolean active = false;
    public TickedAbility(String name, int cooldown) {
        super(name, cooldown);
    }

    @Override
    public void tick() {
        super.tick();
        if (active) {
            logic();
        }
    }

    private void logic() {
        MyneHeroes.LOGGER.info("activated toggled ability");
    }

    @Override
    public void onUse() {
        active = !active;
    }
}
