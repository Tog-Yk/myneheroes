package net.togyk.myneheroes.Item.custom;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.component.ModDataComponentTypes;
import net.togyk.myneheroes.networking.SaveUpgradePayload;
import net.togyk.myneheroes.upgrade.AbilityUpgrade;
import net.togyk.myneheroes.upgrade.Upgrade;
import net.togyk.myneheroes.util.AbilityUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AdvancedArmorItem extends Item implements AbilityHolding, UpgradableItem {
    private final Text titleText;
    private final EquipmentType type;
    private final ArmorMaterial material;

    public AdvancedArmorItem(Text titleText, ArmorMaterial material, EquipmentType type, Item.Settings settings) {
        super(settings.armor(material, type));
        this.titleText = titleText;
        this.type = type;
        this.material = material;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        textConsumer.accept(Text.translatable(Util.createTranslationKey("armor_pattern", Identifier.of(MyneHeroes.MOD_ID, "heading"))).formatted(Formatting.GRAY));
        textConsumer.accept(titleText);

        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
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

        NbtList upgradesNbt = nbt.getListOrEmpty("upgrades");

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
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, world, entity, slot);
        stack.setHolder(entity);
    }

    @Override
    public boolean canUpgrade(ItemStack stack, Upgrade upgrade) {
        boolean hasUpgrade = hasUpgrade(stack, upgrade);
        boolean canBePutInSlot = upgrade.getCompatibleTypes().contains(this.type);
        return !hasUpgrade && canBePutInSlot;
    }

    public boolean hasUpgrade(ItemStack stack, Upgrade upgrade) {
        return this.getUpgrades(stack).stream().map(Upgrade::getId).toList().contains(upgrade.getId());
    }

    @Override
    public List<Upgrade> getUpgrades(ItemStack stack) {
        NbtCompound nbt = stack.getOrDefault(ModDataComponentTypes.UPGRADES, new NbtCompound());

        NbtList upgradesNbt = nbt.getListOrEmpty("upgrades");

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

    public EquipmentSlot getSlotType() {
        return this.type.getEquipmentSlot();
    }

    public ArmorMaterial getMaterial() {
        return material;
    }
}
