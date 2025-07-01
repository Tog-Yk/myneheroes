package net.togyk.myneheroes.ability;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.client.HudType;
import net.togyk.myneheroes.util.HudActionResult;

import java.util.List;

public class HudAbility extends BooleanAbility {
    private final HudType type;

    public HudAbility(Identifier id, Ability.Settings settings, HudType type) {
        super(id, settings);
        this.type = type;
    }

    public HudType getType() {
        return type;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (this.get()) {
            tooltip.add(Text.translatable("tooltip.myneheroes.hud_active").formatted(Formatting.BLUE));
        } else {
            tooltip.add(Text.translatable("tooltip.myneheroes.hud_inactive").formatted(Formatting.BLUE));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public HudAbility copy() {
        return new HudAbility(this.id, settings, type);
    }
}
