package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.togyk.myneheroes.component.ModDataComponentTypes;
import net.togyk.myneheroes.upgrade.Upgrade;
import net.togyk.myneheroes.util.AbilityUtil;

public interface UpgradeHolding {
    default void saveUpgrade(ItemStack stack, Upgrade upgrade) {
        NbtCompound nbt = new NbtCompound();
        nbt.put("upgrade", upgrade.writeNbt(new NbtCompound()));

        stack.set(ModDataComponentTypes.UPGRADES, nbt);
    }

    default Upgrade getUpgrade(ItemStack stack) {
        NbtCompound nbt = stack.getOrDefault(ModDataComponentTypes.UPGRADES, new NbtCompound());

        Upgrade upgrade = null;
        if (nbt.contains("upgrade")) {
            upgrade = AbilityUtil.nbtToUpgrade(nbt.getCompound("upgrade"));
            upgrade.setHolderStack(stack);
        }
        return upgrade;
    }
}
