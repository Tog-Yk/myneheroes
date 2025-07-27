package net.togyk.myneheroes.client.render.hud;

import com.google.common.base.Predicates;
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
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.Item.custom.ReactorItem;
import net.togyk.myneheroes.Item.custom.UpgradableItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.StockpileAbility;
import net.togyk.myneheroes.client.HudOverlay;
import net.togyk.myneheroes.keybind.ModKeyBinds;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.StockpilePower;
import net.togyk.myneheroes.upgrade.Upgrade;
import net.togyk.myneheroes.util.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

@Environment(EnvType.CLIENT)
public class SpeedsterHudRenderer {
    //background
    private static final Identifier BACKGROUND = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/background");
    private static final Identifier BORDER_LEFT = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/border_left");
    private static final Identifier BORDER_RIGHT = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/border_right");

    private static final Identifier BORDER_TOP_LEFT = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/border_top/left");
    private static final Identifier BORDER_TOP_CONNECTION_LEFT = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/border_top/connection_left");
    private static final Identifier BORDER_TOP_MIDDLE = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/border_top/middle");
    private static final Identifier BORDER_TOP_CONNECTION_RIGHT = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/border_top/connection_right");
    private static final Identifier BORDER_TOP_RIGHT = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/border_top/right");

    //gauge
    private static final Identifier CENTER_LINE_GAUGE = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/compas_line_gauge");
    private static final Identifier COMPAS_GAUGE_ARROW = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/compas_gauge_arrow");
    private static final Identifier COMPAS_GAUGE = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/compas_gauge");

    //reactor info
    private static final Identifier BATTERY = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/battery");
    private static final Identifier FUEL = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/fuel");
    private static final Identifier CONNECTED_CASE = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/connected_case");
    private static final Identifier CONNECTED = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/connected");

    // sight
    private static final Identifier SIGHT = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/sight");

    //ability screen
    private static final Identifier ABILITY_SCREEN_FIRST_TIP = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/ability_screen/first_tip");
    private static final Identifier ABILITY_SCREEN_FIRST_EXTENSION = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/ability_screen/first_extension");
    private static final Identifier ABILITY_SCREEN_FIRST_END = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/ability_screen/first_end");

    private static final Identifier ABILITY_SCREEN_SECOND_TIP = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/ability_screen/second_tip");
    private static final Identifier ABILITY_SCREEN_SECOND_EXTENSION = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/ability_screen/second_extension");
    private static final Identifier ABILITY_SCREEN_SECOND_END = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/ability_screen/second_end");

    private static final Identifier ABILITY_SCREEN_THIRD_TIP = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/ability_screen/third_tip");
    private static final Identifier ABILITY_SCREEN_THIRD_EXTENSION = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/ability_screen/third_extension");
    private static final Identifier ABILITY_SCREEN_THIRD_END = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/ability_screen/third_end");

    private static final Identifier ABILITY_SCREEN_FOURTH_TIP = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/ability_screen/fourth_tip");
    private static final Identifier ABILITY_SCREEN_FOURTH_EXTENSION = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/ability_screen/fourth_extension");
    private static final Identifier ABILITY_SCREEN_FOURTH_END = Identifier.of(MyneHeroes.MOD_ID,
            "hud/speedster/ability_screen/fourth_end");

