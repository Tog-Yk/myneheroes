package net.togyk.myneheroes.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.HudAbility;
import net.togyk.myneheroes.util.HudActionResult;
import net.togyk.myneheroes.util.PlayerAbilities;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class AbilityOverlay implements HudRenderCallback {
    private static final Identifier ABILITIES_BACKGROUND = Identifier.of(MyneHeroes.MOD_ID, "hud/general/abilities_background");
    private static final Identifier STOCKPILE_BACKGROUND = Identifier.of(MyneHeroes.MOD_ID, "hud/general/stockpile_background");

    private static final Identifier POWER_INFO_BACKGROUND_LEFT = Identifier.of(MyneHeroes.MOD_ID, "hud/general/power_info_background_left");
    private static final Identifier POWER_INFO_BACKGROUND_MIDDLE = Identifier.of(MyneHeroes.MOD_ID, "hud/general/power_info_background_middle");
    private static final Identifier POWER_INFO_BACKGROUND_RIGHT = Identifier.of(MyneHeroes.MOD_ID, "hud/general/power_info_background_right");

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && !client.options.hudHidden) {
            boolean hasDrawnAbilities = false;
            boolean hasDrawnPowers = false;
            boolean hasDrawnStockpiles = false;

            List<Ability> abilities = ((PlayerAbilities) client.player).myneheroes$getAbilities();
            List<HudAbility> hudAbilities = getHudAbilities(abilities);
            for (HudAbility ability : hudAbilities) {
                if (ability.get()) {
                    HudActionResult result = HudTypeRenderer.drawHud(ability.getType(), drawContext, tickCounter);

                    if (result == HudActionResult.ABILITIES_AND_POWER_HUD_DRAWN
                            || result == HudActionResult.ABILITIES_AND_STOCKPILE_HUD_DRAWN
                            || result == HudActionResult.ALL_HUD_DRAWN
                            || result == HudActionResult.ABILITIES_HUD_DRAWN) {
                        hasDrawnAbilities = true;
                    }

                    if (result == HudActionResult.POWER_HUD_DRAWN
                            || result == HudActionResult.ABILITIES_AND_POWER_HUD_DRAWN
                            || result == HudActionResult.POWER_AND_STOCKPILE_HUD_DRAWN
                            || result == HudActionResult.ALL_HUD_DRAWN) {
                        hasDrawnPowers = true;
                    }

                    if (result == HudActionResult.STOCKPILE_HUD_DRAWN
                            || result == HudActionResult.ABILITIES_AND_STOCKPILE_HUD_DRAWN
                            || result == HudActionResult.POWER_AND_STOCKPILE_HUD_DRAWN
                            || result == HudActionResult.ALL_HUD_DRAWN) {
                        hasDrawnStockpiles = true;
                    }
                }
            }

            int width = drawContext.getScaledWindowWidth();
            int height = drawContext.getScaledWindowHeight();

            if (!hasDrawnAbilities) {
                //draw Ability Hud
                drawContext.drawGuiTexture(ABILITIES_BACKGROUND, 0, 0, 22, 106);
                AbilityOverlayHelper.drawAbilities(client.player, drawContext, false, 3, 3, 4);
            }
            if (!hasDrawnStockpiles) {
                StockpileOverlayHelper.drawStockpilesWithBackground(drawContext, client.player, STOCKPILE_BACKGROUND, 21, 0, 6, 56, 0, 3, 3, 50, -1);
            }
            if (!hasDrawnPowers) {
                PowerOverlayHelper.drawPowerInfo(drawContext, client.player, 0, 0);
            }
        }
    }


    public static List<HudAbility> getHudAbilities(List<Ability> abilityList) {
        List<HudAbility> abilities = new ArrayList<>();
        for (Ability ability : abilityList) {
            if (ability instanceof HudAbility hudAbility) {
                abilities.add(hudAbility);
            }
        }
        return abilities;
    }
}
