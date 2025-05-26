package net.togyk.myneheroes.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.client.HudOverlay;

public class AbilityButtonWidget extends ButtonWidget {
    private final Ability ability;
    private final int index;

    public AbilityButtonWidget(int x, int y, int width, int height, Ability ability, int index, PressAction onPress) {
        super(x, y, width, height, Text.of(""), onPress, DEFAULT_NARRATION_SUPPLIER);
        this.ability = ability;
        this.index = index;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        HudOverlay.drawAbility(context, ability, this.isSelected(), getX(), getY());
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