    public static HudActionResult drawHud(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.options.getPerspective().isFirstPerson()) {
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

            // Enable transparency for textures
            RenderSystem.enableBlend();

            int width = drawContext.getScaledWindowWidth();
            int height = drawContext.getScaledWindowHeight();

            //background
            drawContext.drawGuiTexture(BACKGROUND, width, height, 0, 0,  0,  0, 0, width, height);

            //border
            //top
            drawContext.drawGuiTexture(BORDER_TOP_LEFT, width / 3 - 12, 8, 0, 0, 12, 6, width / 3 - 12, 8);
            drawContext.drawGuiTexture(BORDER_TOP_CONNECTION_LEFT, 8, 8, 0, 0, width / 3, 6, 8, 8);
            drawContext.drawGuiTexture(BORDER_TOP_MIDDLE, (width / 3) - 16, 8, 0, 0, width / 3 + 8, 6, width / 3 - 16, 8);
            drawContext.drawGuiTexture(BORDER_TOP_CONNECTION_RIGHT, 8, 8, 0, 0, width * 2 / 3 - 8, 6, 8, 8);
            drawContext.drawGuiTexture(BORDER_TOP_RIGHT,  width - 12 - width * 2 / 3, 8, 0, 0, width * 2 / 3, 6, width - 12 - width * 2 / 3, 8);

            drawContext.drawGuiTexture(BORDER_LEFT, 12, 14, 0, 0,  width / 5,  height - 20, 0, 12, 14);
            drawContext.drawGuiTexture(BORDER_RIGHT, 12, 14, 0, 0,  width * 4 / 5 - 8,  height - 20, 0, 12, 14);

            //speed
            Vec3d velocity = client.player.getVelocity();
            double velocityY = client.player.isOnGround() ? 0 : velocity.y;
            double speedPerTick = Math.sqrt(velocity.x * velocity.x + velocityY * velocityY + velocity.z * velocity.z);
            double speed = speedPerTick * 20 * 1.83;

            drawContext.drawTextWithShadow(textRenderer, Text.literal(String.format("%.2f", speed) + "b/s"), width * 2 / 3, 14 - textRenderer.fontHeight, 0xC4FFEB28);
            RenderSystem.enableBlend();

            //compas gauge
            drawContext.drawGuiTexture(COMPAS_GAUGE, 9, 9, 0, 0, width / 5 - 4, height - 20 + 4, 9, 9);

            //rotating
            MatrixStack matrixStack = drawContext.getMatrices();
            matrixStack.push();
            matrixStack.translate((float) width / 5 - 4 + 4.5, height - 20 + 4 + 4.5, 0);


            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotation((float) Math.toRadians(client.player.getYaw(0) + 180)));

            matrixStack.push();
            matrixStack.translate(4.5, 4.5, 0);

            drawContext.drawGuiTexture(COMPAS_GAUGE_ARROW, 9, 9, 0, 0, -9, -9, 9, 9);
            matrixStack.pop();
            matrixStack.pop();

            //reactor info
            int reactionInfoX = 12;
            int reactionInfoY = height / 2 - ((20 + textRenderer.fontHeight) / 2);

            ItemStack reactorItemStack = MyneHeroes.getReactorItemClass(client.player);
            if (reactorItemStack != ItemStack.EMPTY && reactorItemStack.getItem() instanceof ReactorItem reactorItem) {

                float fuelPercentile = (float) reactorItem.getStoredFuelOrDefault(reactorItemStack) / reactorItem.getMaxFuel();
                int maxFuelLength = 24;
                int currentFuelLength = (int) (maxFuelLength * fuelPercentile);
                drawContext.drawGuiTexture(FUEL, maxFuelLength, 6, 0, 0, reactionInfoX + 26, reactionInfoY + 2, currentFuelLength, 6);

                float powerPercentile = (float) reactorItem.getStoredPowerOrDefault(reactorItemStack, 0) / reactorItem.getMaxPower();
                int maxBatteryLength = 24;
                int currentBatteryLength = (int) (maxBatteryLength * powerPercentile);
                drawContext.drawGuiTexture(BATTERY, maxBatteryLength, 6, 0, 0, reactionInfoX + 26, reactionInfoY + 10, currentBatteryLength, 6);


                drawContext.drawGuiTexture(CONNECTED, 24, 18, 0, 0, reactionInfoX, reactionInfoY, 24, 18);
                drawContext.drawTextWithShadow(textRenderer, reactorItemStack.getName(),reactionInfoX,reactionInfoY + 20,0xC4FFEB28);
                RenderSystem.enableBlend();
            }

            drawContext.drawGuiTexture(CONNECTED_CASE, 24, 18, 0, 0, reactionInfoX, reactionInfoY, 24, 18);


            //energy storage
            drawEnergyStorage(drawContext, tickCounter, client.player, 12, 16, 120, 10);

            //sight
            drawContext.drawGuiTexture(SIGHT, 64, 64, 0, 0, width/ 2 - 32, height/2 - 32, 64, 64);

            //abilities
            Ability firstAbility = ((PlayerAbilities) client.player).myneheroes$getFirstAbility();

