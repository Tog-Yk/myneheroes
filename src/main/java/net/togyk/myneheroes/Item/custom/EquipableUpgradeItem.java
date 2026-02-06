package net.togyk.myneheroes.Item.custom;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.AttributeModifierAbility;
import net.togyk.myneheroes.ability.SimplePassiveAttributeModifierAbility;
import net.togyk.myneheroes.upgrade.AbilityUpgrade;
import net.togyk.myneheroes.upgrade.Upgrade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EquipableUpgradeItem extends UpgradeItem implements Equipment, AbilityHolding, EquipCallbackItem {
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

        Upgrade upgrade = this.getUpgrade(stack, null);
        if (upgrade instanceof AbilityUpgrade abilityUpgrade) {
            for (Ability ability : abilityUpgrade.getAbilities()) {
                ability.setHolder(upgrade);
                abilities.add(ability);
            }
        }

        return abilities;
    }

    @Override
    public List<Ability> getAccessoriesAbilities(ItemStack stack) {
        List<Ability> abilities = new ArrayList<>();

        Upgrade upgrade = this.getUpgrade(stack, null);
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

    @Override
    public void onEquipped(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
        AttributeContainer attributeContainer = entity.getAttributes();
        List<SimplePassiveAttributeModifierAbility> attributeModifierAbilities = this.getArmorAbilities(stack).stream().filter(ability -> ability instanceof SimplePassiveAttributeModifierAbility).map(SimplePassiveAttributeModifierAbility.class::cast).toList();

        for (SimplePassiveAttributeModifierAbility ability : attributeModifierAbilities) {
            for(Map.Entry<RegistryEntry<EntityAttribute>, AttributeModifierAbility.AbilityAttributeModifierCreator> entry : ability.getAttributeModifiers().modifiers.entrySet()) {
                EntityAttributeInstance entityAttributeInstance = attributeContainer.getCustomInstance(entry.getKey());
                if (entityAttributeInstance != null) {
                    entityAttributeInstance.removeModifier((entry.getValue()).id());
                    entityAttributeInstance.addPersistentModifier((entry.getValue()).createAttributeModifier());
                }
            }
        }
    }

    @Override
    public void onUnequipped(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
        AttributeContainer attributeContainer = entity.getAttributes();
        List<SimplePassiveAttributeModifierAbility> attributeModifierAbilities = this.getArmorAbilities(stack).stream().filter(ability -> ability instanceof SimplePassiveAttributeModifierAbility).map(SimplePassiveAttributeModifierAbility.class::cast).toList();

        for (SimplePassiveAttributeModifierAbility ability : attributeModifierAbilities) {
            for(Map.Entry<RegistryEntry<EntityAttribute>, AttributeModifierAbility.AbilityAttributeModifierCreator> entry : ability.getAttributeModifiers().modifiers.entrySet()) {
                EntityAttributeInstance entityAttributeInstance = attributeContainer.getCustomInstance(entry.getKey());
                if (entityAttributeInstance != null) {
                    entityAttributeInstance.removeModifier((entry.getValue()).id());
                }
            }
        }
    }
}
