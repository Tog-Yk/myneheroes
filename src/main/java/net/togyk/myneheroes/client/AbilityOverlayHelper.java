package net.togyk.myneheroes.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.Window;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.ActivationTimedAbility;
import net.togyk.myneheroes.ability.ItemRenderableAbility;
import net.togyk.myneheroes.ability.VariableLinkedAbility;
import net.togyk.myneheroes.keybind.ModKeyBinds;
import net.togyk.myneheroes.util.PlayerAbilities;

public class AbilityOverlayHelper {
    public static void drawAbilitiesWithBackground(PlayerEntity player, DrawContext drawContext, boolean textLeftOfAbilities, int startX, int startY, Identifier identifier, int width, int height, int offsetX, int offsetY) {
        drawAbilitiesWithBackground(player, drawContext, textLeftOfAbilities, 0xFFFFFFFF, startX, startY, 2, identifier, width, height, offsetX, offsetY);
    }

    public static void drawAbilitiesWithBackground(PlayerEntity player, DrawContext drawContext, boolean textLeftOfAbilities, int startX, int startY, int spacing, Identifier identifier, int width, int height, int offsetX, int offsetY) {
        drawAbilitiesWithBackground(player, drawContext, textLeftOfAbilities, 0xFFFFFFFF, startX, startY, spacing, identifier, width, height, offsetX, offsetY);
    }

    public static void drawAbilitiesWithBackground(PlayerEntity player, DrawContext drawContext, int textColor, boolean textLeftOfAbilities, int startX, int startY, Identifier identifier, int width, int height, int offsetX, int offsetY) {
        drawAbilitiesWithBackground(player, drawContext, textLeftOfAbilities, textColor, startX, startY, 2, identifier, width, height, offsetX, offsetY);
    }

    public static void drawAbilitiesWithBackground(PlayerEntity player, DrawContext drawContext, boolean textLeftOfAbilities, int textColor, int startX, int startY, int spacing, Identifier identifier, int width, int height, int offsetX, int offsetY) {
        if (!((PlayerAbilities) player).myneheroes$getFilteredAbilities().isEmpty()) {
            drawContext.drawGuiTexture(identifier, startX, startY, width, height);
        }
        drawAbilities(player, drawContext, textLeftOfAbilities, textColor, startX + offsetX, startY + offsetY, spacing);
    }

    public static void drawAbilities(PlayerEntity player, DrawContext drawContext, boolean textLeftOfAbilities, int startX, int startY) {
        drawAbilities(player, drawContext, textLeftOfAbilities, 0xFFFFFFFF, startX, startY, 2);
    }

    public static void drawAbilities(PlayerEntity player, DrawContext drawContext, boolean textLeftOfAbilities, int startX, int startY, int spacing) {
        drawAbilities(player, drawContext, textLeftOfAbilities, 0xFFFFFFFF, startX, startY, spacing);
    }

    public static void drawAbilities(PlayerEntity player, DrawContext drawContext, int textColor, boolean textLeftOfAbilities, int startX, int startY) {
        drawAbilities(player, drawContext, textLeftOfAbilities, textColor, startX, startY, 2);
    }

