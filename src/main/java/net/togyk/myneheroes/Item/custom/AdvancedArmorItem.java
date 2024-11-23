package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.entry.RegistryEntry;

public class AdvancedArmorItem extends ArmorItem {
    public AdvancedArmorItem(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
    }

    public boolean ShouldApplyHud() {
        return true;
    }
}
