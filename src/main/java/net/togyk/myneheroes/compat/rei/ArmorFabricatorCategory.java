package net.togyk.myneheroes.compat.rei;

import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.block.ModBlocks;

import java.util.List;

public class ArmorFabricatorCategory implements DisplayCategory<BasicDisplay> {
    public static final CategoryIdentifier<ArmorFabricatorDisplay> ARMOR_FABRICATOR =
            CategoryIdentifier.of(MyneHeroes.MOD_ID, "armor_fabricator");

    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return ARMOR_FABRICATOR;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("block.myneheroes.armor_fabricator");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModBlocks.ARMOR_FABRICATOR.asItem().getDefaultStack());
    }

    // Done with the help:
    // https://github.com/TeamGalacticraft/Galacticraft/tree/main (MIT License)
    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4)));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5))
                .entries(display.getOutputEntries().get(0))
                .disableBackground()
                .markOutput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5))
                .entries(display.getInputEntries().get(0)).markInput().disableBackground());
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 36;
    }
}