package net.togyk.myneheroes;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.togyk.myneheroes.client.ArmorHudOverlay;

public class MyneHeroesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new ArmorHudOverlay());
    }
}
