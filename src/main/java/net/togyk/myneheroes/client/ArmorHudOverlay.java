package net.togyk.myneheroes.client;

import com.google.common.base.Predicates;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.AdvancedArmorItem;
import net.togyk.myneheroes.Item.custom.ReactorItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.AbilityUtil;
import net.togyk.myneheroes.ability.StockpileAbility;
import net.togyk.myneheroes.keybind.ModKeyBindings;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.StockpilePower;
import net.togyk.myneheroes.util.PlayerAbilities;
import net.togyk.myneheroes.util.PowerData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
                if (advancedArmorItem.ShouldApplyHud(helmetStack) && client.options.getPerspective().isFirstPerson()) {

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

                    drawEnergyStorage(drawContext, tickCounter, client.player, 21, height/2 - 60);

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
    private void drawEnergyStorage(DrawContext drawContext, RenderTickCounter tickCounter, PlayerEntity player, int x, int y) {
        List<Power> powers = PowerData.getPowers(player);
        List<Power> stockpilePowers = powers.stream().filter(Predicates.instanceOf(StockpilePower.class)).toList();
        int i;
        for (i = 0; i < stockpilePowers.size(); i++) {
            if (stockpilePowers.get(i) instanceof StockpilePower power) {
                int charge = power.getCharge();
                int maxCharge = power.getMaxCharge();

                float chargePercentile = (float) charge / maxCharge;

                int maxHeight = 120;
                int currentHeight = (int) (maxHeight * chargePercentile);

                drawContext.drawTexture(power.getChargeIcon(), x + 16 * i, y + maxHeight - currentHeight, 0, maxHeight - currentHeight,14,currentHeight,14,14); // yellow rectangle
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

                int maxHeight = 120;
                int currentHeight = (int) (maxHeight * chargePercentile);

                drawContext.drawTexture(chargeIcon, x + 16 * i, y + maxHeight - currentHeight, 0, maxHeight - currentHeight, 14, currentHeight, 14, 14); // yellow rectangle
            }
        }
    }
    
    private List<Identifier> filterIds(List<Ability> abilities) {
        List<Identifier> ids = new ArrayList<>();
        for (Identifier id : abilities.stream().map(Ability::getId).toList()) {
            if (id != null && !ids.contains(id)) {
                ids.add(id);
            }
        }
        return ids;
    }
}
