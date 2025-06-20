package net.togyk.myneheroes.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.client.screen.handeler.UpgradeStationScreenHandler;

public class UpgradeStationScreen extends HandledScreen<UpgradeStationScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(MyneHeroes.MOD_ID, "textures/gui/container/upgrade_station.png");

    public UpgradeStationScreen(UpgradeStationScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = this.x;
        int j = this.y;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        context.drawText(MinecraftClient.getInstance().textRenderer, this.handler.getBlockEntity().getDisplayName2(),  i + 8, j + 15, 0xFF373737, false);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
