package net.togyk.myneheroes.block.screen.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.block.screen.client.widget.LightLevelSliderWidget;
import net.togyk.myneheroes.block.screen.handeler.ArmorLightLevelerScreenHandler;

import java.util.List;

public class ArmorLightLevelerScreen extends HandledScreen<ArmorLightLevelerScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(MyneHeroes.MOD_ID, "textures/gui/container/armor_light_leveler.png");

    private static final Identifier OPTION_SELECTED_TEXTURE = Identifier.of(MyneHeroes.MOD_ID, "textures/gui/sprite/container/armor_light_leveler/option_selected.png");
    private static final Identifier OPTION_HIGHLIGHTED_TEXTURE = Identifier.of(MyneHeroes.MOD_ID, "textures/gui/sprite/container/armor_light_leveler/option_highlighted.png");
    private static final Identifier OPTION_TEXTURE = Identifier.of(MyneHeroes.MOD_ID, "textures/gui/sprite/container/armor_light_leveler/option.png");

    private static final Identifier SCROLLER_TEXTURE = Identifier.of(MyneHeroes.MOD_ID, "textures/gui/sprite/container/armor_light_leveler/scroller.png");
    private static final Identifier SCROLLER_DISABLED_TEXTURE = Identifier.of(MyneHeroes.MOD_ID, "textures/gui/sprite/container/armor_light_leveler/scroller_disabled.png");

    private float scrollAmount;
    private boolean mouseClicked;
    private int scrollOffset;

    LightLevelSliderWidget sliderWidget;

    ButtonWidget setButton;
    ButtonWidget setDefaultButton;

    @Override
    protected void init() {
        super.init();

        this.sliderWidget = new LightLevelSliderWidget("Light Level", this.x + 96, this.y + 26, 64, 14, 0);
        addDrawableChild(sliderWidget);

        this.setButton = ButtonWidget.builder(Text.of("set"), button -> {
                    if (this.handler.canLevel()) {
                        this.handler.level(getSelectedLightLevel());
                    }
                })
                .dimensions(this.x + 96, this.y + 42, 31, 14)
                .build();
        addDrawableChild(setButton);
        this.setDefaultButton = ButtonWidget.builder(Text.of("default"), button -> {
                    if (this.handler.canLevel()) {
                        this.handler.levelDefault();
                    }
                })
                .dimensions(this.x + 129, this.y + 42, 31, 14)
                .build();
        addDrawableChild(setDefaultButton);
    }

    public ArmorLightLevelerScreen(ArmorLightLevelerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        handler.setContentsChangedListener(this::onInventoryChange);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = this.x;
        int j = this.y;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);

        int k = (int)(31.0F * this.scrollAmount);
        Identifier identifier = this.shouldScroll() ? SCROLLER_TEXTURE : SCROLLER_DISABLED_TEXTURE;
        context.drawTexture(identifier, i + 68, j + 17 + k, 0, 0, 12, 17, 12, 17);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);

        this.handler.updateOptions();

        int l = this.x + 48;
        int m = this.y + 17;
        int n = this.scrollOffset + 12;
        renderOptionsBackground(context, mouseX, mouseY, l, m, n);
        if (!handler.getOptions().isEmpty()) {
            renderOptionNumbers(context, l + 8, m + 2, n);
        }
    }

    private void renderOptionsBackground(DrawContext context, int mouseX, int mouseY, int x, int y, int scrollOffset) {
        for (int i = this.scrollOffset; i < scrollOffset && i < this.handler.getOptions().size() && i - this.scrollOffset < 3; i++) {
            int j = i - this.scrollOffset;
            int m = y + j * 16;

            Identifier identifier;
            if (i == this.handler.getSelectedOption()) {
                identifier = OPTION_SELECTED_TEXTURE;
            } else if (mouseX >= x && mouseY >= m && mouseX < x + 16 && mouseY < m + 16) {
                identifier = OPTION_HIGHLIGHTED_TEXTURE;
            } else {
                identifier = OPTION_TEXTURE;
            }

            context.drawTexture(identifier, x, m, 0, 0, 16, 16, 16, 16);
        }
    }

    private void renderOptionNumbers(DrawContext context, int x, int y, int scrollOffset) {
        List<Integer> options = this.handler.getOptions();

        for (int i = this.scrollOffset; i < scrollOffset && i < this.handler.getOptions().size() && i - this.scrollOffset < 3; i++) {
            int j = i - this.scrollOffset;
            int m = y + j * 16 + 2;

            int color;
            if (i == this.handler.getSelectedOption()) {
                color = 0x888888;
            } else {
                color = 0xFFFFFF;
            }
            context.drawCenteredTextWithShadow(this.textRenderer, Text.literal(String.valueOf(options.get(i) + 1)), x, m, color);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
        if (this.handler.canSelect()) {
            int i = this.x + 48;
            int j = this.y + 17;
            int k = this.scrollOffset + 3;

            //option buttons
            for (int l = this.scrollOffset; l < k; l++) {
                int m = l - this.scrollOffset;
                double d = mouseX - (double) i;
                double e = mouseY - (double)(j + m * 16);
                if (d >= 0.0 && e >= 0.0 && d < 16.0 && e < 16.0 && this.handler.onButtonClick(this.client.player, l)) {
                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    this.client.interactionManager.clickButton(this.handler.syncId, l);
                    this.updateSlider();
                    return true;
                }
            }

            //slider
            i = this.x + 68;
            j = this.y + 17;
            if (mouseX >= (double)i && mouseX < (double)(i + 12) && mouseY >= (double)j && mouseY < (double)(j + 48)) {
                MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                this.mouseClicked = true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.mouseClicked && this.shouldScroll()) {
            int i = this.y + 17;
            int j = i + 48;
            this.scrollAmount = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.scrollAmount = MathHelper.clamp(this.scrollAmount, 0.0F, 1.0F);
            this.scrollOffset = (int)((double)(this.scrollAmount * (float)this.getMaxScroll()) + 0.5) * 3;
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.shouldScroll()) {
            int i = this.getMaxScroll();
            float f = (float)verticalAmount / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0F, 1.0F);
            this.scrollOffset = (int)((double)(this.scrollAmount * (float)i) + 0.5) * 3;
        }

        return true;
    }

    private boolean shouldScroll() {
        return this.handler.getOptions().size() > 3;
    }

    protected int getMaxScroll() {
        return (this.handler.getOptions().size() / 3);
    }

    private int getSelectedLightLevel() {
        return sliderWidget.getLightLevel();
    }

    private void onInventoryChange() {
        if (!this.handler.canLevel()) {
            this.scrollAmount = 0.0F;
            this.scrollOffset = 0;
            this.handler.setSelectedOption(0);

            this.sliderWidget.visible = false;
            this.sliderWidget.active = false;

            this.setButton.visible = false;
            this.setButton.active = false;
            this.setDefaultButton.visible = false;
            this.setDefaultButton.active = false;
        } else {

            this.sliderWidget.visible = true;
            this.sliderWidget.active = true;

            this.setButton.visible = true;
            this.setButton.active = true;
            this.setDefaultButton.visible = true;
            this.setDefaultButton.active = true;
        }
    }

    private void updateSlider() {
        this.sliderWidget.setLightLevel(this.handler.getLightlevel());
    }
}
