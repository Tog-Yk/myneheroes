package net.togyk.myneheroes.client.screen.widget;

import me.shedaniel.math.Point;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ReiEntityWidget extends Widget {
    public final EntityType<?> type;
    public final Point point;
    public final int width;
    public final int height;

    public ReiEntityWidget(EntityType<?> type, Point point, int width, int height) {
        this.type = type;
        this.point = point;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Entity entity = type.create(MinecraftClient.getInstance().world);
        if (entity instanceof LivingEntity livingEntity) {
            int scale = (int) (45f / Math.max(entity.getWidth() * 2f, entity.getHeight()));

            InventoryScreen.drawEntity(
                    context, point.x - width / 2, point.y - height / 2, point.x + width / 2, point.y + height / 2, scale, 0f,
                    mouseX, mouseY,
                    livingEntity
            );
        }
    }

    @Override
    public List<? extends Element> children() {
        return List.of();
    }
}
