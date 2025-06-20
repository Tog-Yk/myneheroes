package net.togyk.myneheroes.client.screen;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.togyk.myneheroes.MyneHeroes;

public class ModHandledScreens {
    public static void registerModScreenHandlerTypes() {
        MyneHeroes.LOGGER.info("Registering Handled Screen for " + MyneHeroes.MOD_ID);

        HandledScreens.register(ModScreenHandlerTypes.ARMOR_DYEING, ArmorDyeingScreen::new);
        HandledScreens.register(ModScreenHandlerTypes.ARMOR_LIGHT_LEVELER, ArmorLightLevelerScreen::new);
        HandledScreens.register(ModScreenHandlerTypes.SELECTION_ABILITY, SelectionAbilityScreen::new);
        HandledScreens.register(ModScreenHandlerTypes.TOOLBELT_ABILITY, ToolbeltAbilityScreen::new);
        HandledScreens.register(ModScreenHandlerTypes.UPGRADE_STATION, UpgradeStationScreen::new);
    }
}
