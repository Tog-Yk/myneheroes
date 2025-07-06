package net.togyk.myneheroes.upgrade;

import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Abilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Upgrades {
    private static final Map<Identifier, Upgrade> UPGRADES = new HashMap<>();

    public static final Upgrade MECHANICAL_HUD = registerUpgrade(new AbilityUpgrade(Abilities.TOGGLE_MECHANICAL_HUD, List.of(ArmorItem.Type.HELMET), Identifier.of(MyneHeroes.MOD_ID, "mechanical_hud")));
    public static final Upgrade SPEEDSTER_HUD = registerUpgrade(new AbilityUpgrade(Abilities.TOGGLE_SPEEDSTER_HUD, List.of(ArmorItem.Type.HELMET), Identifier.of(MyneHeroes.MOD_ID, "speedster_hud")));
    public static final Upgrade LASER = registerUpgrade(new AbilityUpgrade(Abilities.SHOOT_LASER, List.of(ArmorItem.Type.CHESTPLATE), Identifier.of(MyneHeroes.MOD_ID, "lazar")));
    public static final Upgrade FlY = registerUpgrade(new AbilityUpgrade(Abilities.ALLOW_FLYING, List.of(ArmorItem.Type.BOOTS), Identifier.of(MyneHeroes.MOD_ID, "fly")));
    public static final Upgrade KINETIC_ENERGY_STORAGE = registerUpgrade(new AbilityUpgrade(Abilities.RELEASE_KINETIC_ENERGY, List.of(ArmorItem.Type.HELMET, ArmorItem.Type.CHESTPLATE, ArmorItem.Type.LEGGINGS, ArmorItem.Type.BOOTS), Identifier.of(MyneHeroes.MOD_ID, "kinetic_energy_storage")));
    public static final Upgrade TAKE_OFF_SUIT = registerUpgrade(new AbilityUpgrade(Abilities.TAKE_OFF_SUIT, List.of(ArmorItem.Type.HELMET, ArmorItem.Type.CHESTPLATE, ArmorItem.Type.LEGGINGS, ArmorItem.Type.BOOTS), Identifier.of(MyneHeroes.MOD_ID, "take_off_suit")));
    public static final Upgrade TOOLBELT_3 = registerUpgrade(new AbilityUpgrade(Abilities.TOOLBELT_3, List.of(ArmorItem.Type.LEGGINGS), Identifier.of(MyneHeroes.MOD_ID, "toolbelt_3")));

    public static final Upgrade COLOR = registerUpgrade(new ColorUpgrade(List.of(), Identifier.of(MyneHeroes.MOD_ID, "color")));


    private static Upgrade registerUpgrade(Upgrade upgrade) {
        if (!UPGRADES.containsKey(upgrade.getId())) {
            UPGRADES.put(upgrade.getId(), upgrade);
            return upgrade;
        } else {
            MyneHeroes.LOGGER.error("There already exist an power with the id of {}", upgrade.getId());
            return null;
        }
    }

    public static Upgrade get(Identifier id) {
        Upgrade upgrade = UPGRADES.getOrDefault(id, null);
        if (upgrade != null) {
            return upgrade.copy();
        }
        return null;
    }

    public static boolean contains(Identifier id) {
        return UPGRADES.containsKey(id);
    }
}
