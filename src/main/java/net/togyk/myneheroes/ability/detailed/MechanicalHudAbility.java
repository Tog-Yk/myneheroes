package net.togyk.myneheroes.ability.detailed;

import com.google.common.base.Predicates;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.ReactorItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.util.AbilityUtil;
import net.togyk.myneheroes.ability.HudAbility;
import net.togyk.myneheroes.ability.StockpileAbility;
import net.togyk.myneheroes.client.HudOverlay;
import net.togyk.myneheroes.keybind.ModKeyBindings;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.StockpilePower;
import net.togyk.myneheroes.util.HudActionResult;
import net.togyk.myneheroes.util.PlayerAbilities;
import net.togyk.myneheroes.util.PowerData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MechanicalHudAbility extends HudAbility {
    //background
    private static final Identifier BACKGROUND = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/background");
    private static final Identifier BORDER = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/border");

    //gauge
    private static final Identifier CENTER_LINE_GAUGE = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/center_line_gauge");
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

    public MechanicalHudAbility(Identifier id, Ability.Settings settings) {
        super(id, settings);
    }

    @Override
    public HudActionResult drawHud(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (this.get() && client.player != null && client.options.getPerspective().isFirstPerson()) {

            // Enable transparency for textures
            RenderSystem.enableBlend();

            int width = drawContext.getScaledWindowWidth();
            int height = drawContext.getScaledWindowHeight();

            //background
            drawContext.drawGuiTexture(BACKGROUND, width, height, 0, 0,  0,  0, 0, width, height);

            drawContext.drawGuiTexture(BORDER, width, height, 0, 0,  0,  0, 0, width, height);

            //gauge
            drawContext.drawGuiTexture(CENTER_LINE_GAUGE, 30, 30, 0, 0, width/2 - 15, height / 24, 30, 30);
            drawContext.drawGuiTexture(LEVEL_GAUGE, 30, 30, 0, 0, width/2 - 15, height -70, 30, 30);

            //reactor info
            int reactionInfoX = width / 32;
            int reactionInfoY = height - height / 24;

            drawContext.drawGuiTexture(BATTERY_CASING, 18, 30, 0, 0, reactionInfoX, reactionInfoY -30, 18, 30);

            ItemStack reactorItemStack = MyneHeroes.getReactorItemClass(client.player);
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
            drawEnergyStorage(drawContext, tickCounter, client.player, width / 32 + 12, height/2 - 60, 14, 120);

            //sight
            drawContext.drawGuiTexture(SIGHT, 64, 64, 0, 0, width/3 * 2 - 32, height/2 - 64, 64, 64);

            // Reset state

            //abilities
            Ability firstAbility = ((PlayerAbilities) client.player).myneheroes$getFirstAbility();

            if (firstAbility != null) {
                TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
                boolean hasChatOpen = MinecraftClient.getInstance().currentScreen instanceof ChatScreen;


                List<Integer> textLengths = new ArrayList<>();
                Text firstAbilityText;
                Ability secondAbility = ((PlayerAbilities) client.player).myneheroes$getSecondAbility();
                Text secondAbilityText = null;
                Ability thirdAbility = ((PlayerAbilities) client.player).myneheroes$getThirdAbility();
                Text thirdAbilityText = null;
                Ability fourthAbility = ((PlayerAbilities) client.player).myneheroes$getFourthAbility();
                Text fourthAbilityText = null;

                firstAbilityText = hasChatOpen ? Text.translatable("ability."+firstAbility.getId().toTranslationKey()) : ModKeyBindings.useFirstAbility.getBoundKeyLocalizedText();
                textLengths.add(textRenderer.getWidth(firstAbilityText));
                if (secondAbility != null) {
                    secondAbilityText = hasChatOpen ? Text.translatable("ability."+secondAbility.getId().toTranslationKey()) : ModKeyBindings.useSecondAbility.getBoundKeyLocalizedText();
                    textLengths.add(textRenderer.getWidth(secondAbilityText));
                    if (thirdAbility != null) {
                        thirdAbilityText = hasChatOpen ? Text.translatable("ability."+thirdAbility.getId().toTranslationKey()) : ModKeyBindings.useThirdAbility.getBoundKeyLocalizedText();
                        textLengths.add(textRenderer.getWidth(thirdAbilityText));
                        if (fourthAbility != null) {
                            fourthAbilityText = hasChatOpen ? Text.translatable("ability."+fourthAbility.getId().toTranslationKey()) : ModKeyBindings.useFourthAbility.getBoundKeyLocalizedText();
                            textLengths.add(textRenderer.getWidth(fourthAbilityText));
                        }
                    }
                }

                int abilityScreenWidth = 6 + 18 + textLengths.stream().max(Comparator.naturalOrder()).get() + 6;
                int abilityScreenHeight = 4 + textLengths.size() * 18 + 2;
                int abilityScreenX = width/3 * 2 + 28;
                int abilityScreenY = height/2 - 50 - abilityScreenHeight;
                drawAbilityScreen(drawContext, abilityScreenX, abilityScreenY, abilityScreenWidth, abilityScreenHeight);

                int y = abilityScreenY + 4;

                RenderSystem.setShaderColor(1, 1, 1, (float) 0xC4 / 255);

                HudOverlay.drawAbility(drawContext, firstAbility, ModKeyBindings.useFirstAbility.isPressed(), abilityScreenX + 6, y);
                drawContext.drawTextWithShadow(textRenderer, firstAbilityText, abilityScreenX + 6 + 18, y + 4, 0xC428EEFF);
                RenderSystem.enableBlend();
                y += 18;

                HudOverlay.drawAbility(drawContext, secondAbility, ModKeyBindings.useSecondAbility.isPressed(), abilityScreenX + 6, y);
                if (secondAbility != null) {
                    drawContext.drawTextWithShadow(textRenderer, secondAbilityText, abilityScreenX + 6 + 18, y + 4, 0xC428EEFF);
                    RenderSystem.enableBlend();
                }
                y += 18;

                HudOverlay.drawAbility(drawContext, thirdAbility, ModKeyBindings.useThirdAbility.isPressed(), abilityScreenX + 6, y);
                if (thirdAbility != null) {
                    drawContext.drawTextWithShadow(textRenderer, thirdAbilityText, abilityScreenX + 6 + 18, y + 4, 0xC428EEFF);
                    RenderSystem.enableBlend();
                }
                y += 18;

                HudOverlay.drawAbility(drawContext, fourthAbility, ModKeyBindings.useFourthAbility.isPressed(), abilityScreenX + 6, y);
                if (fourthAbility != null) {
                    drawContext.drawTextWithShadow(textRenderer, fourthAbilityText, abilityScreenX + 6 + 18, y + 4, 0xC428EEFF);
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
        List<Power> stockpilePowers = powers.stream().filter(Predicates.instanceOf(StockpilePower.class)).toList();
        int i;
        for (i = 0; i < stockpilePowers.size(); i++) {
            if (stockpilePowers.get(i) instanceof StockpilePower power) {
                int charge = power.getCharge();
                int maxCharge = power.getMaxCharge();

                float chargePercentile = (float) charge / maxCharge;

                int currentHeight = (int) ((height - 4) * chargePercentile);

                drawContext.drawTexture(power.getChargeIcon(), x + 2 + 18 * i, y + 2 + (height - 4) - currentHeight, 0, (height - 4) - currentHeight, width - 4, currentHeight, 14, 14);
                drawContext.drawGuiTexture(ENERGY_STORAGE_BACKGROUND, width, height, 0, 0, x + 18 * i, y, width, height);
            }
        }

        List<Ability> abilities = ((PlayerAbilities) player).myneheroes$getAbilities();
        List<Ability> stockpileAbilities = abilities.stream().filter(Predicates.instanceOf(StockpileAbility.class)).toList();
        List<Identifier> stockpileAbilitiesIds = filterIds(stockpileAbilities);
        for (int a = i; a < stockpileAbilitiesIds.size() + i; a++) {
            Identifier id = stockpileAbilitiesIds.get(a - i);
            float charge = 0;
            float maxCharge = 0;

            Identifier chargeIcon = null;

            for (Ability ability : AbilityUtil.getAbilitiesMatchingId(stockpileAbilities, id)) {
                if (ability instanceof StockpileAbility stockpileAbility) {
                    charge += stockpileAbility.getCharge();
                    maxCharge += stockpileAbility.getMaxCharge();

                    chargeIcon = stockpileAbility.getChargeIcon();
                }
            }

            if (chargeIcon != null) {

                float chargePercentile = charge / maxCharge;

                int currentHeight = (int) ((height - 4) * chargePercentile);

                drawContext.drawTexture(chargeIcon, x + 2 + 18 * i, y + 2 + (height - 4) - currentHeight, 0, (height - 4) - currentHeight, width - 4, currentHeight, 14, 14);
                drawContext.drawGuiTexture(ENERGY_STORAGE_BACKGROUND, width, height, 0, 0, x + 18 * i, y, width, height);
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

    private static List<Identifier> filterIds(List<Ability> abilities) {
        List<Identifier> ids = new ArrayList<>();
        for (Identifier id : abilities.stream().map(Ability::getId).toList()) {
            if (id != null && !ids.contains(id)) {
                ids.add(id);
            }
        }
        return ids;
    }

    @Override
    public MechanicalHudAbility copy() {
        return new MechanicalHudAbility(this.id, settings);
    }
}
