package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.AbilityUtil;
import net.togyk.myneheroes.ability.BooleanAbility;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AdvancedArmorItem extends ArmorItem {
    public final List<Ability> abilities;
    public AdvancedArmorItem(@Nullable Ability suitSpecificAbility,RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
        abilities = AbilityUtil.getStandardArmorAbilities(type);
        if (suitSpecificAbility != null) {
            abilities.add(suitSpecificAbility);
        }
    }

    public boolean ShouldApplyHud() {
        BooleanAbility ability = (BooleanAbility) AbilityUtil.getAbilityMatchingName(abilities,"toggle_hud");
        if (ability != null) {
            return ability.get();
        } else {
            return false;
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("hud:").formatted(Formatting.BLUE));
        if (ShouldApplyHud()) {
            tooltip.add(Text.literal(" active").formatted(Formatting.DARK_GREEN));
        } else {
            tooltip.add(Text.literal(" not active").formatted(Formatting.DARK_RED));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
}
