package net.togyk.myneheroes.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;

public class StackButtonWidget extends ButtonWidget {
    private static Identifier SLOT = Identifier.of(MyneHeroes.MOD_ID,
            "toolbelt/slot");
    private static Identifier SELECTED_SLOT = Identifier.of(MyneHeroes.MOD_ID,
            "toolbelt/slot_selected");

    private final ItemStack stack;
    private final int index;

    public StackButtonWidget(int x, int y, int width, int height, ItemStack stack, int index, PressAction onPress) {
        super(x, y, width, height, stack.getName(), onPress, DEFAULT_NARRATION_SUPPLIER);
        this.stack = stack;
        this.index = index;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        context.drawGuiTexture(SLOT, getX() - 3, getY() - 3, 22, 22);
        if (this.isHovered()) {
            context.drawGuiTexture(SELECTED_SLOT, getX() - 4, getY() - 4, 24, 24);
        }
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        context.drawItem(stack, getX(), getY());
        context.drawItemInSlot(textRenderer, stack, getX(), getY());
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public int getWidth() {
        return super.getWidth();
    }

    public int getIndex() {
        return index;
    }
}
