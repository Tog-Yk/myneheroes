package net.togyk.myneheroes.upgrade;

import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;

import java.util.List;

public class SimpleUpgrade extends Upgrade {
    protected SimpleUpgrade(List<ArmorItem.Type> compatibleTypes, Identifier id) {
        super(compatibleTypes, id);
    }

    @Override
    public Upgrade copy() {
        return new SimpleUpgrade(compatibleTypes, id);
    }
}
