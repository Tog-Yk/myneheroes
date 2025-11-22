package net.togyk.myneheroes.client.render.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.util.HudActionResult;

@Environment(EnvType.CLIENT)
public class TutorialHudRenderer {
    //background
    private static final Identifier TUTOR= Identifier.of(MyneHeroes.MOD_ID,
            "hud/tutor");

    public static HudActionResult drawHud(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.options.getPerspective().isFirstPerson()) {
            // Enable transparency for textures
            RenderSystem.enableBlend();

            int width = drawContext.getScaledWindowWidth();
            int height = drawContext.getScaledWindowHeight();

            //background
            drawContext.drawGuiTexture(TUTOR, width/2, height/2, 0, 0,  width/4,  height/4, 0, width/2, height/2);
            //text
            drawContext.drawText(client.textRenderer, Text.translatable(Util.createTranslationKey("gui", Identifier.of("myneheroes:shibashis_shenanigans.fulltext"))), width/2 + 10, height/2 +10, 0xFFFFFF, true);
            RenderSystem.disableBlend();
        }
        // if the hud implements drawing of the active skills, drawing of the active skills should be suppressed. Otherwise: return NO_HUD_DRAWN
        return HudActionResult.NO_HUD_DRAWN;
    }
}
