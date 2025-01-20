package net.togyk.myneheroes.block.screen.client.widget;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class ColorSliderWidget extends SliderWidget {
    private final String name;
    public ColorSliderWidget(String name, int x, int y, int width, int height, double value) {
        super(x, y, width, height, Text.of(name + ": " + (int) (value * 255)), value);
        this.name = name;
    }

    @Override
    protected void updateMessage() {
        // Update the slider's label dynamically based on its value
        this.setMessage(Text.of(this.name + ": " + getValue()));
    }

    @Override
    protected void applyValue() {
        // Use the slider's value here when the user interacts
        double newValue = this.value; // `value` is a double between 0.0 and 1.0
    }

    public int getValue() {
        return (int) (value * 255);
    }
}
