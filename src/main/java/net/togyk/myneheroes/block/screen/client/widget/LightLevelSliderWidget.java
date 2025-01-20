package net.togyk.myneheroes.block.screen.client.widget;

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
        // Use the slider's value here when the user interacts
        double newValue = this.value; // `value` is a double between 0.0 and 1.0
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
}
