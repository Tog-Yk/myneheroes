package net.togyk.myneheroes.Item.custom;

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

import java.util.ArrayList;
import java.util.List;

public class DyeableAdvancedArmorItem extends AdvancedArmorItem {
    private final List<Integer> defaultColors;

    public DyeableAdvancedArmorItem(List<Integer> defaultColors, RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
        this.defaultColors = defaultColors;
    }


    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        NbtCompound nbt = this.getNbt(stack);
        if (nbt != null && nbt.contains("hud_is_active")) {
            tooltip.add(Text.literal("hud:").formatted(Formatting.BLUE));
            if (nbt.getBoolean("hud_is_active")) {
                tooltip.add(Text.literal(" active").formatted(Formatting.DARK_GREEN));
            } else {
                tooltip.add(Text.literal(" not active").formatted(Formatting.DARK_RED));
            }
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    public Integer getColor(ItemStack stack, int index) {
        if (layerIsDyeable(index)) {
            NbtCompound nbt = getNbt(stack);
            if (nbt != null && nbt.contains("colors")) {
                NbtList nbtList = nbt.getList("colors", NbtElement.INT_TYPE);
                if (nbtList.size() >= index) {
                    return nbtList.getInt(index);
                }
            }
            return this.defaultColors.get(index);
        } else {
            return -1;
        }
    }
    public void setColor(ItemStack stack, int index, int color) {
        NbtCompound nbt = getNbt(stack);
        //read the colors
        List<Integer> colors = this.defaultColors;
        if (nbt.contains("colors", NbtElement.LIST_TYPE)) { // Check if the key exists and is a list
            NbtList nbtList = nbt.getList("colors", NbtElement.INT_TYPE); // Get the list as an INT type
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < nbtList.size(); i++) {
                colors.set(i, nbtList.getInt(i)); // Convert each NbtInt to an integer
            }
        }
        //set the color
        colors.set(index, color);

        //write the colors
        NbtList nbtList = new NbtList();
        for (int value : colors) {
            nbtList.add(NbtInt.of(value)); // Add each integer as an IntTag
        }

        setNbt(stack, nbt);
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
}
