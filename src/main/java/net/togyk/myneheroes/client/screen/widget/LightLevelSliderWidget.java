package net.togyk.myneheroes.client.screen.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class LightLevelSliderWidget extends SliderWidget {
    private final String name;
    public LightLevelSliderWidget(String name, int x, int y, int width, int height, double value) {
        super(x, y, width, height, (int) ((value * 16) - 1) == -1 ? Text.of("Normal " + name) : Text.of(name + ": " + (int) ((value * 16) -1)), value);
        this.name = name;
    }

    @Override
    protected void updateMessage() {
        // Update the slider's label dynamically based on its value
        int lightLevel = getLightLevel();
        if (lightLevel == -1) {
            this.setMessage(Text.of("Normal " + name));
        } else {
            this.setMessage(Text.of(this.name + ": " + lightLevel));
        }
    }

    @Override
    protected void applyValue() {
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        if (visible) {
            super.renderWidget(context, mouseX, mouseY, delta);
        }
    }

    public int getLightLevel() {
        return (int) ((value * 16) -1);
    }

    public void setLightLevel(int lightLevel) {
        if (lightLevel + 1 > 16 || lightLevel + 1 < 0) {
            return;
        }
        this.value = (double) (lightLevel + 1) / 16;
        this.updateMessage();
    }
}
