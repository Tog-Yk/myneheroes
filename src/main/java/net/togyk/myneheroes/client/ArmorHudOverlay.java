package net.togyk.myneheroes.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.AdvancedArmorItem;
import net.togyk.myneheroes.Item.custom.ReactorItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.keybind.ModKeyBindings;
import net.togyk.myneheroes.util.PlayerAbilities;

@Environment(EnvType.CLIENT)
public class ArmorHudOverlay implements HudRenderCallback {
    //gauge
    private static final Identifier CENTER_LINE_GAUGE = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/center_line_gauge.png");
    private static final Identifier LEVEL_GAUGE = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/level_gauge.png");

    //reactor info
    private static final Identifier BATTERY_BACKGROUND = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/battery_background.png");
    private static final Identifier BATTERY_CASING = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/battery_casing.png");
    private static final Identifier BATTERY = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/battery.png");
    private static final Identifier FUEL = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/fuel.png");
    private static final Identifier CONNECTED_CASE = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/connected_case.png");
    private static final Identifier CONNECTED = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/connected.png");

    // energy storage
    private static final Identifier ENERGY_STORAGE_SIDE = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/energy_storage_side.png");
    private static final Identifier POTENTIAL_ENERGY_BAR = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/potential_energy_bar.png");
    private static final Identifier KINETIC_ENERGY_BAR = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/kinetic_energy_bar.png");

