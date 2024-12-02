package net.togyk.myneheroes.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.AdvancedArmorItem;
import net.togyk.myneheroes.Item.custom.ReactorItem;
import net.togyk.myneheroes.MyneHeroes;

@Environment(EnvType.CLIENT)
public class ArmorHudOverlay implements HudRenderCallback {
    //gauge
    private static final Identifier CENTER_LINE_GAUGE = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/center_line_gauge.png");
    private static final Identifier LEVEL_GAUGE = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/level_gauge.png");

    //reactor info
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

    /**
     * Searches the player's inventory for the first matching item.
     *
     * @param player The player to search in.
     * @return The matching ItemStack, or an empty ItemStack if not found.
     */
    private ItemStack getReactorItemClass(PlayerEntity player) {
        PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.getItem() instanceof ReactorItem) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            ItemStack helmetStack = client.player.getEquippedStack(EquipmentSlot.HEAD);
            if (helmetStack != null && helmetStack.getItem() instanceof AdvancedArmorItem advancedArmorItem) {
                if (advancedArmorItem.ShouldApplyHud(helmetStack)) {

                    int width = drawContext.getScaledWindowWidth();
                    int height = drawContext.getScaledWindowHeight();

                    //gauge
                    drawContext.drawTexture(CENTER_LINE_GAUGE, width/2 - 15, 10, 0, 0, 30, 30, 30, 30);
                    drawContext.drawTexture(LEVEL_GAUGE, width/2 - 15, height -70, 0, 0, 30, 30, 30, 30);

                    //reactor info
                    ItemStack reactorItemStack = getReactorItemClass(client.player);
                    if (reactorItemStack != ItemStack.EMPTY && reactorItemStack.getItem() instanceof ReactorItem reactorItem) {
                        drawContext.drawTexture(CONNECTED, 30, height -10 -18, 0, 0, 24, 18, 24, 18);
                        drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, reactorItemStack.getName(),57,height -10 -8,0xFFFFFF);


                        float powerPercentile = (float) reactorItem.getStoredPowerOrDefault(reactorItemStack, 0) / reactorItem.getMaxPower();
                        int maxBatteryLength = 26;
                        int currentBatteryLength = (int) (maxBatteryLength * powerPercentile);
                        drawContext.drawTexture(BATTERY, 10 + 2, height -10 -30 +2 + maxBatteryLength - currentBatteryLength,0,0,14, currentBatteryLength,14,4);
                    }
                    drawContext.drawTexture(BATTERY_CASING, 10, height -10 -30, 0, 0, 18, 30, 18, 30);

                    drawContext.drawTexture(CONNECTED_CASE, 30, height -10 -18, 0, 0, 24, 18, 24, 18);
                    //energy storage
                    drawContext.drawTexture(ENERGY_STORAGE_SIDE, 10, height/2 - 60, 0, 0,6,120,6,120); // purple rectangle

                    drawContext.drawTexture(POTENTIAL_ENERGY_BAR, 21, height/2 - 60, 0, 0,14,120,32,32); // yellow rectangle
                    drawContext.drawTexture(KINETIC_ENERGY_BAR, 37, height/2 - 60, 0, 0,14,120,32,32); // purple rectangle
                    drawContext.drawTexture(BATTERY_CASING, 53, height/2 - 60, 0, 0,14,120,18,30); // white rectangle

                    //sight
                    drawContext.drawTexture(SIGHT, width/3 * 2 - 32, height/2 - 64, 0, 0,64,64,64,64); // white rectangle
                    drawContext.drawTexture(ABILITY_SCREEN_SIGHT, width/3 * 2 + 28, height/2 - 108, 0, 0,48,64,48,64); // white rectangle
                }
            } else {
                return;
            }
        }
    }
}
