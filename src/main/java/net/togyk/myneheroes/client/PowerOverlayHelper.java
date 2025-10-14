package net.togyk.myneheroes.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.PlayerPowers;
import net.togyk.myneheroes.util.ScrollData;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PowerOverlayHelper {
    public static void drawPowerInfo(DrawContext drawContext, PlayerEntity player, int x, int y) {
        List<Power> powers = ((PlayerPowers) player).myneheroes$getPowers();
        Power power = PowerOverlayHelper.getPower(player);

        drawContext.drawTexture(power.getIcon(), x, y, 0, 0, 16, 16, 16, 16);

        if (powers.size() > 1) {
            PowerOverlayHelper.drawPowerName(drawContext, power, x + 18, y);
            drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.literal("+" + (powers.size() - 1)), x + 18, y + 8, 0xFFFFFF);
        } else {
            PowerOverlayHelper.drawPowerName(drawContext, power, x + 18, y + 4);
        }
    }
    public static void drawPowerName(DrawContext drawContext, Power power, int x, int y) {
        if (power != null) {
            Text powerName = Text.translatable("power."+power.getId().toTranslationKey());
            drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, powerName, x, y, 0xFFFFFF);
        }
    }

    @Nullable
    public static Power getPower(PlayerEntity player) {
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
