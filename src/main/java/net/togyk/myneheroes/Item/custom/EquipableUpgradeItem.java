package net.togyk.myneheroes.Item.custom;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.upgrade.AbilityUpgrade;
import net.togyk.myneheroes.upgrade.Upgrade;

import java.util.ArrayList;
import java.util.List;

public class EquipableUpgradeItem extends UpgradeItem implements Equipment, AbilityHolding {
    public final EquipmentSlot slot;

    public EquipableUpgradeItem(Upgrade upgrade, EquipmentSlot slot, Settings settings) {
        this(upgrade, slot, null, settings);
    }

    public EquipableUpgradeItem(Upgrade upgrade, EquipmentSlot slot, Text tooltip, Settings settings) {
        super(upgrade, tooltip, settings);
        this.slot = slot;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return this.equipAndSwap(this, world, user, hand);
    }

    @Override
    public EquipmentSlot getSlotType() {
        return this.slot;
    }

    @Override
    public List<Ability> getArmorAbilities(ItemStack stack) {
        List<Ability> abilities = new ArrayList<>();

        Upgrade upgrade = this.getUpgrade(stack);
        if (upgrade instanceof AbilityUpgrade abilityUpgrade) {
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
}
