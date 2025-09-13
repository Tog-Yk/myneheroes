package net.togyk.myneheroes.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.togyk.myneheroes.client.render.hud.MechanicalHudRenderer;
import net.togyk.myneheroes.client.render.hud.SpeedsterHudRenderer;
import net.togyk.myneheroes.client.render.hud.SpiderSenseHudRenderer;
import net.togyk.myneheroes.client.render.hud.TutorialHudRenderer;
import net.togyk.myneheroes.util.HudActionResult;

@Environment(EnvType.CLIENT)
public class HudTypeRenderer {
    public static HudActionResult drawHud(HudType type, DrawContext drawContext, RenderTickCounter tickCounter) {
        switch (type) {
            case MECHANICAL -> {
                return MechanicalHudRenderer.drawHud(drawContext, tickCounter);
            }
            case SPEEDSTER -> {
                return SpeedsterHudRenderer.drawHud(drawContext, tickCounter);
            }
            case SPIDER_SENSE -> {
                return SpiderSenseHudRenderer.drawHud(drawContext, tickCounter);
            }
            case TUTOR -> {
                return TutorialHudRenderer.drawHud(drawContext, tickCounter);
            }
            case null, default -> {
                return HudActionResult.NO_HUD_DRAWN;
            }
        }
    }
}
