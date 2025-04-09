package net.togyk.myneheroes.Item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
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
import net.minecraft.world.World;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Abilities;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.AbilityUtil;
import net.togyk.myneheroes.ability.BooleanAbility;
import net.togyk.myneheroes.component.ModDataComponentTypes;
import net.togyk.myneheroes.power.AbilityHolding;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdvancedArmorItem extends ArmorItem implements AbilityHolding {
    public AdvancedArmorItem(@Nullable Ability suitSpecificAbility,RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
    }
    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        for (Ability ability: this.getAbilities(stack)) {
            ability.appendTooltip(stack, context, tooltip, type);
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
    public boolean UnlockAbility(ItemStack stack, Identifier id) {
        List<Identifier> abilitiesIds = this.getAbilities(stack).stream().map(Ability::getId).toList();
        if (!abilitiesIds.contains(id)) {
            //save to nbt
            this.saveAbility(stack, Abilities.get(id));
            return true;
        } else {
            return false;
        }
    }

    public List<Ability> getAbilities(ItemStack stack) {
        NbtCompound nbt = stack.getOrDefault(ModDataComponentTypes.ABILITIES, new NbtCompound());

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
    }

    @Override
    public void saveAbility(ItemStack stack, Ability ability) {
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
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        stack.setHolder(entity);
    }
}
