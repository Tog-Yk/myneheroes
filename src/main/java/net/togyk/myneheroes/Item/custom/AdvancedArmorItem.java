package net.togyk.myneheroes.Item.custom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.component.ModDataComponentTypes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdvancedArmorItem extends ArmorItem implements AbilityHolding, UpgradableItem {
    public AdvancedArmorItem(@Nullable Ability suitSpecificAbility, RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
    }
    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        MinecraftClient client = MinecraftClient.getInstance();
        for (Ability ability: this.getAbilities(stack, client.world)) {
            ability.appendTooltip(stack, context, tooltip, type);
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    public List<Ability> getAbilities(ItemStack stack, @Nullable World world) {
        List<Ability> abilities = new ArrayList<>();
        if (world == null) {
            return abilities;
        }

        for (ItemStack upgradeStack : this.getUpgrades(stack, world)) {
            if (upgradeStack.getItem() instanceof UpgradeItem upgrade) {
                for (Ability ability : upgrade.getAbilities(upgradeStack)) {
                    ability.setHolder(upgradeStack);
                    abilities.add(ability);
                }
                upgrade.setHolder(stack);
            }
        }

        return abilities;
    }

    @Override
    public void saveAbility(ItemStack stack, World world, Ability ability) {
        NbtCompound nbt = stack.getOrDefault(ModDataComponentTypes.ABILITIES, new NbtCompound());

        NbtList abilitiesNbt = new NbtList();
        if (nbt.contains("abilities")) {
            abilitiesNbt = nbt.getList("abilities", NbtElement.COMPOUND_TYPE);
        }

        //getting a list of all the id to replace the correct one
        List<Ability> loadedAbilities = this.getAbilities(stack, world);
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

    @Override
    public void saveUpgrade(ItemStack stack, ItemStack upgrade, World world) {
        NbtCompound nbt = stack.getOrDefault(ModDataComponentTypes.UPGRADES, new NbtCompound());

        NbtList abilitiesNbt = new NbtList();
        if (nbt.contains("upgrades")) {
            abilitiesNbt = nbt.getList("upgrades", NbtElement.COMPOUND_TYPE);
        }

        //getting a list of all the id to replace the correct one
        List<ItemStack> upgrades = this.getUpgrades(stack, world);
        List<Item> upgradeItems = upgrades.stream().map(ItemStack::getItem).toList();

        if (upgradeItems.contains(upgrade.getItem())) {
            int index = upgradeItems.indexOf(upgrade.getItem());
            if (abilitiesNbt.size() > index) {
                abilitiesNbt.set(index, upgrade.encodeAllowEmpty(world.getRegistryManager()));
            } else {
                abilitiesNbt.add(upgrade.encodeAllowEmpty(world.getRegistryManager()));
            }
        } else {
            abilitiesNbt.add(upgrade.encodeAllowEmpty(world.getRegistryManager()));
        }

        nbt.put("upgrades", abilitiesNbt);

        stack.set(ModDataComponentTypes.UPGRADES, nbt);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        stack.setHolder(entity);
    }

    @Override
    public boolean canBeUpgraded() {
        return true;
    }

    @Override
    public boolean canUpgrade(ItemStack stack, ItemStack upgradeStack, World world) {
        if (upgradeStack.getItem() instanceof UpgradeItem upgrade) {
            boolean hasUpgrade = hasUpgrade(stack, upgrade, world);
            boolean canBePutInSlot = upgrade.getEquipmentSlot() == null || upgrade.getEquipmentSlot() == this.getType();
            return !hasUpgrade && canBePutInSlot;
        }
        return false;
    }

    public boolean hasUpgrade(ItemStack stack, UpgradeItem upgrade, World world) {
        return this.getUpgrades(stack, world).stream().map(ItemStack::getItem).toList().contains(upgrade);
    }

    @Override
    public List<ItemStack> getUpgrades(ItemStack stack, World world) {
        NbtCompound nbt = stack.getOrDefault(ModDataComponentTypes.UPGRADES, new NbtCompound());

        NbtList abilitiesNbt = new NbtList();
        if (nbt.contains("upgrades")) {
            abilitiesNbt = nbt.getList("upgrades", NbtElement.COMPOUND_TYPE);
        }

        List<ItemStack> abilitiesList = new ArrayList<>();
        for (NbtElement nbtElement : abilitiesNbt) {
            if (nbtElement instanceof NbtCompound nbtCompound) {
                ItemStack upgradeStack = ItemStack.fromNbtOrEmpty(world.getRegistryManager(), nbtCompound);
                if (upgradeStack != null) {
                    abilitiesList.add(upgradeStack);
                }
            }
        }
        return abilitiesList;
    }

    @Override
    public void setUpgrades(ItemStack stack, List<ItemStack> upgrades, World world) {
        NbtCompound nbt = new NbtCompound();

        NbtList upgradesNbt = new NbtList();

        for (ItemStack upgradeStack : upgrades) {
            upgradesNbt.add(upgradeStack.encodeAllowEmpty(world.getRegistryManager()));
        }

        nbt.put("upgrades", upgradesNbt);

        stack.set(ModDataComponentTypes.UPGRADES, nbt);
    }
}
