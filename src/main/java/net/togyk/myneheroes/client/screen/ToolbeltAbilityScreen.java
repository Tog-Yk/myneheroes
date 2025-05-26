package net.togyk.myneheroes.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.togyk.myneheroes.client.screen.handeler.ToolbeltAbilityScreenHandler;
import net.togyk.myneheroes.client.screen.widget.StackButtonWidget;

import java.util.ArrayList;
import java.util.List;

public class ToolbeltAbilityScreen extends HandledScreen<ToolbeltAbilityScreenHandler> {
    private List<StackButtonWidget> stackButtonWidgets = new ArrayList<>();
    private final World world;

    public ToolbeltAbilityScreen(ToolbeltAbilityScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        world = inventory.player.getWorld();
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
        for (StackButtonWidget button : this.stackButtonWidgets) {
            button.render(context, mouseX, mouseY, delta);
        }
    }

    public void updateAbilityButtons(int width, int height) {
        List<StackButtonWidget> stackButtons = new ArrayList<>();

        List<ItemStack> abilities = this.handler.getAbility().getInventory(world);
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

                stackButtons.add(new StackButtonWidget(x - 8, y - 8, 16, 16, abilities.get(i), i, button -> {
                    int index = ((StackButtonWidget) button).getIndex();
                    this.handler.selected(index);
                    this.close();
                }));
            }
        }

        this.stackButtonWidgets = stackButtons;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (StackButtonWidget buttonWidget : stackButtonWidgets) {
            if (buttonWidget.isSelected()) {
                buttonWidget.onClick(mouseX, mouseY);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
