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
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.component.ModDataComponentTypes;
import net.togyk.myneheroes.upgrade.AbilityUpgrade;
import net.togyk.myneheroes.upgrade.Upgrade;
import net.togyk.myneheroes.util.AbilityUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdvancedArmorItem extends ArmorItem implements AbilityHolding, UpgradableItem {
    private final Text titleText;

    public AdvancedArmorItem(Text titleText, RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
        this.titleText = titleText;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "heading"))).formatted(Formatting.GRAY));
        tooltip.add(titleText);

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
    public void saveAbility(ItemStack stack, World world, Ability ability) {
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
}
