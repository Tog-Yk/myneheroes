package net.togyk.myneheroes.compat.rei;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.client.screen.widget.ReiEntityWidget;

import java.util.LinkedList;
import java.util.List;

public class EntityInteractionCategory implements DisplayCategory<EntityInteractionDisplay> {
    private static final Identifier TEXTURE = Identifier.of(MyneHeroes.MOD_ID, "textures/gui/container/rei/entity_interaction.png");
    public static final CategoryIdentifier<EntityInteractionDisplay> ENTITY_INTERACTION =
            CategoryIdentifier.of(MyneHeroes.MOD_ID, "entity_interaction");

    @Override
    public CategoryIdentifier<? extends EntityInteractionDisplay> getCategoryIdentifier() {
        return ENTITY_INTERACTION;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("recipe_category.myneheroes.entity_interaction");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Items.MILK_BUCKET.getDefaultStack());
    }

    // Done with the help:
    // https://github.com/TeamGalacticraft/Galacticraft/tree/main (MIT License)
    @Override
    public List<Widget> setupDisplay(EntityInteractionDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 87, bounds.getCenterY() - 35);
        List<Widget> widgets = new LinkedList<>();

        widgets.add(Widgets.createTexturedWidget(TEXTURE, startPoint.x, startPoint.y, 0, 0, 176, 84, 176, 84));

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 16, startPoint.y + 33))
                .entries(display.getInputEntries().get(0)).markInput().disableBackground());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 143, startPoint.y + 33))
                .entries(display.getOutputEntries().get(0)).markOutput().disableBackground());


        //entity widget
        EntityType<?> type = display.getEntityType();

        widgets.add(new ReiEntityWidget(type, new Point(startPoint.x + 79, startPoint.y + 42), 54, 54));

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 95;
    }
}