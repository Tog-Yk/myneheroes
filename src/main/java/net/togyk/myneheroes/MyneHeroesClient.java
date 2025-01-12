package net.togyk.myneheroes;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.togyk.myneheroes.block.screen.ModScreenHandlerTypes;
import net.togyk.myneheroes.block.screen.client.ModHandledScreens;
import net.togyk.myneheroes.client.ArmorHudOverlay;
import net.togyk.myneheroes.Item.color.ModColorProvider;
import net.togyk.myneheroes.keybind.ModKeybindingHelper;
import net.togyk.myneheroes.keybind.ModKeyBindings;
import net.togyk.myneheroes.networking.ModMessages;

public class MyneHeroesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new ArmorHudOverlay());
        ModKeyBindings.registerKeyBinds();
        ModKeybindingHelper.registerModKeybingHelper();

        ModMessages.registerClientMessages();
        ModColorProvider.registerColorProviders();

        ModHandledScreens.registerModScreenHandlerTypes();
    }
}
