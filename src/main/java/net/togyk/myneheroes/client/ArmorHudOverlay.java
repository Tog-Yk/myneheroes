package net.togyk.myneheroes.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.AdvancedArmorItem;
import net.togyk.myneheroes.MyneHeroes;

@Environment(EnvType.CLIENT)
public class ArmorHudOverlay implements HudRenderCallback {
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
            "textures/gui/end_portal.png");
    private static final Identifier POTENTIAL_ENERGY_BAR = Identifier.of(MyneHeroes.MOD_ID,
            "textures/gui/end_portal.png");


    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
            if (helmet != null && helmet.getItem() instanceof AdvancedArmorItem advancedArmorItem) {
                if (advancedArmorItem.ShouldApplyHud()) {
                    drawContext.drawText(MinecraftClient.getInstance().textRenderer, Text.literal("should apply HUD"), 10, 10, 0xFFFFFF, false);

                    int height = drawContext.getScaledWindowHeight();

                    //reactor info
                    drawContext.drawTexture(BATTERY_CASING, 10, height -10 -30, 0, 0, 18, 30, 18, 30);

                    drawContext.drawTexture(CONNECTED_CASE, 30, height -10 -18, 0, 0, 24, 18, 24, 18);
                    drawContext.drawTexture(CONNECTED, 30, height -10 -18, 0, 0, 24, 18, 24, 18);

                    drawContext.drawTexture(BATTERY_CASING, 10, height/2 - 60, 0, 0,5,120,18,30); // purple rectangle

                    drawContext.drawTexture(BATTERY_CASING, 20, height/2 - 60, 0, 0,18,120,18,30); // purple rectangle
                    drawContext.drawTexture(BATTERY_CASING, 40, height/2 - 60, 0, 0,18,120,18,30); // yellow rectangle
                    drawContext.drawTexture(BATTERY_CASING, 60, height/2 - 60, 0, 0,18,120,18,30); // white rectangle
                }
            } else {
                return;
            }
        }
    }
}
