package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.List;

public class ModSmithingTemplateItem extends Item {
    private static final Text APPLIES_TO_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.ofVanilla("smithing_template.applies_to")))
            .formatted(Formatting.GRAY);
    private static final Text INGREDIENTS_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.ofVanilla("smithing_template.ingredients")))
            .formatted(Formatting.GRAY);
    private final Text titleText;
    private final Text appliesToText;
    private final Text ingredientsText;

    public ModSmithingTemplateItem(boolean isTrimPattern, Identifier templateName, Settings settings) {
        super(settings);
        if (isTrimPattern) {
            titleText = Text.translatable(Util.createTranslationKey("trim_pattern", templateName)).formatted(Formatting.GRAY);
            appliesToText = Text.translatable(
                            Util.createTranslationKey("item", Identifier.ofVanilla("smithing_template.armor_trim.applies_to"))
                    ).formatted(Formatting.BLUE);
            ingredientsText = Text.translatable(
                            Util.createTranslationKey("item", Identifier.ofVanilla("smithing_template.armor_trim.ingredients"))
                    )
                    .formatted(Formatting.BLUE);
        } else {
            titleText = Text.translatable(Util.createTranslationKey("upgrade", templateName)).formatted(Formatting.GRAY);
            appliesToText = Text.translatable(Util.createTranslationKey(
                    "item", Identifier.of(templateName.getNamespace(), "smithing_template." + templateName.getPath() + ".applies_to")
            )).formatted(Formatting.BLUE);
            ingredientsText = Text.translatable(Util.createTranslationKey(
                    "item", Identifier.of(templateName.getNamespace(), "smithing_template." + templateName.getPath() + ".ingredients")
            )).formatted(Formatting.BLUE);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(this.titleText);
        tooltip.add(ScreenTexts.EMPTY);
        tooltip.add(APPLIES_TO_TEXT);
        tooltip.add(ScreenTexts.space().append(this.appliesToText));
        tooltip.add(INGREDIENTS_TEXT);
        tooltip.add(ScreenTexts.space().append(this.ingredientsText));
    }
}
