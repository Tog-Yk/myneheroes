package net.togyk.myneheroes.client.render.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.togyk.myneheroes.Item.custom.ReactorItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.client.AbilityOverlayHelper;
import net.togyk.myneheroes.client.StockpileOverlayHelper;
import net.togyk.myneheroes.keybind.ModKeyBinds;
import net.togyk.myneheroes.util.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Environment(EnvType.CLIENT)
public class MechanicalHudRenderer {
    //background
    private static final Identifier BACKGROUND = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/background");
    private static final Identifier BORDER = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/border");

    //gauge
    private static final Identifier CENTER_LINE_GAUGE = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/center_line_gauge");
    private static final Identifier LEVEL_GAUGE_RING = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/level_gauge_ring");
    private static final Identifier LEVEL_GAUGE = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/level_gauge");

    //reactor info
    private static final Identifier BATTERY_CASING = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/battery_casing");
    private static final Identifier BATTERY = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/battery");
    private static final Identifier FUEL = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/fuel");
    private static final Identifier CONNECTED_CASE = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/connected_case");
    private static final Identifier CONNECTED = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/connected");

    // energy storage
    private static final Identifier ENERGY_STORAGE_SIDE = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/energy_storage_side");
    private static final Identifier ENERGY_STORAGE_BACKGROUND = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/energy_storage_background");

    // sight
    private static final Identifier SIGHT = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/sight");

    //ability screen
    private static final Identifier ABILITY_SCREEN_LEFT_TOP_CORNER = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/ability_screen/left_top_corner");
    private static final Identifier ABILITY_SCREEN_TOP_LINE = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/ability_screen/top_line");
    private static final Identifier ABILITY_SCREEN_RIGHT_TOP_CORNER = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/ability_screen/right_top_corner");
    private static final Identifier ABILITY_SCREEN_LEFT_LINE = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/ability_screen/left_line");
    private static final Identifier ABILITY_SCREEN_MIDDLE = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/ability_screen/middle");
    private static final Identifier ABILITY_SCREEN_RIGHT_LINE = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/ability_screen/right_line");
    private static final Identifier ABILITY_SCREEN_LEFT_BOTTOM_CORNER = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/ability_screen/left_bottom_corner");
    private static final Identifier ABILITY_SCREEN_BOTTOM_LINE = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/ability_screen/bottom_line");
    private static final Identifier ABILITY_SCREEN_RIGHT_BOTTOM_CORNER = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/ability_screen/right_bottom_corner");


    public static HudActionResult drawHud(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.options.getPerspective().isFirstPerson()) {

            // Enable transparency for textures
            RenderSystem.enableBlend();

            int width = drawContext.getScaledWindowWidth();
            int height = drawContext.getScaledWindowHeight();

            //background
            drawContext.drawGuiTexture(BACKGROUND, width, height, 0, 0,  0,  0, 0, width, height);

            drawContext.drawGuiTexture(BORDER, width, height, 0, 0,  0,  0, 0, width, height);

            //gauge
            drawContext.drawGuiTexture(CENTER_LINE_GAUGE, 30, 30, 0, 0, width/2 - 15, height / 24, 30, 30);

            drawContext.drawGuiTexture(LEVEL_GAUGE_RING, 30, 30, 0, 0, width/2 - 15, height -70, 30, 30);

            MatrixStack matrixStack = drawContext.getMatrices();
            matrixStack.push();
            matrixStack.translate((float) width /2, height -61, 0);


            matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotation((float) -(Math.toRadians(((PlayerHoverFlightControl) client.player).myneheroes$getRoll()))));

            matrixStack.push();
            matrixStack.translate(-15, -9, 0);

            drawContext.drawGuiTexture(LEVEL_GAUGE, 30, 30, 0, 0, 0, 0, 30, 30);
            matrixStack.pop();
            matrixStack.pop();

            //reactor info
            int reactionInfoX = width / 32;
            int reactionInfoY = height - height / 24;

            drawContext.drawGuiTexture(BATTERY_CASING, 18, 30, 0, 0, reactionInfoX, reactionInfoY -30, 18, 30);

