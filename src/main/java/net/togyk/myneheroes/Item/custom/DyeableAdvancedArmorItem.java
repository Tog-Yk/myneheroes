package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.component.ModDataComponentTypes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DyeableAdvancedArmorItem extends AdvancedArmorItem {
    private final List<Integer> defaultColors;
    private final List<Integer> defaultLightLevels;

    public DyeableAdvancedArmorItem(List<Integer> defaultColors, List<Integer> lightLevels, @Nullable Ability suitSpecificAbility, RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(suitSpecificAbility, material, type, settings);
        this.defaultColors = defaultColors;
        this.defaultLightLevels = lightLevels;
    }

    public Integer getColor(ItemStack stack, int index) {
        if (layerIsDyeable(index)) {
            List<Integer> colors = stack.get(ModDataComponentTypes.COLORS);
            if (colors != null && colors.size() > index) {
                return colors.get(index);
            } else if (this.defaultColors.size() > index){
                return this.defaultColors.get(index);
            }
        }
        return -1;
    }

    public int getDefaultColor(int index) {
        if (layerIsDyeable(index) && this.defaultColors.size() > index) {
            return this.defaultColors.get(index);
        }
        return -1;
    }

    public void setColor(ItemStack stack, int index, Integer color) {
        if (index >= defaultColors.size()) {
            return;
        }

        List<Integer> colors = new ArrayList<>();

        //comparing the saved colors to the defaults to make sure all dyeable layers are accounted for
        for (int i = 0; i < defaultColors.size(); i++) {
            colors.add(getColor(stack, i)); // Convert each NbtInt to an integer
        }
        colors.set(index, color);

        //save to nbt
        stack.set(ModDataComponentTypes.COLORS, colors);
    }

    public boolean layerIsDyeable(int index) {
        //getting the layer
        List<ArmorMaterial.Layer> layers = getMaterial().value().layers();
        ArmorMaterial.Layer layer = layers.get(index);

        return layer.isDyeable();
    }

    public int getLightLevel(ItemStack stack, int index) {
        if (layerIsDyeable(index)) {
            List<Integer> colors = stack.get(ModDataComponentTypes.LIGHT_LEVEL);
            if (colors != null && colors.size() > index) {
                return colors.get(index);
            } else if (this.defaultLightLevels.size() > index){
                return this.defaultLightLevels.get(index);
            }
        }
        return -1;
    }

    public int getDefaultLightLevel(int index) {
        if (layerIsDyeable(index) && defaultLightLevels.size() > index) {
            return Math.max(-1, Math.min(15, defaultLightLevels.get(index)));
        }
        return -1;
    }

    public void setLightLevel(ItemStack stack, int index, int lightLevel) {
        if (index >= defaultLightLevels.size()) {
            return;
        }

        List<Integer> colors = new ArrayList<>();

        //comparing the saved colors to the defaults to make sure all dyeable layers are accounted for
        for (int i = 0; i < defaultLightLevels.size(); i++) {
            colors.add(getLightLevel(stack, i)); // Convert each NbtInt to an integer
        }
        colors.set(index, lightLevel);

        //save to nbt
        stack.set(ModDataComponentTypes.LIGHT_LEVEL, colors);
    }

    public boolean layerIsLightable(ItemStack stack, int index) {
        return getLightLevel(stack, index) >= 0;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Colors:").formatted(Formatting.BLUE));
        for (int i = 0; i < defaultColors.size(); i++) {
            if (layerIsDyeable(i)) {
                tooltip.add(Text.literal(" " + String.valueOf(-1 * getColor(stack, i))));
            }
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
}
