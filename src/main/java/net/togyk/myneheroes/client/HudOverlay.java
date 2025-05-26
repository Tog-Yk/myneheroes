package net.togyk.myneheroes.client;

import com.google.common.base.Predicates;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Abilities;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.util.AbilityUtil;
import net.togyk.myneheroes.ability.HudAbility;
import net.togyk.myneheroes.ability.StockpileAbility;
import net.togyk.myneheroes.keybind.ModKeyBindings;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.StockpilePower;
import net.togyk.myneheroes.util.*;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class HudOverlay implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            boolean hasDrawnAbilities = false;
            boolean hasDrawnPowers = false;

            List<Ability> abilities = ((PlayerAbilities) client.player).getAbilities();
            List<HudAbility> hudAbilities = getHudAbilities(abilities);
            for (HudAbility ability : hudAbilities) {
                HudActionResult result = ability.drawHud(drawContext, tickCounter);

                switch (result) {
                    case ABILITIES_AND_POWER_HUD_DRAWN:
                        hasDrawnAbilities = true;
                        hasDrawnPowers = true;
                    case POWER_HUD_DRAWN:
                        hasDrawnPowers = true;
                    case ABILITIES_HUD_DRAWN:
                        hasDrawnAbilities = true;
                }
            }

            int width = drawContext.getScaledWindowWidth();
            int height = drawContext.getScaledWindowHeight();

            if (!hasDrawnAbilities) {
                //draw Ability Hud
                TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
                boolean hasChatOpen = MinecraftClient.getInstance().currentScreen instanceof ChatScreen;
                Text text;

                int y = 4;

                Ability abilityBeforeFirst = ((PlayerAbilities) client.player).getAbilityBeforeFirst();
                if (abilityBeforeFirst != null) {
                    drawContext.drawTexture(abilityBeforeFirst.icon, width - 20, y, 0, 8, 16, 8, 16, 16);
                    drawContext.fill(width - 20, y, width - 4, y + 8, 0x70000000);
                }
                y += 10;

                Ability firstAbility = ((PlayerAbilities) client.player).getFirstAbility();
                drawAbility(drawContext, firstAbility, ModKeyBindings.useFirstAbility.isPressed(), width - 20, y);
                if (firstAbility != null) {
                    text = hasChatOpen ? Text.translatable("ability."+firstAbility.getId().toTranslationKey()) : ModKeyBindings.useFirstAbility.getBoundKeyLocalizedText();
                    drawContext.drawTextWithShadow(textRenderer, text, width - 6 - 18 - textRenderer.getWidth(text), y + 4, 0xFFFFFF);
                }
                y += 18;

                Ability secondAbility = ((PlayerAbilities) client.player).getSecondAbility();
                drawAbility(drawContext, secondAbility, ModKeyBindings.useSecondAbility.isPressed(), width - 20, y);
                if (secondAbility != null) {
                    text = hasChatOpen ? Text.translatable("ability."+secondAbility.getId().toTranslationKey()) : ModKeyBindings.useSecondAbility.getBoundKeyLocalizedText();
                    drawContext.drawTextWithShadow(textRenderer, text, width - 6 - 18 - textRenderer.getWidth(text), y + 4, 0xFFFFFF);
                }
                y += 18;

                Ability thirdAbility = ((PlayerAbilities) client.player).getThirdAbility();
                drawAbility(drawContext, thirdAbility, ModKeyBindings.useThirdAbility.isPressed(), width - 20, y);
                if (thirdAbility != null) {
                    text = hasChatOpen ? Text.translatable("ability."+thirdAbility.getId().toTranslationKey()) : ModKeyBindings.useThirdAbility.getBoundKeyLocalizedText();
                    drawContext.drawTextWithShadow(textRenderer, text, width - 6 - 18 - textRenderer.getWidth(text), y + 4, 0xFFFFFF);
                }
                y += 18;

                Ability fourthAbility = ((PlayerAbilities) client.player).getFourthAbility();
                drawAbility(drawContext, fourthAbility, ModKeyBindings.useFourthAbility.isPressed(), width - 20, y);
                if (fourthAbility != null) {
                    text = hasChatOpen ? Text.translatable("ability."+fourthAbility.getId().toTranslationKey()) : ModKeyBindings.useFourthAbility.getBoundKeyLocalizedText();
                    drawContext.drawTextWithShadow(textRenderer, text, width - 6 - 18 - textRenderer.getWidth(text), y + 4, 0xFFFFFF);
                }
                y += 18;

                Ability fifthAbility = ((PlayerAbilities) client.player).getFifthAbility();
                if (fifthAbility != null) {
                    drawContext.drawTexture(fifthAbility.icon, width - 20, y, 0, 0, 16, 8, 16, 16);
                    drawContext.fill(width - 20, y, width - 4, y + 8, 0x70000000);
                }
            }
            if (!hasDrawnPowers) {
                List<Power> powers = ((PlayerPowers) client.player).getPowers();
                if (!powers.isEmpty()) {
                    //draw Power Hud
                    int scrolled = ScrollData.getScrolledPowersOffset(client.player);
                    if (powers.size() > scrolled) {
                        Power power = powers.get(scrolled);

                        drawContext.drawTexture(power.getBackground(), width - 112, height - 32, 0, 0, 112, 32, 112, 32);
                        drawPowerInfo(drawContext, tickCounter, power, power.isDampened(), width - 112, height - 32);

                        if (powers.size() > 1) {
                            drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.literal("+" + (powers.size() - 1)), width - 112 + 4, height - 32 + 16 + 4, 0xFFFFFF);
                        }
                    }
                }
            }
        }
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

                int currentHeight = (int) (height * chargePercentile);

                drawContext.drawTexture(power.getChargeIcon(), x + 16 * i, y + height - currentHeight, 0, height - currentHeight, width, currentHeight, 14, 14); // yellow rectangle
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

                int currentHeight = (int) (height * chargePercentile);

                drawContext.drawTexture(chargeIcon, x + 18 * i, y + height - currentHeight, 0, height - currentHeight, 16, currentHeight, 16, 16); // yellow rectangle
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

    public static void drawAbility(DrawContext drawContext, Ability ability, boolean isDisabled, int x, int y) {
        if (ability != null) {
            if (isDisabled) {
                drawContext.drawTexture(ability.disabled_icon, x, y, 0, 0, 16, 16, 16, 16);
            } else {
                drawContext.drawTexture(ability.icon, x, y, 0, 0, 16, 16, 16, 16);
            }
            if (ability.getCooldown() != 0) {
                float cooldownPercentile = (float) ability.getCooldown() / ability.getMaxCooldown();
                int maxIconLength = 16;
                int currentCooldownLength = (int) (maxIconLength * cooldownPercentile);
                drawContext.fill(x, y + maxIconLength - currentCooldownLength, x + 16, y + maxIconLength, 0x88BBBBBB);
            }
        }
    }

    public static void drawPowerInfo(DrawContext drawContext, RenderTickCounter tickCounter, Power power, boolean isDisabled, int x, int y) {
        if (power != null) {
            Text powerName = Text.translatable("power."+power.getId().toTranslationKey());
            drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, powerName, x + 4, y + 8 + 4, 0xFFFFFF);
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
