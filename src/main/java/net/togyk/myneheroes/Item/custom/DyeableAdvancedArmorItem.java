package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.component.ModDataComponentTypes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DyeableAdvancedArmorItem extends AdvancedArmorItem implements DyeableItem, LightableItem {
    private final List<Integer> defaultColors;
    private final List<Integer> defaultLightLevels;

    public DyeableAdvancedArmorItem(List<Integer> defaultColors, List<Integer> lightLevels, Text titleText, RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(titleText, material, type, settings);
        this.defaultColors = defaultColors;
        this.defaultLightLevels = lightLevels;
    }

    public int getColor(ItemStack stack, int index) {
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

    @Override
    public List<Integer> getColors(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentTypes.COLORS, this.defaultColors);
    }

    public int getDefaultColor(int index) {
        if (layerIsDyeable(index) && this.defaultColors.size() > index) {
            return this.defaultColors.get(index);
        }
        return -1;
    }


    public void setDefaultColor(ItemStack stack, int index) {
        setColor(stack, index, getDefaultColor(index));
        setLayerIsDyed(stack, index, false);
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
        this.setLayerIsDyed(stack, index, true);

        //save to nbt
        stack.set(ModDataComponentTypes.COLORS, colors);
    }

    public void setLayerIsDyed(ItemStack stack, int index, boolean bool) {
        if (index >= defaultColors.size()) {
            return;
        }

        List<Boolean> colors = new ArrayList<>();

        //comparing the saved colors to the defaults to make sure all dyeable layers are accounted for
        for (int i = 0; i < defaultColors.size(); i++) {
            colors.add(layerIsDyed(stack, i)); // Convert each NbtInt to an integer
        }
        colors.set(index, bool);

        //save to nbt
        stack.set(ModDataComponentTypes.IS_DYED, colors);
    }

    public boolean layerIsDyeable(int index) {
        //getting the layer
        List<ArmorMaterial.Layer> layers = getMaterial().value().layers();
        ArmorMaterial.Layer layer = layers.get(index);

        return layer.isDyeable();
    }

    public boolean layerIsDyed(ItemStack stack, int index) {
        if (this.layerIsDyeable(index)) {
            List<Boolean> colors = stack.get(ModDataComponentTypes.IS_DYED);
            if (colors != null && colors.size() > index) {
                return colors.get(index);
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public List<Integer> getLightLevels(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentTypes.LIGHT_LEVEL, this.defaultLightLevels);
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

    public boolean layerIsLightable(int index) {
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.literal("Colors:").formatted(Formatting.GRAY));
        for (int i = 0; i < defaultColors.size(); i++) {
            if (layerIsDyeable(i)) {
                int color = getColor(stack, i);
                tooltip.add(Text.literal(" " + String.format("0x%06X", color)).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
            }
        }
    }
}
