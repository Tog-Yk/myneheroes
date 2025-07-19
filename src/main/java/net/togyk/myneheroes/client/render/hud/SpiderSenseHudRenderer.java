package net.togyk.myneheroes.client.render.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.util.HudActionResult;

@Environment(EnvType.CLIENT)
public class SpiderSenseHudRenderer {
    //background
    private static final Identifier SPIDER_SENSE = Identifier.of(MyneHeroes.MOD_ID,
            "hud/spider_sense");

    public static HudActionResult drawHud(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.options.getPerspective().isFirstPerson()) {
            // Enable transparency for textures
            RenderSystem.enableBlend();

            int width = drawContext.getScaledWindowWidth();
            int height = drawContext.getScaledWindowHeight();

            //background
            drawContext.drawGuiTexture(SPIDER_SENSE, width, height, 0, 0,  0,  0, 0, width, height);

            RenderSystem.disableBlend();
        }
        return HudActionResult.NO_HUD_DRAWN;
    }
}