    public static void drawAbilities(PlayerEntity player, DrawContext drawContext, boolean textLeftOfAbilities, int textColor, int startX, int startY, int spacing) {
        boolean hasChatOpen = MinecraftClient.getInstance().currentScreen instanceof ChatScreen;

        int y = startY;

        Ability abilityBeforeFirst = ((PlayerAbilities) player).myneheroes$getAbilityBeforeFirst();
        drawHalfAbility(drawContext, abilityBeforeFirst, false, false, startX, y);
        y += 8 + spacing;

        Ability firstAbility = ((PlayerAbilities) player).myneheroes$getFirstAbility();
        if (hasChatOpen) {
            drawAbilityWithName(drawContext, firstAbility, textLeftOfAbilities, textColor, ModKeyBinds.useFirstAbility.isPressed(), startX, y);
        } else {
            drawAbilityWithText(drawContext, firstAbility, ModKeyBinds.useFirstAbility.getBoundKeyLocalizedText(), textLeftOfAbilities, textColor, ModKeyBinds.useFirstAbility.isPressed(), startX, y);
        }
        y += 16 + spacing;

        Ability secondAbility = ((PlayerAbilities) player).myneheroes$getSecondAbility();
        if (hasChatOpen) {
            drawAbilityWithName(drawContext, secondAbility, textLeftOfAbilities, textColor, ModKeyBinds.useSecondAbility.isPressed(), startX, y);
        } else {
            drawAbilityWithText(drawContext, secondAbility, ModKeyBinds.useSecondAbility.getBoundKeyLocalizedText(), textLeftOfAbilities, textColor, ModKeyBinds.useSecondAbility.isPressed(), startX, y);
        }
        y += 16 + spacing;

        Ability thirdAbility = ((PlayerAbilities) player).myneheroes$getThirdAbility();
        if (hasChatOpen) {
            drawAbilityWithName(drawContext, thirdAbility, textLeftOfAbilities, textColor, ModKeyBinds.useThirdAbility.isPressed(), startX, y);
        } else {
            drawAbilityWithText(drawContext, thirdAbility, ModKeyBinds.useThirdAbility.getBoundKeyLocalizedText(), textLeftOfAbilities, textColor, ModKeyBinds.useThirdAbility.isPressed(), startX, y);
        }
        y += 16 + spacing;

        Ability fourthAbility = ((PlayerAbilities) player).myneheroes$getFourthAbility();
        if (hasChatOpen) {
            drawAbilityWithName(drawContext, fourthAbility, textLeftOfAbilities, textColor, ModKeyBinds.useFourthAbility.isPressed(), startX, y);
        } else {
            drawAbilityWithText(drawContext, fourthAbility, ModKeyBinds.useFourthAbility.getBoundKeyLocalizedText(), textLeftOfAbilities, textColor, ModKeyBinds.useFourthAbility.isPressed(), startX, y);
        }
        y += 16 + spacing;

        Ability fifthAbility = ((PlayerAbilities) player).myneheroes$getFifthAbility();
        drawHalfAbility(drawContext, fifthAbility, true, false, startX, y);
    }

    public static void drawAbilityWithName(DrawContext drawContext, Ability ability, boolean nameLeftOfAbility, boolean isPressed, int x, int y) {
        drawAbilityWithName(drawContext, ability, nameLeftOfAbility, 0xFFFFFFFF, isPressed, x, y);
    }
    public static void drawAbilityWithName(DrawContext drawContext, Ability ability, boolean nameLeftOfAbility, int nameColor, boolean isPressed, int x, int y) {
        drawAbilityWithName(drawContext, ability, nameLeftOfAbility, nameColor, isPressed, x, y, nameLeftOfAbility ? -2 : 18, 4);
    }

    public static void drawAbilityWithName(DrawContext drawContext, Ability ability, boolean nameLeftOfAbility, int nameColor, boolean isPressed, int x, int y, int textOffsetX, int textOffsetY) {
        if (ability != null) {
            drawAbilityWithText(drawContext, ability, ability.getName(), nameLeftOfAbility, nameColor, isPressed, x, y, textOffsetX, textOffsetY);
        }
    }


    public static void drawAbilityWithText(DrawContext drawContext, Ability ability, Text text, boolean textLeftOfAbility, boolean isPressed, int x, int y) {
        drawAbilityWithText(drawContext, ability, text, textLeftOfAbility, 0xFFFFFFFF, isPressed, x, y);
    }

    public static void drawAbilityWithText(DrawContext drawContext, Ability ability, Text text, boolean textLeftOfAbility, int textColor, boolean isPressed, int x, int y) {
        drawAbilityWithText(drawContext, ability, text, textLeftOfAbility, textColor, isPressed, x, y, textLeftOfAbility ? 8 : 12, 10);
    }

