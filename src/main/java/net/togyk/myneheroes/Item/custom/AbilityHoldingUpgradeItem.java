package net.togyk.myneheroes.Item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.component.ModDataComponentTypes;
import net.togyk.myneheroes.upgrade.AbilityUpgrade;
import net.togyk.myneheroes.upgrade.Upgrade;
import net.togyk.myneheroes.util.AbilityUtil;

import java.util.ArrayList;
import java.util.List;

public class AbilityHoldingUpgradeItem extends UpgradeItem implements AbilityHolding {
    public AbilityHoldingUpgradeItem(Upgrade upgrade, Settings settings) {
        super(upgrade, settings);
    }

    public void saveUpgrade(ItemStack stack, AbilityUpgrade abilityUpgrade) {
        NbtCompound nbt = new NbtCompound();
        nbt.put("upgrade", abilityUpgrade.writeNbt(new NbtCompound()));

        stack.set(ModDataComponentTypes.UPGRADES, nbt);
    }

    @Override
    public List<Ability> getAbilities(ItemStack stack) {
        List<Ability> abilities = new ArrayList<>();

        Upgrade upgrade = this.getUpgrade(stack);
        if (upgrade instanceof AbilityUpgrade abilityUpgrade) {
            upgrade.setHolderStack(stack);
            for (Ability ability : abilityUpgrade.getAbilities()) {
                ability.setHolder(upgrade);
                abilities.add(ability);
            }
        }

        return abilities;
    }

    @Override
    public void saveAbility(ItemStack stack, Ability ability) {
    }

    public Upgrade getUpgrade(ItemStack stack) {
        NbtCompound nbt = stack.getOrDefault(ModDataComponentTypes.UPGRADES, new NbtCompound());

        Upgrade upgrade = getUpgrade();
        if (nbt.contains("upgrade")) {
            upgrade = AbilityUtil.nbtToUpgrade(nbt.getCompound("upgrade"));
        }
        return upgrade;
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (this.getUpgrade().onClicked(stack, otherStack, slot, clickType, player, cursorStackReference)) {
            return true;
        }
        return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
    }
}
