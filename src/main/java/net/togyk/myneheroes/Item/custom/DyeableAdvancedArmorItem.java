package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.togyk.myneheroes.component.ModDataComponentTypes;

import java.util.ArrayList;
import java.util.List;

public class DyeableAdvancedArmorItem extends AdvancedArmorItem {
    private final List<Integer> defaultColors;
    private final List<Integer> lightLevels;

    public DyeableAdvancedArmorItem(List<Integer> defaultColors, List<Integer> lightLevels, RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
        this.defaultColors = defaultColors;
        this.lightLevels = lightLevels;
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

    public int getLightLevel(int index) {
        if (lightLevels.size() > index) {
            return Math.max(-1, Math.min(15, lightLevels.get(index)));
        }
        return -1;
    }

    public boolean layerIsLightable(int index) {
        return getLightLevel(index) != -1;
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