            if (firstAbility != null) {
                boolean hasChatOpen = MinecraftClient.getInstance().currentScreen instanceof ChatScreen;


                List<Integer> textLengths = new ArrayList<>();
                Text firstAbilityText;
                Ability secondAbility = ((PlayerAbilities) client.player).myneheroes$getSecondAbility();
                Text secondAbilityText = null;
                Ability thirdAbility = ((PlayerAbilities) client.player).myneheroes$getThirdAbility();
                Text thirdAbilityText = null;
                Ability fourthAbility = ((PlayerAbilities) client.player).myneheroes$getFourthAbility();
                Text fourthAbilityText = null;

                firstAbilityText = hasChatOpen ? Text.translatable("ability."+firstAbility.getId().toTranslationKey()) : ModKeyBinds.useFirstAbility.getBoundKeyLocalizedText();
                textLengths.add(textRenderer.getWidth(firstAbilityText));
                if (secondAbility != null) {
                    secondAbilityText = hasChatOpen ? Text.translatable("ability."+secondAbility.getId().toTranslationKey()) : ModKeyBinds.useSecondAbility.getBoundKeyLocalizedText();
                    textLengths.add(textRenderer.getWidth(secondAbilityText));
                    if (thirdAbility != null) {
                        thirdAbilityText = hasChatOpen ? Text.translatable("ability."+thirdAbility.getId().toTranslationKey()) : ModKeyBinds.useThirdAbility.getBoundKeyLocalizedText();
                        textLengths.add(textRenderer.getWidth(thirdAbilityText));
                        if (fourthAbility != null) {
                            fourthAbilityText = hasChatOpen ? Text.translatable("ability."+fourthAbility.getId().toTranslationKey()) : ModKeyBinds.useFourthAbility.getBoundKeyLocalizedText();
                            textLengths.add(textRenderer.getWidth(fourthAbilityText));
                        }
                    }
                }

                int extension_length; //text length: outer: 20 inner: 50
                int longestText = textLengths.stream().max(Comparator.naturalOrder()).get();
                if ((secondAbilityText != null && textRenderer.getWidth(secondAbilityText) == longestText) || (thirdAbilityText != null && textRenderer.getWidth(thirdAbilityText) == longestText)) {
                    if ((firstAbilityText != null && textRenderer.getWidth(firstAbilityText) < longestText - 28) && (fourthAbilityText != null && textRenderer.getWidth(fourthAbilityText) < longestText - 28)) {
                        extension_length = Math.max(0, longestText - 28 - 28);
                    } else {
                        extension_length = Math.max(0, longestText - 28);
                    }
                } else {
                    extension_length = Math.max(0, longestText - 28);
                }


                int abilityScreenWidth = 6 + 18 + textLengths.stream().max(Comparator.naturalOrder()).get() + 6;
                int abilityScreenHeight = 4 + textLengths.size() * 22 + 2;
                int abilityScreenX = width - abilityScreenWidth;
                int abilityScreenY = (height - abilityScreenHeight) / 2;

                int AbilityY = abilityScreenY + 4;


                drawContext.drawGuiTexture(ABILITY_SCREEN_FIRST_TIP, width - 40 - 40 - extension_length, AbilityY, 40, 20);
                drawContext.drawGuiTexture(ABILITY_SCREEN_FIRST_EXTENSION, extension_length, 20, 0, 0, width - 40 - extension_length, AbilityY, extension_length, 20);
                drawContext.drawGuiTexture(ABILITY_SCREEN_FIRST_END, width - 40, AbilityY, 40, 20);

                RenderSystem.setShaderColor(1, 1, 1, (float) 0xC4 / 255);

                HudOverlay.drawAbility(drawContext, firstAbility, ModKeyBinds.useFirstAbility.isPressed(), abilityScreenX + abilityScreenWidth - 2 - 16, AbilityY + 2);
                drawContext.drawTextWithShadow(textRenderer, firstAbilityText, abilityScreenX + abilityScreenWidth - 16 - 18 - textRenderer.getWidth(firstAbilityText), AbilityY + 6, 0xC4FFEB28);
                RenderSystem.enableBlend();

                AbilityY += 22;

                if (secondAbility != null) {
                    drawContext.drawGuiTexture(ABILITY_SCREEN_SECOND_TIP, width - 40 - 40 - (extension_length + 30), AbilityY, 40, 20);
                    drawContext.drawGuiTexture(ABILITY_SCREEN_SECOND_EXTENSION, (extension_length + 30), 20, 0, 0, width - 40 - (extension_length + 30), AbilityY, (extension_length + 30), 20);
                    drawContext.drawGuiTexture(ABILITY_SCREEN_SECOND_END, width - 40, AbilityY, 40, 20);

                    HudOverlay.drawAbility(drawContext, secondAbility, ModKeyBinds.useSecondAbility.isPressed(), abilityScreenX + abilityScreenWidth - 2 - 16, AbilityY + 2);

                    drawContext.drawTextWithShadow(textRenderer, secondAbilityText, abilityScreenX + abilityScreenWidth - 16 - 18 - textRenderer.getWidth(secondAbilityText), AbilityY + 6, 0xC4FFEB28);
                    RenderSystem.enableBlend();
                }
                AbilityY += 22;

                if (thirdAbility != null) {
                    drawContext.drawGuiTexture(ABILITY_SCREEN_THIRD_TIP, width - 40 - 40 - (extension_length + 30), AbilityY, 40, 20);
                    drawContext.drawGuiTexture(ABILITY_SCREEN_THIRD_EXTENSION, (extension_length + 30), 20, 0, 0, width - 40 - (extension_length + 30), AbilityY, (extension_length + 30), 20);
                    drawContext.drawGuiTexture(ABILITY_SCREEN_THIRD_END, width - 40, AbilityY, 40, 20);

                    HudOverlay.drawAbility(drawContext, thirdAbility, ModKeyBinds.useThirdAbility.isPressed(), abilityScreenX + abilityScreenWidth - 2 - 16, AbilityY + 2);

                    drawContext.drawTextWithShadow(textRenderer, thirdAbilityText, abilityScreenX + abilityScreenWidth - 16 - 18 - textRenderer.getWidth(thirdAbilityText), AbilityY + 6, 0xC4FFEB28);
                    RenderSystem.enableBlend();
                }
                AbilityY += 22;

                if (fourthAbility != null) {
                    drawContext.drawGuiTexture(ABILITY_SCREEN_FOURTH_TIP, width - 40 - 40 - extension_length, AbilityY, 40, 20);
                    drawContext.drawGuiTexture(ABILITY_SCREEN_FOURTH_EXTENSION, extension_length, 20, 0, 0, width - 40 - extension_length, AbilityY, extension_length, 20);
                    drawContext.drawGuiTexture(ABILITY_SCREEN_FOURTH_END, width - 40, AbilityY, 40, 20);

                    HudOverlay.drawAbility(drawContext, fourthAbility, ModKeyBinds.useFourthAbility.isPressed(), abilityScreenX + abilityScreenWidth - 2 - 16, AbilityY + 2);

                    drawContext.drawTextWithShadow(textRenderer, fourthAbilityText, abilityScreenX + abilityScreenWidth - 16 - 18 - textRenderer.getWidth(fourthAbilityText), AbilityY + 6, 0xC4FFEB28);
                    RenderSystem.enableBlend();
                }

                RenderSystem.setShaderColor(1, 1, 1, 1);
            }
            RenderSystem.disableBlend();