    // sight
    private static final Identifier SIGHT = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/sight.png");
    private static final Identifier ABILITY_SCREEN_SIGHT = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/ability_screen.png");


    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            ItemStack helmetStack = client.player.getEquippedStack(EquipmentSlot.HEAD);
            if (helmetStack != null && helmetStack.getItem() instanceof AdvancedArmorItem advancedArmorItem) {
                if (advancedArmorItem.ShouldApplyHud() && client.options.getPerspective().isFirstPerson()) {

                    int width = drawContext.getScaledWindowWidth();
                    int height = drawContext.getScaledWindowHeight();

                    //gauge
                    drawContext.drawTexture(CENTER_LINE_GAUGE, width/2 - 15, 10, 0, 0, 30, 30, 30, 30);
                    drawContext.drawTexture(LEVEL_GAUGE, width/2 - 15, height -70, 0, 0, 30, 30, 30, 30);

                    //reactor info
                    drawContext.drawTexture(BATTERY_BACKGROUND, 10, height -10 -30, 0, 0, 18, 30, 18, 30);

                    ItemStack reactorItemStack = MyneHeroes.getReactorItemClass(client.player);
                    if (reactorItemStack != ItemStack.EMPTY && reactorItemStack.getItem() instanceof ReactorItem reactorItem) {
                        drawContext.drawTexture(CONNECTED, 30, height -10 -18, 0, 0, 24, 18, 24, 18);
                        drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, reactorItemStack.getName(),57,height -10 -8,0xFFFFFF);


                        float fuelPercentile = (float) reactorItem.getStoredFuelOrDefault(reactorItemStack, 0) / reactorItem.getMaxFuel();
                        int maxFuelLength = 26;
                        int currentFuelLength = (int) (maxFuelLength * fuelPercentile);
                        drawContext.drawTexture(FUEL, 10 + 2, height -10 -30 +2 + maxFuelLength - currentFuelLength,0,maxFuelLength - currentFuelLength,6, currentFuelLength,6,4);

                        float powerPercentile = (float) reactorItem.getStoredPowerOrDefault(reactorItemStack, 0) / reactorItem.getMaxPower();
                        int maxBatteryLength = 26;
                        int currentBatteryLength = (int) (maxBatteryLength * powerPercentile);
                        drawContext.drawTexture(BATTERY, 10 + 10, height -10 -30 +2 + maxBatteryLength - currentBatteryLength,0,maxBatteryLength - currentBatteryLength,6, currentBatteryLength,6,4);
                    }
                    drawContext.drawTexture(BATTERY_CASING, 10, height -10 -30, 0, 0, 18, 30, 18, 30);

                    drawContext.drawTexture(CONNECTED_CASE, 30, height -10 -18, 0, 0, 24, 18, 24, 18);
                    //energy storage
                    drawContext.drawTexture(ENERGY_STORAGE_SIDE, 10, height/2 - 60, 0, 0,6,120,6,120); // purple rectangle

                    drawContext.drawTexture(POTENTIAL_ENERGY_BAR, 21, height/2 - 60, 0, 0,14,120,32,32); // yellow rectangle
                    drawContext.drawTexture(KINETIC_ENERGY_BAR, 37, height/2 - 60, 0, 0,14,120,32,32); // purple rectangle
                    drawContext.drawTexture(BATTERY_CASING, 53, height/2 - 60, 0, 0,14,120,18,30); // white rectangle

                    //sight
                    drawContext.drawTexture(SIGHT, width/3 * 2 - 32, height/2 - 64, 0, 0,64,64,64,64);

                    int abilityScreenX = width/3 * 2 + 28;
                    int abilityScreenY = height/2 - 108;
                    int abilityScreenWidth = 48;
                    int abilityScreenHeight = 64;
                    drawContext.drawTexture(ABILITY_SCREEN_SIGHT, abilityScreenX, abilityScreenY, 0, 0,abilityScreenWidth,abilityScreenHeight,abilityScreenWidth,abilityScreenHeight);
                    Ability firstAbility = ((PlayerAbilities) client.player).getFirstAbility();
                    if (firstAbility != null) {
                        if (ModKeyBindings.useFirstAbility.isPressed()) {
                            drawContext.drawTexture(firstAbility.disabled_icon, abilityScreenX + 4, abilityScreenY + 4, 0, 0, 8, 8, 8, 8);
                        } else {
                            drawContext.drawTexture(firstAbility.icon, abilityScreenX + 4, abilityScreenY + 4, 0, 0, 8, 8, 8, 8);
                        }
                        if (firstAbility.getCooldown() != 0) {
                            float cooldownPercentile = (float) firstAbility.getCooldown() / firstAbility.getMaxCooldown();
                            int maxIconLength = 8;
                            int currentCooldownLength = (int) (maxIconLength * cooldownPercentile);
                            drawContext.fill(abilityScreenX + 4, abilityScreenY + 4 + maxIconLength - currentCooldownLength, abilityScreenX + 4 + 8, abilityScreenY + 4 + maxIconLength, 0x88BBBBBB);
                        }
                        drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.literal(firstAbility.getName()), abilityScreenX + 4+10, abilityScreenY + 4, 0xFFFFFF);
                    }
                    Ability secondAbility = ((PlayerAbilities) client.player).getSecondAbility();
                    if (secondAbility != null) {
                        if (ModKeyBindings.useSecondAbility.isPressed()) {
                            drawContext.drawTexture(secondAbility.disabled_icon, abilityScreenX + 4, abilityScreenY + 14, 0, 0, 8, 8, 8, 8);
                        } else {
                            drawContext.drawTexture(secondAbility.icon, abilityScreenX + 4, abilityScreenY + 14, 0, 0, 8, 8, 8, 8);
                        }
                        if (secondAbility.getCooldown() != 0) {
                            float cooldownPercentile = (float) secondAbility.getCooldown() / secondAbility.getMaxCooldown();
                            int maxIconLength = 8;
                            int currentCooldownLength = (int) (maxIconLength * cooldownPercentile);
                            drawContext.fill(abilityScreenX + 4, abilityScreenY + 14 + maxIconLength - currentCooldownLength, abilityScreenX + 4 + 8, abilityScreenY + 14 + maxIconLength, 0x88BBBBBB);
                        }
                        drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.literal(secondAbility.getName()), abilityScreenX + 4+10, abilityScreenY + 14, 0xFFFFFF);
                    }
                    Ability thirdAbility = ((PlayerAbilities) client.player).getThirdAbility();
                    if (thirdAbility != null) {
                        if (ModKeyBindings.useThirdAbility.isPressed()) {
                            drawContext.drawTexture(thirdAbility.disabled_icon, abilityScreenX + 4, abilityScreenY + 24, 0, 0, 8, 8, 8, 8);
                        } else {
                            drawContext.drawTexture(thirdAbility.icon, abilityScreenX + 4, abilityScreenY + 24, 0, 0, 8, 8, 8, 8);
                        }
                        if (thirdAbility.getCooldown() != 0) {
                            float cooldownPercentile = (float) thirdAbility.getCooldown() / thirdAbility.getMaxCooldown();
                            int maxIconLength = 8;
                            int currentCooldownLength = (int) (maxIconLength * cooldownPercentile);
                            drawContext.fill(abilityScreenX + 4, abilityScreenY + 24 + maxIconLength - currentCooldownLength, abilityScreenX + 4 + 8, abilityScreenY + 24 + maxIconLength, 0x88BBBBBB);
                        }
                        drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.literal(thirdAbility.getName()), abilityScreenX + 4+10, abilityScreenY + 24, 0xFFFFFF);
                    }
                    Ability fourthAbility = ((PlayerAbilities) client.player).getFourthAbility();
                    if (fourthAbility != null) {
                        if (ModKeyBindings.useForthAbility.isPressed()) {
                            drawContext.drawTexture(fourthAbility.disabled_icon, abilityScreenX + 4, abilityScreenY + 34, 0, 0, 8, 8, 8, 8);
                        } else {
                            drawContext.drawTexture(fourthAbility.icon, abilityScreenX + 4, abilityScreenY + 34, 0, 0, 8, 8, 8, 8);
                        }
                        if (fourthAbility.getCooldown() != 0) {
                            float cooldownPercentile = (float) fourthAbility.getCooldown() / fourthAbility.getMaxCooldown();
                            int maxIconLength = 8;
                            int currentCooldownLength = (int) (maxIconLength * cooldownPercentile);
                            drawContext.fill(abilityScreenX + 4, abilityScreenY + 34 + maxIconLength - currentCooldownLength, abilityScreenX + 4 + 8, abilityScreenY + 34 + maxIconLength, 0x88BBBBBB);
                        }
                        drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.literal(fourthAbility.getName()), abilityScreenX + 4+10, abilityScreenY + 34, 0xFFFFFF);
                    }
                }
            }
        }
    }
}
