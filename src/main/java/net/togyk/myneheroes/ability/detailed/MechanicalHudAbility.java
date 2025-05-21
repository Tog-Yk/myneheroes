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
import net.togyk.myneheroes.ability.AbilityUtil;
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
    private static final Identifier ABILITY_SCREEN = Identifier.of(MyneHeroes.MOD_ID,
            "hud/mechanical/ability_screen");

    public MechanicalHudAbility(Identifier id, String name, Ability.Settings settings) {
        super(id, name, settings);
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

                float fuelPercentile = (float) reactorItem.getStoredFuelOrDefault(reactorItemStack, 0) / reactorItem.getMaxFuel();
                int maxFuelLength = 26;
                int currentFuelLength = (int) (maxFuelLength * fuelPercentile);
                drawContext.drawGuiTexture(FUEL, 14, maxFuelLength, 0, maxFuelLength - currentFuelLength, reactionInfoX + 2, reactionInfoY -30 + 2 + maxFuelLength - currentFuelLength, 14, currentFuelLength);

                float powerPercentile = (float) reactorItem.getStoredPowerOrDefault(reactorItemStack, 0) / reactorItem.getMaxPower();
                int maxBatteryLength = 26;
                int currentBatteryLength = (int) (maxBatteryLength * powerPercentile);
                drawContext.drawGuiTexture(BATTERY, 14, maxBatteryLength, 0, maxBatteryLength - currentBatteryLength, reactionInfoX + 2, reactionInfoY -30 + 2 + maxBatteryLength - currentBatteryLength, 14, currentBatteryLength);


                drawContext.drawGuiTexture(CONNECTED, 24, 18, 0, 0, reactionInfoX + 20, reactionInfoY -18, 24, 18);
                drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, reactorItemStack.getName(),reactionInfoX + 48,reactionInfoY -8,0x88FFFF);
                RenderSystem.enableBlend();
            }

            drawContext.drawGuiTexture(CONNECTED_CASE, 24, 18, 0, 0, reactionInfoX + 20, reactionInfoY -18, 24, 18);


            //energy storage
            drawContext.drawGuiTexture(ENERGY_STORAGE_SIDE, 6, 120, 0, 0, width / 32, height/2 - 60,6,120);
            drawEnergyStorage(drawContext, tickCounter, client.player, width / 32 + 12, height/2 - 60, 14, 120);

            //sight
            drawContext.drawGuiTexture(SIGHT, 64, 64, 0, 0, width/3 * 2 - 32, height/2 - 64,64,64);
            int abilityScreenX = width/3 * 2 + 28;
            int abilityScreenY = height/2 - 128;
            int abilityScreenWidth = 48;
            int abilityScreenHeight = 85;
            drawContext.drawGuiTexture(ABILITY_SCREEN, abilityScreenWidth, abilityScreenHeight, 0, 0, abilityScreenX, abilityScreenY, abilityScreenWidth, abilityScreenHeight);


            // Reset state
            RenderSystem.disableBlend();

            //abilities

            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            boolean hasChatOpen = MinecraftClient.getInstance().currentScreen instanceof ChatScreen;
            Text text;
            List<Integer> textLengths = new ArrayList<>();

            int y = abilityScreenY + 4;

            Ability firstAbility = ((PlayerAbilities) client.player).getFirstAbility();
            HudOverlay.drawAbility(drawContext, tickCounter, firstAbility, ModKeyBindings.useFirstAbility.isPressed(), abilityScreenX + 6, y);
            if (firstAbility != null) {
                text = hasChatOpen ? Text.translatable(firstAbility.getId().toTranslationKey()) : ModKeyBindings.useFirstAbility.getBoundKeyLocalizedText();
                textLengths.add(textRenderer.getWidth(text));
                drawContext.drawTextWithShadow(textRenderer, text, abilityScreenX + 6 + 18, y + 4, 0xFFFFFF);
            }
            y += 18;

            Ability secondAbility = ((PlayerAbilities) client.player).getSecondAbility();
            HudOverlay.drawAbility(drawContext, tickCounter, secondAbility, ModKeyBindings.useSecondAbility.isPressed(), abilityScreenX + 6, y);
            if (secondAbility != null) {
                text = hasChatOpen ? Text.translatable(secondAbility.getId().toTranslationKey()) : ModKeyBindings.useSecondAbility.getBoundKeyLocalizedText();
                textLengths.add(textRenderer.getWidth(text));
                drawContext.drawTextWithShadow(textRenderer, text, abilityScreenX + 6 + 18, y + 4, 0xFFFFFF);
            }
            y += 18;

            Ability thirdAbility = ((PlayerAbilities) client.player).getThirdAbility();
            HudOverlay.drawAbility(drawContext, tickCounter, thirdAbility, ModKeyBindings.useThirdAbility.isPressed(), abilityScreenX + 6, y);
            if (thirdAbility != null) {
                text = hasChatOpen ? Text.translatable(thirdAbility.getId().toTranslationKey()) : ModKeyBindings.useThirdAbility.getBoundKeyLocalizedText();
                textLengths.add(textRenderer.getWidth(text));
                drawContext.drawTextWithShadow(textRenderer, text, abilityScreenX + 6 + 18, y + 4, 0xFFFFFF);
            }
            y += 18;

            Ability fourthAbility = ((PlayerAbilities) client.player).getFourthAbility();
            HudOverlay.drawAbility(drawContext, tickCounter, fourthAbility, ModKeyBindings.useForthAbility.isPressed(), abilityScreenX + 6, y);
            if (fourthAbility != null) {
                text = hasChatOpen ? Text.translatable(fourthAbility.getId().toTranslationKey()) : ModKeyBindings.useForthAbility.getBoundKeyLocalizedText();
                textLengths.add(textRenderer.getWidth(text));
                drawContext.drawTextWithShadow(textRenderer, text, abilityScreenX + 6 + 18, y + 4, 0xFFFFFF);
            }


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

        List<Ability> abilities = ((PlayerAbilities) player).getAbilities();
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

                float chargePercentile = (float) charge / maxCharge;

                int currentHeight = (int) ((height - 4) * chargePercentile);

                drawContext.drawTexture(chargeIcon, x + 2 + 18 * i, y + 2 + (height - 4) - currentHeight, 0, (height - 4) - currentHeight, width - 4, currentHeight, 14, 14);
                drawContext.drawGuiTexture(ENERGY_STORAGE_BACKGROUND, width, height, 0, 0, x + 18 * i, y, width, height);
            }
        }
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
        return new MechanicalHudAbility(this.id, this.getName(), settings);
    }
}
