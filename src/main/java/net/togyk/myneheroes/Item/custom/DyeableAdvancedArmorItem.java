package net.togyk.myneheroes.Item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.entry.RegistryEntry;
import net.togyk.myneheroes.MyneHeroes;

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
            NbtCompound nbt = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).getNbt();
            NbtCompound modnbt = new NbtCompound();
            if (nbt.contains(MyneHeroes.MOD_ID)) {
                modnbt = nbt.getCompound(MyneHeroes.MOD_ID);
            }
            if (modnbt != null && modnbt.contains("colors")) {
                NbtList nbtList = modnbt.getList("colors", NbtElement.INT_TYPE);
                if (nbtList.size() >= index) {
                    return nbtList.getInt(index);
                }
            }
            return this.defaultColors.get(index);
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
        NbtCompound nbt = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).getNbt();
        NbtCompound modnbt = new NbtCompound();
        if (nbt.contains(MyneHeroes.MOD_ID)) {
            modnbt = nbt.getCompound(MyneHeroes.MOD_ID);
        }
        NbtList nbtList = new NbtList();
        for (Integer integer : colors) {
            nbtList.add(NbtInt.of(integer));
        }
        modnbt.put("colors", nbtList);
        nbt.put(MyneHeroes.MOD_ID, modnbt);
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
