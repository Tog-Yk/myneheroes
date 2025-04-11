package net.togyk.myneheroes;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.togyk.myneheroes.Item.ModPredicates;
import net.togyk.myneheroes.block.screen.client.ModHandledScreens;
import net.togyk.myneheroes.client.HudOverlay;
import net.togyk.myneheroes.Item.color.ModColorProvider;
import net.togyk.myneheroes.client.render.entity.ModEntityRenderers;
import net.togyk.myneheroes.keybind.ModKeybindingHelper;
import net.togyk.myneheroes.keybind.ModKeyBindings;
import net.togyk.myneheroes.networking.ModMessages;

public class MyneHeroesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new HudOverlay());
        ModKeyBindings.registerKeyBinds();
        ModKeybindingHelper.registerModKeybingHelper();
        ModEntityRenderers.registerModEntityRenderers();

        ModMessages.registerClientMessages();

        ModColorProvider.registerColorProviders();

        ModHandledScreens.registerModScreenHandlerTypes();

        ModPredicates.registerModPredicates();
    }
}
