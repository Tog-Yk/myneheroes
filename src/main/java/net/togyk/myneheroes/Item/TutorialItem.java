package net.togyk.myneheroes.Item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.AbilityHolding;
import net.togyk.myneheroes.Item.custom.EquipCallbackItem;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.AttributeModifierAbility;
import net.togyk.myneheroes.ability.HudAbility;
import net.togyk.myneheroes.ability.SimplePassiveAttributeModifierAbility;
import net.togyk.myneheroes.component.ModDataComponentTypes;
import net.togyk.myneheroes.util.AbilityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TutorialItem extends Item implements Equipment, AbilityHolding, EquipCallbackItem {
    public HudAbility defaultHudAbility;

    public TutorialItem(Settings settings, HudAbility hudAbility) {
        super(settings);
        this.defaultHudAbility = hudAbility;
    }

    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.HEAD;
    }

    /**
     * @param stack
     * @return
     */
    @Override
    public List<Ability> getArmorAbilities(ItemStack stack) {
        defaultHudAbility.setHolder(stack);
        NbtCompound nbt = stack.getOrDefault(ModDataComponentTypes.ABILITIES, getDefaultNBT());

        NbtList abilitiesNbt = new NbtList();

        if (nbt.contains("abilities")) {
            abilitiesNbt = nbt.getList("abilities", NbtElement.COMPOUND_TYPE);
        }

        List<Ability> abilitiesList = new ArrayList<>();
        for (NbtElement nbtElement : abilitiesNbt) {
            if (nbtElement instanceof NbtCompound nbtCompound) {
                Ability ability = AbilityUtil.nbtToAbility(nbtCompound);
                abilitiesList.add(ability);
            }
        }
        return abilitiesList;
        //return List.of(this.defaultHudAbility);
    }

    private NbtCompound getDefaultNBT () {
        NbtCompound defaultNBT = new NbtCompound();
        NbtList abilitiesNbt = new NbtList();
        abilitiesNbt.add(defaultHudAbility.writeNbt(new NbtCompound()));
        defaultNBT.put("abilities", abilitiesNbt);
        return defaultNBT;
    }

    /**
     * @param stack
     * @param ability
     */
    @Override
    public void saveAbility(ItemStack stack, Ability ability) {
        if (ability instanceof HudAbility hudAbility) {
            this.defaultHudAbility = hudAbility;
            NbtCompound nbt = stack.getOrDefault(ModDataComponentTypes.ABILITIES, new NbtCompound());

            NbtList abilitiesNbt = new NbtList();
            if (nbt.contains("abilities")) {
                abilitiesNbt = nbt.getList("abilities", NbtElement.COMPOUND_TYPE);
            }

            //getting a list of all the id to replace the correct one
            List<Ability> loadedAbilities = this.getArmorAbilities(stack);
            List<Identifier> identifiers = loadedAbilities.stream().map(Ability::getId).toList();

            if (identifiers.contains(ability.getId())) {
                int index = identifiers.indexOf(ability.getId());
                if (abilitiesNbt.size() > index) {
                    abilitiesNbt.set(index, ability.writeNbt(new NbtCompound()));
                } else {
                    abilitiesNbt.add(ability.writeNbt(new NbtCompound()));
                }
            } else {
                abilitiesNbt.add(ability.writeNbt(new NbtCompound()));
            }

            nbt.put("abilities", abilitiesNbt);

            stack.set(ModDataComponentTypes.ABILITIES, nbt);
        }
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
