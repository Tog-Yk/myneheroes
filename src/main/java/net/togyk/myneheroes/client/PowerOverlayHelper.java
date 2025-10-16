package net.togyk.myneheroes.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.PlayerPowers;
import net.togyk.myneheroes.util.ScrollData;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PowerOverlayHelper {
    private static final Identifier POWER_INFO_BACKGROUND_LEFT = Identifier.of(MyneHeroes.MOD_ID, "hud/general/power_info_background_left");
    private static final Identifier POWER_INFO_BACKGROUND_MIDDLE = Identifier.of(MyneHeroes.MOD_ID, "hud/general/power_info_background_middle");
    private static final Identifier POWER_INFO_BACKGROUND_RIGHT = Identifier.of(MyneHeroes.MOD_ID, "hud/general/power_info_background_right");

    public static void drawPowerInfoWithBackground(DrawContext drawContext, PlayerEntity player, int x, int y) {
        drawPowerInfoWithBackground(drawContext, player, x, y, 2, 0);
    }
    public static void drawPowerInfoWithBackground(DrawContext drawContext, PlayerEntity player, int x, int y, int spaceHorizontal, int spaceVertical) {
        drawPowerInfoWithBackground(drawContext, player, x, y, 22, POWER_INFO_BACKGROUND_LEFT, 19, POWER_INFO_BACKGROUND_MIDDLE, 6, 0, POWER_INFO_BACKGROUND_RIGHT, 3, 3, 3, 4, spaceHorizontal, spaceVertical);
    }

    public static void drawPowerInfoWithBackground(DrawContext drawContext, PlayerEntity player, int x, int y, int height, Identifier leftTexture, int leftWidth, Identifier middleTexture, int middleStandardWidth, int middleMinimalWidth, Identifier rightTexture, int rightWidth, int offsetX, int offsetY, int bufferX, int spaceHorizontal, int spaceVertical) {
        Power power = getSelectedPower(player);
        if (power != null) {
            drawContext.drawGuiTexture(leftTexture, x, y, leftWidth, height);

            Text powerName = power.getName();
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            int totalWidth = offsetX + 16 + spaceHorizontal + textRenderer.getWidth(powerName) + bufferX;
            int middleWidth = Math.max(middleMinimalWidth, totalWidth - leftWidth - rightWidth);
            drawContext.drawGuiTexture(middleTexture, middleWidth, height, 0, 0, x + leftWidth, y, middleWidth, height);

            drawContext.drawGuiTexture(rightTexture, x + leftWidth + middleWidth, y, rightWidth, height);

            drawPowerInfo(drawContext, player, x + offsetX, y + offsetY);
        }
    }

    public static void drawPowerInfo(DrawContext drawContext, PlayerEntity player, int x, int y) {
        List<Power> powers = ((PlayerPowers) player).myneheroes$getPowers();
        Power power = PowerOverlayHelper.getSelectedPower(player);

        if (power != null) {
            drawContext.drawTexture(power.getIcon(), x, y, 0, 0, 16, 16, 16, 16);
        }

        if (powers.size() > 1) {
            PowerOverlayHelper.drawPowerName(drawContext, power, x + 18, y);
            drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.literal("+" + (powers.size() - 1)), x + 18, y + 8, 0xFFFFFF);
        } else {
            PowerOverlayHelper.drawPowerName(drawContext, power, x + 18, y + 4);
        }
    }
    public static void drawPowerName(DrawContext drawContext, Power power, int x, int y) {
        if (power != null) {
            drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, power.getName(), x, y, 0xFFFFFF);
        }
    }

    @Nullable
    public static Power getSelectedPower(PlayerEntity player) {
        List<Power> powers = ((PlayerPowers) player).myneheroes$getPowers();
        if (!powers.isEmpty()) {
            //draw Power Hud
            int scrolled = ScrollData.getScrolledPowersOffset(player);
            if (powers.size() > scrolled) {
                return powers.get(scrolled);
            }
        }
        return null;
    }
}