    public static void drawAbilityWithText(DrawContext drawContext, Ability ability, Text text, boolean textLeftOfAbility, int textColor, boolean isPressed, int x, int y, int textOffestX, int textOffsetY) {
        if (ability != null) {
            RenderSystem.enableBlend();
            drawAbility(drawContext, ability, isPressed, x, y);

            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            int textX = textLeftOfAbility ? x + textOffestX - 4 - textRenderer.getWidth(text) : x + textOffestX;
            drawContext.drawTextWithShadow(textRenderer, text, textX, y + textOffsetY, textColor);
        }
    }

    public static void drawAbility(DrawContext drawContext, Ability ability, boolean isPressed, int x, int y) {
        if (ability != null) {
            if (ability instanceof ItemRenderableAbility itemRenderable) {
                drawContext.drawItem(itemRenderable.getItem(), x, y);
            } else {
                drawContext.drawTexture(ability.getIcon(), x, y, 0, 0, 16, 16, 16, 16);
            }
            if (!ability.isUsable()) {
                drawContext.fill(x, y, x + 16, y + 16, 0x88333333);
            } else if (isPressed && !ability.canHold(MinecraftClient.getInstance().player)) {
                drawContext.fill(x, y, x + 16, y + 16, 0x33000066);
            }

            int maxIconLength = 16;
            if (ability.getCooldown() != 0) {
                float cooldownPercentile = (float) ability.getCooldown() / ability.getMaxCooldown();
                int currentCooldownLength = (int) (maxIconLength * cooldownPercentile);
                drawContext.fill(x, y + maxIconLength - currentCooldownLength, x + 16, y + maxIconLength, 0x88BBBBBB);
            } else if (ability.getHoldTime() != 0) {
                float holdTimePercentile = (float) ability.getHoldTime() / ability.getMaxHoldTime();
                int currentHoldTimeLength = (int) (maxIconLength * holdTimePercentile);
                drawContext.fill(x, y + maxIconLength - currentHoldTimeLength, x + 16, y + maxIconLength, 0x88BBBBBB);
            } else if (ability instanceof ActivationTimedAbility timedAbility && timedAbility.isActivated()) {
                if (timedAbility.getActivatedTime() != 0) {
                    float activatedTimePercentile = (float) timedAbility.getActivatedTime() / timedAbility.getMaxActivatedTime();
                    int currentActivatedTimeLength = (int) ((maxIconLength - 1) * activatedTimePercentile) + 1;
                    drawContext.fill(x, y + maxIconLength - currentActivatedTimeLength, x + 16, y + maxIconLength, 0x88000066);
                }
            }
            if (ability instanceof VariableLinkedAbility variableLinkedAbility) {
                TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
                Object variable = variableLinkedAbility.getVariable();

                if (variable != null) {
                    Text text = Text.literal(String.valueOf(variable));
                    drawContext.drawTextWithShadow(textRenderer, text, x, y, 0xFFFFFFFF);
                }
            }
        }
    }

    public static void drawHalfAbility(DrawContext drawContext, Ability ability, boolean topHalf, boolean isPressed, int x, int y) {
        if (ability != null) {
            easyEnableScissor(x, y, 16, 8);
            drawAbility(drawContext, ability, isPressed, x, topHalf ? y : y - 8);

            RenderSystem.setShaderColor(0, 0,0,0.5F);
            RenderSystem.enableBlend();

            drawAbility(drawContext, ability, isPressed, x, topHalf ? y : y - 8);
            RenderSystem.setShaderColor(1,1,1,1);

            RenderSystem.disableScissor();
        }
    }

    private static void easyEnableScissor(int x, int y, int width, int height) {
        Window window = MinecraftClient.getInstance().getWindow();
        double scale = window.getScaleFactor();

        RenderSystem.enableScissor((int) (x * scale), (int) (window.getHeight() - (y + height) * scale), (int) (width * scale), (int) (height * scale));
    }
}
