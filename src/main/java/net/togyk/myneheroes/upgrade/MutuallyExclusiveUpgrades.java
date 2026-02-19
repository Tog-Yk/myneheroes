package net.togyk.myneheroes.upgrade;

import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class MutuallyExclusiveUpgrades {
    private static List<List<Identifier>> MEUpgrades = new ArrayList<>();

    public static void add(List<Upgrade> upgrades) {
        MEUpgrades.add(upgrades.stream().map(Upgrade::getId).toList());
    }

    static {
        add(List.of(Upgrades.TOOLBELT_3, Upgrades.TOOLBELT_4, Upgrades.TOOLBELT_6, Upgrades.TOOLBELT_8));
        add(List.of(Upgrades.MECHANICAL_HUD, Upgrades.SPEEDSTER_HUD));
    }

    public static List<Identifier> get(Upgrade upgrade) {
        List<Identifier> ids = new ArrayList<>();
        for (List<Identifier> list : MEUpgrades) {
            if (list.contains(upgrade.getId())) {
                for (Identifier id : list) {
                if (!ids.contains(id) && id != upgrade.getId()) {
                        ids.add(id);
                    }
                }
            }
        }
        return ids;
    }
}
