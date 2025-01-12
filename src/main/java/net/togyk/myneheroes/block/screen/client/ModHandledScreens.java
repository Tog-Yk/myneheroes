package net.togyk.myneheroes.block.screen.client;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.block.screen.ModScreenHandlerTypes;

public class ModHandledScreens {
    public static void registerModScreenHandlerTypes() {
        MyneHeroes.LOGGER.info("Registering Handled Screen for " + MyneHeroes.MOD_ID);

        HandledScreens.register(ModScreenHandlerTypes.ARMOR_DYEING, ArmorDyeingScreen::new  );
    }
}
