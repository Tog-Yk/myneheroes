package net.togyk.myneheroes.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.ability.Abilities;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.upgrade.Upgrade;
import net.togyk.myneheroes.upgrade.Upgrades;

import java.util.*;

public class AbilityUtil {

    public static Ability getAbilityMatchingId(List<Ability> abilityList, Identifier id) {
        for (Ability ability : abilityList) {
            if (ability != null && ability.getId() == id) {
                return ability;
            }
        }
        return null;
    }

    public static List<Ability> getAbilitiesMatchingId(List<Ability> abilityList, Identifier id) {
        List<Ability> abilities = new ArrayList<>();
        for (Ability ability : abilityList) {
            if (ability != null && ability.getId() == id) {
                abilities.add(ability);
            }
        }
        return abilities;
    }

    public static List<StockPile> getStockPilesMatchingId(List<StockPile> stockPiles, Identifier id) {
        List<StockPile> abilities = new ArrayList<>();
        for (StockPile stockPile : stockPiles) {
            if (stockPile != null && stockPile.getStockPileId() == id) {
                abilities.add(stockPile);
            }
        }
        return abilities;
    }

    public static Ability nbtToAbility(NbtCompound nbt) {
        Identifier abilityId = Identifier.of(nbt.getString("id"));
        Ability ability = Abilities.get(abilityId);
        if (ability != null) {
            ability.readNbt(nbt);
        }
        return ability;
    }

    public static Upgrade nbtToUpgrade(NbtCompound nbt) {
        Identifier upgradeId = Identifier.of(nbt.getString("id"));
        Upgrade upgrade = Upgrades.get(upgradeId);
        if (upgrade != null) {
            upgrade.readNbt(nbt);
        }
        return upgrade;
    }
}
