package net.togyk.myneheroes.upgrade;

import net.minecraft.item.ArmorItem;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.ModItems;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Abilities;
import net.togyk.myneheroes.registry.ModRegistries;

import java.util.List;

public class Upgrades {
    public static final AbilityUpgrade MECHANICAL_HUD = registerUpgrade(new AbilityUpgrade(Abilities.TOGGLE_MECHANICAL_HUD, List.of(ArmorItem.Type.HELMET), Identifier.of(MyneHeroes.MOD_ID, "mechanical_hud")));
    public static final AbilityUpgrade SPEEDSTER_HUD = registerUpgrade(new AbilityUpgrade(Abilities.TOGGLE_SPEEDSTER_HUD, List.of(ArmorItem.Type.HELMET), Identifier.of(MyneHeroes.MOD_ID, "speedster_hud")));
    public static final AbilityUpgrade LASER = registerUpgrade(new AbilityUpgrade(Abilities.SHOOT_LASER, List.of(ArmorItem.Type.CHESTPLATE), Identifier.of(MyneHeroes.MOD_ID, "lazar")));
    public static final AbilityUpgrade FlY = registerUpgrade(new AbilityUpgrade(Abilities.ALLOW_FLYING, List.of(ArmorItem.Type.BOOTS), Identifier.of(MyneHeroes.MOD_ID, "fly")));
    public static final AbilityUpgrade KINETIC_ENERGY_STORAGE = registerUpgrade(new AbilityUpgrade(Abilities.RELEASE_KINETIC_ENERGY, List.of(ArmorItem.Type.HELMET, ArmorItem.Type.CHESTPLATE, ArmorItem.Type.LEGGINGS, ArmorItem.Type.BOOTS), Identifier.of(MyneHeroes.MOD_ID, "kinetic_energy_storage")));
    public static final AbilityUpgrade TAKE_OFF_SUIT = registerUpgrade(new AbilityUpgrade(Abilities.TAKE_OFF_SUIT, List.of(ArmorItem.Type.HELMET, ArmorItem.Type.CHESTPLATE, ArmorItem.Type.LEGGINGS, ArmorItem.Type.BOOTS), Identifier.of(MyneHeroes.MOD_ID, "take_off_suit")));
    public static final AbilityUpgrade TOOLBELT_3 = registerUpgrade(new AbilityUpgrade(Abilities.TOOLBELT_3, List.of(ArmorItem.Type.LEGGINGS), Identifier.of(MyneHeroes.MOD_ID, "toolbelt_3")));

    public static final StockPileUpgrade WEB_SHOOTER = registerUpgrade(new StockPileUpgrade(Abilities.WEB_SHOOTER, 250, ModItems.WEB_FLUID, 125, List.of(ArmorItem.Type.CHESTPLATE), Identifier.of(MyneHeroes.MOD_ID, "web_shooter")));

    public static final ColorUpgrade COLOR = registerUpgrade(new ColorUpgrade(List.of(), Identifier.of(MyneHeroes.MOD_ID, "color")));


    private static <T extends Upgrade> T registerUpgrade(T upgrade) {
        return Registry.register(ModRegistries.UPGRADE, upgrade.id, upgrade);
    }

    public static Upgrade get(Identifier id) {
        Upgrade upgrade = ModRegistries.UPGRADE.get(id);
        if (upgrade != null) {
            return upgrade.copy();
        }
        return null;
    }

    public static boolean contains(Identifier id) {
        return ModRegistries.UPGRADE.containsId(id);
    }
}
