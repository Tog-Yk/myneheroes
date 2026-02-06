package net.togyk.myneheroes.Item.custom;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.AttributeModifierAbility;
import net.togyk.myneheroes.ability.SimplePassiveAttributeModifierAbility;
import net.togyk.myneheroes.component.ModDataComponentTypes;
import net.togyk.myneheroes.networking.SaveUpgradePayload;
import net.togyk.myneheroes.upgrade.AbilityUpgrade;
import net.togyk.myneheroes.upgrade.Upgrade;
import net.togyk.myneheroes.util.AbilityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdvancedArmorItem extends ArmorItem implements AbilityHolding, UpgradableItem, EquipCallbackItem {
    private final Text titleText;

    public AdvancedArmorItem(Text titleText, RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
        this.titleText = titleText;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "heading"))).formatted(Formatting.GRAY));
        tooltip.add(titleText);

        super.appendTooltip(stack, context, tooltip, type);
    }

    public List<Ability> getArmorAbilities(ItemStack stack) {
        List<Ability> abilities = new ArrayList<>();

        for (Upgrade upgrade : this.getUpgrades(stack)) {
            if (upgrade instanceof AbilityUpgrade abilityUpgrade) {
                upgrade.setHolderStack(stack);
                for (Ability ability : abilityUpgrade.getAbilities()) {
                    ability.setHolder(upgrade);
                    abilities.add(ability);
                }
            }
        }

        return abilities;
    }

    @Override
    public void saveAbility(ItemStack stack, Ability ability) {
    }

    @Override
    public void saveUpgrade(ItemStack stack, Upgrade upgrade) {
        NbtCompound nbt = stack.getOrDefault(ModDataComponentTypes.UPGRADES, new NbtCompound());

        NbtList upgradesNbt = new NbtList();
        if (nbt.contains("upgrades")) {
            upgradesNbt = nbt.getList("upgrades", NbtElement.COMPOUND_TYPE);
        }

        //getting a list of all the id to replace the correct one
        List<Upgrade> upgrades = this.getUpgrades(stack);
        List<Identifier> identifiers = upgrades.stream().map(Upgrade::getId).toList();

        if (identifiers.contains(upgrade.getId())) {
            int index = identifiers.indexOf(upgrade.getId());
            if (upgradesNbt.size() > index) {
                upgradesNbt.set(index, upgrade.writeNbt(new NbtCompound()));
            } else {
                upgradesNbt.add(upgrade.writeNbt(new NbtCompound()));
            }
        } else {
            upgradesNbt.add(upgrade.writeNbt(new NbtCompound()));
        }


        nbt.put("upgrades", upgradesNbt);

        stack.set(ModDataComponentTypes.UPGRADES, nbt);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        stack.setHolder(entity);
    }

    @Override
    public boolean canUpgrade(ItemStack stack, Upgrade upgrade) {
        if (upgrade == null) return false;

        boolean hasUpgrade = hasUpgrade(stack, upgrade);
        boolean canBePutInSlot = upgrade.getCompatibleTypes().contains(this.getType());
        return !hasUpgrade && canBePutInSlot;
    }

    public boolean hasUpgrade(ItemStack stack, Upgrade upgrade) {
        return this.getUpgrades(stack).stream().map(Upgrade::getId).toList().contains(upgrade.getId());
    }

    @Override
    public List<Upgrade> getUpgrades(ItemStack stack) {
        NbtCompound nbt = stack.getOrDefault(ModDataComponentTypes.UPGRADES, new NbtCompound());

        NbtList upgradesNbt = new NbtList();
        if (nbt.contains("upgrades")) {
            upgradesNbt = nbt.getList("upgrades", NbtElement.COMPOUND_TYPE);
        }

        List<Upgrade> upgradeList = new ArrayList<>();
        for (NbtElement nbtElement : upgradesNbt) {
            if (nbtElement instanceof NbtCompound nbtCompound) {
                Upgrade upgrade = AbilityUtil.nbtToUpgrade(nbtCompound);
                if (upgrade != null) {
                    upgrade.setHolderStack(stack);
                    upgradeList.add(upgrade);
                }
            }
        }
        return upgradeList;
    }

    @Override
    public void setUpgrades(ItemStack stack, List<Upgrade> upgrades) {
        NbtCompound nbt = new NbtCompound();

        NbtList upgradesNbt = new NbtList();

        for (Upgrade upgrade : upgrades) {
            upgradesNbt.add(upgrade.writeNbt(new NbtCompound()));
        }

        nbt.put("upgrades", upgradesNbt);

        stack.set(ModDataComponentTypes.UPGRADES, nbt);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        for (Upgrade upgrade : this.getUpgrades(stack)) {
            if (upgrade.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference)) {
                //sync to server
                ClientPlayNetworking.send(new SaveUpgradePayload(slot.getIndex(), upgrade));
                return true;
            }
        }
        return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
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
