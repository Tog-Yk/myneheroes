package net.togyk.myneheroes.Item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.component.ModDataComponentTypes;

import java.util.ArrayList;
import java.util.List;

public class DyeableAdvancedArmorItem extends AdvancedArmorItem {
    private final List<Integer> defaultColors;

    public DyeableAdvancedArmorItem(List<Integer> defaultColors, RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
        this.defaultColors = defaultColors;
    }

    public Integer getColor(ItemStack stack, int index) {
        if (layerIsDyeable(index)) {
            List<Integer> colors = stack.get(ModDataComponentTypes.COLORS);
            if (colors != null && colors.size() > index) {
                return colors.get(index);
            } else {
                return this.defaultColors.get(index);
            }
        } else {
            return -1;
        }
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
    /*
    all the nbt and how they get saved
    shouldApplyHud: nbt.putBoolean("should_apply_hud", !shouldApplyHud);
     */

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
