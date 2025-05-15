package net.togyk.myneheroes.ability.detailed;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.ReactorItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.HudAbility;
import net.togyk.myneheroes.client.HudOverlay;
import net.togyk.myneheroes.keybind.ModKeyBindings;
import net.togyk.myneheroes.util.HudActionResult;
import net.togyk.myneheroes.util.PlayerAbilities;

public class MechanicalHudAbility extends HudAbility {
    //gauge
    private static final Identifier CENTER_LINE_GAUGE = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/hud/mechanical/center_line_gauge.png");
    private static final Identifier LEVEL_GAUGE = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/hud/mechanical/level_gauge.png");

    //reactor info
    private static final Identifier BATTERY_BACKGROUND = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/hud/mechanical/battery_background.png");
    private static final Identifier BATTERY_CASING = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/hud/mechanical/battery_casing.png");
    private static final Identifier BATTERY = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/hud/mechanical/battery.png");
    private static final Identifier FUEL = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/hud/mechanical/fuel.png");
    private static final Identifier CONNECTED_CASE = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/hud/mechanical/connected_case.png");
    private static final Identifier CONNECTED = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/hud/mechanical/connected.png");

    // energy storage
    private static final Identifier ENERGY_STORAGE_SIDE = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/hud/mechanical/energy_storage_side.png");

    // sight
    private static final Identifier SIGHT = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/hud/mechanical/sight.png");
    private static final Identifier ABILITY_SCREEN_SIGHT = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/hud/mechanical/ability_screen.png");

    public MechanicalHudAbility(Identifier id, String name, Ability.Settings settings) {
        super(id, name, settings);
    }

    @Override
    public HudActionResult drawHud(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (this.get() && client.player != null && client.options.getPerspective().isFirstPerson()) {

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
            drawContext.drawTexture(ENERGY_STORAGE_SIDE, 10, height/2 - 60, 0, 0,6,120,6,120);

            HudOverlay.drawEnergyStorage(drawContext, tickCounter, client.player, 21, height/2 - 60);

            //sight
            drawContext.drawTexture(SIGHT, width/3 * 2 - 32, height/2 - 64, 0, 0,64,64,64,64);

            int abilityScreenX = width/3 * 2 + 28;
            int abilityScreenY = height/2 - 108;
            int abilityScreenWidth = 48;
            int abilityScreenHeight = 64;
            drawContext.drawTexture(ABILITY_SCREEN_SIGHT, abilityScreenX, abilityScreenY, 0, 0,abilityScreenWidth,abilityScreenHeight,abilityScreenWidth,abilityScreenHeight);

            Ability firstAbility = ((PlayerAbilities) client.player).getFirstAbility();
            HudOverlay.drawAbility(drawContext, tickCounter, firstAbility, ModKeyBindings.useFirstAbility.isPressed(), abilityScreenX + 4, abilityScreenY + 4);

            Ability secondAbility = ((PlayerAbilities) client.player).getSecondAbility();
            HudOverlay.drawAbility(drawContext, tickCounter, secondAbility, ModKeyBindings.useSecondAbility.isPressed(), abilityScreenX + 4, abilityScreenY + 14);

            Ability thirdAbility = ((PlayerAbilities) client.player).getThirdAbility();
            HudOverlay.drawAbility(drawContext, tickCounter, thirdAbility, ModKeyBindings.useThirdAbility.isPressed(), abilityScreenX + 4, abilityScreenY + 24);

            Ability fourthAbility = ((PlayerAbilities) client.player).getFourthAbility();
            HudOverlay.drawAbility(drawContext, tickCounter, fourthAbility, ModKeyBindings.useForthAbility.isPressed(), abilityScreenX + 4, abilityScreenY + 34);

            return HudActionResult.ABILITIES_HUD_DRAWN;
        }
        return HudActionResult.NO_HUD_DRAWN;
    }

    @Override
    public MechanicalHudAbility copy() {
        return new MechanicalHudAbility(this.id, this.getName(), settings);
    }
}