            return HudActionResult.ABILITIES_HUD_DRAWN;
        }
        return HudActionResult.NO_HUD_DRAWN;
    }

    public static void drawEnergyStorage(DrawContext drawContext, RenderTickCounter tickCounter, PlayerEntity player, int x, int y, int width, int height) {

        List<Power> powers = PowerData.getPowers(player);
        List<StockPile> stockpiles = new ArrayList<>(powers.stream().filter(Predicates.instanceOf(StockpilePower.class)).map(power -> (StockPile) power).toList());

        List<Ability> abilities = ((PlayerAbilities) player).myneheroes$getAbilities();
        abilities.stream().filter(Predicates.instanceOf(StockPile.class)).forEach(ability -> stockpiles.add((StockPile) ability));

        Iterable<ItemStack> armorIterator = player.getArmorItems();
        List<ItemStack> armor = StreamSupport.stream(armorIterator.spliterator(), false)
                .toList();

        for (ItemStack stack : armor) {
            if (stack.getItem() instanceof UpgradableItem upgradableItem) {
                for (Upgrade upgrade : upgradableItem.getUpgrades(stack)) {
                    if (upgrade instanceof StockPile stockPile) {
                        stockpiles.add(stockPile);
                    }
                }
            }
        }

        List<ItemStack> inventory = player.getInventory().main;

        for (ItemStack stack : inventory) {
            if (stack.getItem() instanceof UpgradableItem upgradableItem) {
                for (Upgrade upgrade : upgradableItem.getUpgrades(stack)) {
                    if (upgrade instanceof StockPile stockPile) {
                        stockpiles.add(stockPile);
                    }
                }
            }
        }

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

                int currentWidth = (int) (width * chargePercentile);

                drawContext.drawTexture(chargeIcon, x, y + 12 * a, 0, 0, currentWidth, height, 14, 14);
            }
        }
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
