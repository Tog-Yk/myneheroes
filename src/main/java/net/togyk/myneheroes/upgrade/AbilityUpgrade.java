package net.togyk.myneheroes.upgrade;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.UpgradableItem;
import net.togyk.myneheroes.Item.custom.UpgradeHolding;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.util.AbilityUtil;

import java.util.ArrayList;
import java.util.List;

public class AbilityUpgrade extends Upgrade {
    protected List<Ability> abilities;

    protected AbilityUpgrade(List<Ability> abilities, List<ArmorItem.Type> compatibleTypes, Identifier id) {
        super(compatibleTypes, id);
        this.abilities = abilities.stream().map(Ability::copy).toList();
    }
    protected AbilityUpgrade(Ability ability, List<ArmorItem.Type> compatibleTypes, Identifier id) {
        this(List.of(ability), compatibleTypes, id);
    }

    public List<Ability> getAbilities() {
        abilities.forEach(ability -> ability.setHolder(this));
        return abilities;
    }

    public void saveAbility(Ability ability) {
        this.save();
    }

    public void save() {
        ItemStack stack = this.getHolderStack();
        if (stack != null) {
            if (stack.getItem() instanceof UpgradableItem upgradableItem) {
                upgradableItem.saveUpgrade(stack, this);
            } else if (stack.getItem() instanceof UpgradeHolding upgradeHolding) {
                upgradeHolding.saveUpgrade(stack, this);
            }
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList abilitiesNbt = new NbtList();
        for (Ability ability : this.abilities) {
            if (ability != null) {
                ability.setHolder(this);
                abilitiesNbt.add(ability.writeNbt(new NbtCompound()));
            }
        }

        nbt.put("abilities", abilitiesNbt);

        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

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
        this.abilities = abilitiesList;
    }

    @Override
    public Upgrade copy() {
        return new AbilityUpgrade(abilities, compatibleTypes, id);
    }
}
