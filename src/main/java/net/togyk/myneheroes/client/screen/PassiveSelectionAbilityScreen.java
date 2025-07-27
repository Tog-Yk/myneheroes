package net.togyk.myneheroes.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.client.screen.handeler.PassiveSelectionAbilityScreenHandler;
import net.togyk.myneheroes.client.screen.widget.AbilityButtonWidget;

import java.util.ArrayList;
import java.util.List;

public class PassiveSelectionAbilityScreen extends HandledScreen<PassiveSelectionAbilityScreenHandler> {
    private List<AbilityButtonWidget> abilityButtonWidgets = new ArrayList<>();

    public PassiveSelectionAbilityScreen(PassiveSelectionAbilityScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);

        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();

        updateAbilityButtons(width, height);
        for (AbilityButtonWidget button : this.abilityButtonWidgets) {
            button.render(context, mouseX, mouseY, delta);
        }
    }

    public void updateAbilityButtons(int width, int height) {
        List<AbilityButtonWidget> stackButtons = new ArrayList<>();

        List<Ability> abilities = this.handler.getAbility().getAbilities();
        int i = 0;
        for (int radius = 24; (radius < height / 3 || radius < width / 3) && i < abilities.size(); radius += 24) {
            double circumference = 2* radius *Math.PI;
            int initialI = i;
            int amount = (int) (circumference / 32);
            for (i = initialI; i < initialI + amount && i < abilities.size(); i++) {

                double angle = 2 * Math.PI * (i - initialI) / Math.min(amount, abilities.size() - initialI) - 0.5*Math.PI;
                int xOffset = (int) (Math.cos(angle) * radius);
                int yOffset = (int) (Math.sin(angle) * radius);

                int x = (width / 2) + xOffset;
                int y = (height / 2) + yOffset;

                stackButtons.add(new AbilityButtonWidget(x - 8, y - 8, 16, 16, abilities.get(i), i, button -> {
                    int index = ((AbilityButtonWidget) button).getIndex();
                    this.handler.selected(index);
                    this.close();
                }));
            }
        }

        this.abilityButtonWidgets = stackButtons;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean buttonClicked = false;
        for (AbilityButtonWidget buttonWidget : abilityButtonWidgets) {
            if (buttonWidget.isSelected()) {
                buttonWidget.onClick(mouseX, mouseY);
                buttonClicked = true;
            }
        }
        if (!buttonClicked) {
            this.handler.selected(-1);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