            ItemStack reactorItemStack = MyneHeroes.getItemClass(client.player, ReactorItem.class);
            if (reactorItemStack != ItemStack.EMPTY && reactorItemStack.getItem() instanceof ReactorItem reactorItem) {

                float fuelPercentile = (float) reactorItem.getStoredFuelOrDefault(reactorItemStack) / reactorItem.getMaxFuel();
                int maxFuelLength = 26;
                int currentFuelLength = (int) (maxFuelLength * fuelPercentile);
                drawContext.drawGuiTexture(FUEL, 14, maxFuelLength, 0, maxFuelLength - currentFuelLength, reactionInfoX + 2, reactionInfoY -30 + 2 + maxFuelLength - currentFuelLength, 14, currentFuelLength);

                float powerPercentile = (float) reactorItem.getStoredPowerOrDefault(reactorItemStack, 0) / reactorItem.getMaxPower();
                int maxBatteryLength = 26;
                int currentBatteryLength = (int) (maxBatteryLength * powerPercentile);
                drawContext.drawGuiTexture(BATTERY, 14, maxBatteryLength, 0, maxBatteryLength - currentBatteryLength, reactionInfoX + 2, reactionInfoY -30 + 2 + maxBatteryLength - currentBatteryLength, 14, currentBatteryLength);


                drawContext.drawGuiTexture(CONNECTED, 24, 18, 0, 0, reactionInfoX + 20, reactionInfoY -18, 24, 18);
                drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, reactorItemStack.getName(),reactionInfoX + 48,reactionInfoY -8,0xC428EEFF);
                RenderSystem.enableBlend();
            }

            drawContext.drawGuiTexture(CONNECTED_CASE, 24, 18, 0, 0, reactionInfoX + 20, reactionInfoY -18, 24, 18);


            //energy storage
            drawContext.drawGuiTexture(ENERGY_STORAGE_SIDE, 6, 120, 0, 0, width / 32, height/2 - 60,6,120);
            StockpileOverlayHelper.drawStockpilesWithBackground(drawContext, client.player, ENERGY_STORAGE_BACKGROUND, width / 32 + 12, height/2 - 60, 14, 120, 2, 2);

            //sight
            drawContext.drawGuiTexture(SIGHT, 64, 64, 0, 0, width/3 * 2 - 32, height/2 - 64, 64, 64);

            // Reset state

            //abilities
            Ability firstAbility = ((PlayerAbilities) client.player).myneheroes$getFirstAbility();

            if (firstAbility != null) {
                TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
                boolean hasChatOpen = MinecraftClient.getInstance().currentScreen instanceof ChatScreen;


                List<Integer> textLengths = new ArrayList<>();
                Ability secondAbility = ((PlayerAbilities) client.player).myneheroes$getSecondAbility();
                Ability thirdAbility = ((PlayerAbilities) client.player).myneheroes$getThirdAbility();
                Ability fourthAbility = ((PlayerAbilities) client.player).myneheroes$getFourthAbility();

                Text firstAbilityText = hasChatOpen ? firstAbility.getName() : ModKeyBinds.useFirstAbility.getBoundKeyLocalizedText();
                textLengths.add(textRenderer.getWidth(firstAbilityText));
                if (secondAbility != null) {
                    Text secondAbilityText = hasChatOpen ? secondAbility.getName() : ModKeyBinds.useSecondAbility.getBoundKeyLocalizedText();
                    textLengths.add(textRenderer.getWidth(secondAbilityText));
                    if (thirdAbility != null) {
                        Text thirdAbilityText = hasChatOpen ? thirdAbility.getName() : ModKeyBinds.useThirdAbility.getBoundKeyLocalizedText();
                        textLengths.add(textRenderer.getWidth(thirdAbilityText));
                        if (fourthAbility != null) {
                            Text fourthAbilityText = hasChatOpen ? fourthAbility.getName() : ModKeyBinds.useFourthAbility.getBoundKeyLocalizedText();
                            textLengths.add(textRenderer.getWidth(fourthAbilityText));
                        }
                    }
                }

                int abilityScreenWidth = 6 + 18 + textLengths.stream().max(Comparator.naturalOrder()).get() + 6;
                int abilityScreenHeight = 4 + textLengths.size() * 18 + 2;
                boolean hasAbilityBeforeFirst = ((PlayerAbilities) client.player).myneheroes$getAbilityBeforeFirst() != null;
                if (hasAbilityBeforeFirst) {
                    abilityScreenHeight += 10;
                }
                boolean hasFifthAbility = ((PlayerAbilities) client.player).myneheroes$getFifthAbility() != null;
                if (hasFifthAbility) {
                    abilityScreenHeight += 10;
                }
                int abilityScreenX = width/3 * 2 + 28;
                int abilityScreenY = height/2 - 50 - abilityScreenHeight + (hasFifthAbility ? 10 : 0);
                drawAbilityScreen(drawContext, abilityScreenX, abilityScreenY, abilityScreenWidth, abilityScreenHeight);

                AbilityOverlayHelper.drawAbilities(client.player, drawContext, 0xC428EEFF, false, abilityScreenX + 6, abilityScreenY + 4 + (hasAbilityBeforeFirst ? 0 : -10));

            }
            RenderSystem.disableBlend();


            return HudActionResult.ABILITIES_AND_STOCKPILE_HUD_DRAWN;
        }
        return HudActionResult.NO_HUD_DRAWN;
    }

    public static void drawEnergyStorage(DrawContext drawContext, RenderTickCounter tickCounter, PlayerEntity player, int x, int y, int width, int height) {

        List<StockPile> stockpiles = StockpileOverlayHelper.getStockPiles(player);
        List<Identifier> stockpileAbilitiesIds = filterIds(stockpiles);
        for (int a = 0; a < stockpileAbilitiesIds.size(); a++) {
            Identifier id = stockpileAbilitiesIds.get(a);
            float charge = 0;
            float maxCharge = 0;

            Identifier chargeIcon = null;

            for (StockPile stockPile : AbilityUtil.getStockPilesMatchingId(stockpiles, id)) {
                charge += stockPile.getCharge();
                maxCharge += stockPile.getMaxCharge();

                chargeIcon = stockPile.getChargeIcon();
            }

            if (chargeIcon != null) {

                float chargePercentile = charge / maxCharge;

                int currentHeight = (int) ((height - 4) * chargePercentile);

                drawContext.drawTexture(chargeIcon, x + 2 + 18 * a, y + 2 + (height - 4) - currentHeight, 0, (height - 4) - currentHeight, width - 4, currentHeight, 14, 14);
                drawContext.drawGuiTexture(ENERGY_STORAGE_BACKGROUND, width, height, 0, 0, x + 18 * a, y, width, height);
            }
        }
    }

    public static void drawAbilityScreen(DrawContext drawContext, int x, int y, int width, int height) {
        drawContext.drawGuiTexture(ABILITY_SCREEN_LEFT_TOP_CORNER, 3, 3, 0, 0, x, y, 3, 3);
        drawContext.drawGuiTexture(ABILITY_SCREEN_TOP_LINE, width - 6, 3, 0, 0, x + 3, y, width - 6, 3);
        drawContext.drawGuiTexture(ABILITY_SCREEN_RIGHT_TOP_CORNER, 3, 3, 0, 0, x + width - 3, y, 3, 3);

        drawContext.drawGuiTexture(ABILITY_SCREEN_LEFT_LINE, 3, height - 6, 0, 0, x, y + 3, 3, height - 6);
        drawContext.drawGuiTexture(ABILITY_SCREEN_MIDDLE, width - 6, height - 6, 0, 0, x + 3, y + 3, width - 6, height - 6);
        drawContext.drawGuiTexture(ABILITY_SCREEN_RIGHT_LINE, 3, height - 6, 0, 0, x + width - 3, y + 3, 3, height - 6);

        drawContext.drawGuiTexture(ABILITY_SCREEN_LEFT_BOTTOM_CORNER, 3, 3, 0, 0, x, y + height - 3, 3, 3);
        drawContext.drawGuiTexture(ABILITY_SCREEN_BOTTOM_LINE, width - 6, 3, 0, 0, x + 3, y + height - 3, width - 6, 3);
        drawContext.drawGuiTexture(ABILITY_SCREEN_RIGHT_BOTTOM_CORNER, 3, 3, 0, 0, x + width - 3, y + height - 3, 3, 3);
    }

    private static List<Identifier> filterIds(List<StockPile> stockPiles) {
        List<Identifier> ids = new ArrayList<>();
        for (Identifier id : stockPiles.stream().map(StockPile::getStockPileId).toList()) {
            if (id != null && !ids.contains(id)) {
                ids.add(id);
            }
        }
        return ids;
    }
}
