package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.AbilityUtil;
import net.togyk.myneheroes.ability.BooleanAbility;
import net.togyk.myneheroes.component.ModDataComponentTypes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdvancedArmorItem extends ArmorItem {
    private final List<Ability> abilities;
    public AdvancedArmorItem(@Nullable Ability suitSpecificAbility,RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
        abilities = AbilityUtil.getStandardArmorAbilities(type);
        if (suitSpecificAbility != null) {
            abilities.add(suitSpecificAbility);
        }
    }

    public boolean ShouldApplyHud(ItemStack stack) {
        if (this.getAbilities(stack) != null) {
            Ability ability = AbilityUtil.getAbilityMatchingName(this.getAbilities(stack), "toggle_hud");
            if (ability instanceof BooleanAbility booleanAbility) {
                return booleanAbility.get();
            }
        }
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("hud:").formatted(Formatting.BLUE));
        if (ShouldApplyHud(stack)) {
            tooltip.add(Text.literal(" active").formatted(Formatting.DARK_GREEN));
        } else {
            tooltip.add(Text.literal(" not active").formatted(Formatting.DARK_RED));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    public List<Identifier> getUnlockedAbilities(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentTypes.UPGRADES, new ArrayList<>());
    }

    public boolean UnlockAbility(ItemStack stack, Identifier id) {
        List<Identifier> unlockedAbilities = this.getUnlockedAbilities(stack);
        List<Identifier> abilitiesIds = this.abilities.stream().map(Ability::getId).toList();
        if (abilitiesIds.contains(id)) {
            unlockedAbilities.add(id);
            //save to nbt
            stack.set(ModDataComponentTypes.UPGRADES, unlockedAbilities);
            return true;
        } else {
            return false;
        }
    }

    public List<Ability> getAbilities(ItemStack stack) {
        List<Ability> abilityList = new ArrayList<>();
        List<Identifier> UnlockedAbilities = this.getUnlockedAbilities(stack);
        if (this.abilities != null) {
            for (Ability ability : this.abilities) {
                if (ability != null && UnlockedAbilities.contains(ability.getId())) abilityList.add(ability);
            }
        }
        return abilityList;
    }

    //any use of these abilities won't be saved on the items in theory
    public List<Ability> getNotUnlockedAbilities(ItemStack stack) {
        List<Ability> abilityList = List.copyOf(this.abilities);
        List<Identifier> UnlockedAbilities = this.getUnlockedAbilities(stack);
        for (Ability ability: this.abilities) {
            if (UnlockedAbilities.contains(ability.getId())) {
                abilityList.remove(ability);
            }
        }
        return abilityList;
    }
}
