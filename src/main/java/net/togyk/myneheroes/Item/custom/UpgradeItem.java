package net.togyk.myneheroes.Item.custom;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.component.ModDataComponentTypes;
import net.togyk.myneheroes.util.AbilityUtil;

import java.util.ArrayList;
import java.util.List;

public class UpgradeItem extends Item {
    private final List<Ability> abilities;
    private final int levelCost;
    private final ArmorItem.Type equipmentSlot;

    private ItemStack holder;

    public UpgradeItem(Ability ability, int levelCost, ArmorItem.Type slot, Settings settings) {
        super(settings);

        this.abilities = List.of(ability);
        this.levelCost = levelCost;
        this.equipmentSlot = slot;
    }

    public UpgradeItem(List<Ability> abilities, int levelCost, ArmorItem.Type slot, Settings settings) {
        super(settings);

        this.abilities = abilities;
        this.levelCost = levelCost;
        this.equipmentSlot = slot;
    }

    public int getLevelCost() {
        return levelCost;
    }

    public ArmorItem.Type getEquipmentSlot() {
        return equipmentSlot;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }


    public List<Ability> getAbilities(ItemStack stack) {
        NbtCompound nbt = stack.getOrDefault(ModDataComponentTypes.ABILITIES, null);
        List<Ability> abilities = new ArrayList<>();
        if (nbt == null) {
            abilities = this.abilities;
        } else {
            NbtList abilitiesNbt = new NbtList();
            if (nbt.contains("abilities")) {
                abilitiesNbt = nbt.getList("abilities", NbtElement.COMPOUND_TYPE);
            }

            for (NbtElement nbtElement : abilitiesNbt) {
                if (nbtElement instanceof NbtCompound nbtCompound) {
                    Ability ability = AbilityUtil.nbtToAbility(nbtCompound);
                    if (ability != null) {
                        abilities.add(ability);
                    }
                }
            }
        }

        for (Ability ability : abilities) {
            ability.setHolder(stack);
        }

        return abilities;
    }

    public void saveAbility(ItemStack stack, World world, Ability ability) {
        NbtCompound nbt = stack.getOrDefault(ModDataComponentTypes.ABILITIES, new NbtCompound());

        NbtList abilitiesNbt = new NbtList();
        if (nbt.contains("abilities")) {
            abilitiesNbt = nbt.getList("abilities", NbtElement.COMPOUND_TYPE);
        }

        //getting a list of all the id to replace the correct one
        List<Ability> loadedAbilities = this.getAbilities(stack);
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

        this.save(stack, world);
    }

    public void setHolder(ItemStack holder) {
        this.holder = holder;
    }


    public ItemStack getHolder() {
        return this.holder;
    }

    private void save(ItemStack stack, World world) {
        if (this.getHolder().getItem() instanceof UpgradableItem upgradableItem) {
            upgradableItem.saveUpgrade(this.getHolder(), stack, world);
        }
    }
}
