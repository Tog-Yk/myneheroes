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
    public static final AbilityUpgrade MECHANICAL_HUD = registerUpgrade(new AbilityUpgrade(
            Abilities.TOGGLE_MECHANICAL_HUD,
            List.of(
                    ArmorItem.Type.HELMET
            ),
            Identifier.of(MyneHeroes.MOD_ID,"mechanical_hud")
    ));
    public static final AbilityUpgrade SPEEDSTER_HUD = registerUpgrade(new AbilityUpgrade(
            Abilities.TOGGLE_SPEEDSTER_HUD,
            List.of(ArmorItem.Type.HELMET),
            Identifier.of(MyneHeroes.MOD_ID,"speedster_hud")));
    public static final AbilityUpgrade LASER = registerUpgrade(new AbilityUpgrade(
            Abilities.SHOOT_LASER,
            List.of(
                    ArmorItem.Type.CHESTPLATE
            ),
            Identifier.of(MyneHeroes.MOD_ID,"lazar")));
    public static final AbilityUpgrade FlY = registerUpgrade(new AbilityUpgrade(
            Abilities.ALLOW_FLYING,
            List.of(
                    ArmorItem.Type.BOOTS
            ),
            Identifier.of(MyneHeroes.MOD_ID,"fly")));
    public static final AbilityUpgrade KINETIC_ENERGY_STORAGE = registerUpgrade(new AbilityUpgrade(
            Abilities.RELEASE_KINETIC_ENERGY,
            List.of(
                    ArmorItem.Type.HELMET,
                    ArmorItem.Type.CHESTPLATE,
                    ArmorItem.Type.LEGGINGS,
                    ArmorItem.Type.BOOTS
            ),
            Identifier.of(MyneHeroes.MOD_ID,"kinetic_energy_storage")));
    public static final AbilityUpgrade TAKE_OFF_SUIT = registerUpgrade(new AbilityUpgrade(
            Abilities.TAKE_OFF_SUIT,
            List.of(
                    ArmorItem.Type.HELMET,
                    ArmorItem.Type.CHESTPLATE,
                    ArmorItem.Type.LEGGINGS,
                    ArmorItem.Type.BOOTS),
            Identifier.of(MyneHeroes.MOD_ID,"take_off_suit")));
    public static final AbilityUpgrade TOOLBELT_3 = registerUpgrade(new AbilityUpgrade(
            Abilities.TOOLBELT_3,
            List.of(
                    ArmorItem.Type.LEGGINGS
            ),
            Identifier.of(MyneHeroes.MOD_ID,"toolbelt_3")));
    public static final AbilityUpgrade TOOLBELT_4 = registerUpgrade(new AbilityUpgrade(
            Abilities.TOOLBELT_4,
            List.of(
                    ArmorItem.Type.LEGGINGS
            ),
            Identifier.of(MyneHeroes.MOD_ID,"toolbelt_4")));
    public static final AbilityUpgrade TOOLBELT_6 = registerUpgrade(new AbilityUpgrade(
            Abilities.TOOLBELT_6,
            List.of(
                    ArmorItem.Type.LEGGINGS
            ),
            Identifier.of(MyneHeroes.MOD_ID,"toolbelt_6")));
    public static final AbilityUpgrade TOOLBELT_8 = registerUpgrade(new AbilityUpgrade(
            Abilities.TOOLBELT_8,
            List.of(
                    ArmorItem.Type.LEGGINGS
            ),
            Identifier.of(MyneHeroes.MOD_ID,"toolbelt_8")));

    public static final StockPileUpgrade WEB_SHOOTER = registerUpgrade(new StockPileUpgrade(
            Abilities.WEB_SHOOTER,
            250,
            ModItems.WEB_FLUID,
            125,
            List.of(
                    ArmorItem.Type.CHESTPLATE
            ),
            Identifier.of(MyneHeroes.MOD_ID,"web_shooter")));
    
    public static final SimpleUpgrade ANTI_RADIATION = registerUpgrade(new SimpleUpgrade(
            List.of(
                    ArmorItem.Type.HELMET,
                    ArmorItem.Type.CHESTPLATE,
                    ArmorItem.Type.LEGGINGS,
                    ArmorItem.Type.BOOTS
            ),
            Identifier.of(MyneHeroes.MOD_ID,"anti_radiation")
    ));

    public static final AbilityUpgrade STRENGTH_BOOST = registerUpgrade(new AbilityUpgrade(
            Abilities.STRENGTH_BOOST,
            List.of(
                    ArmorItem.Type.HELMET,
                    ArmorItem.Type.CHESTPLATE,
                    ArmorItem.Type.LEGGINGS,
                    ArmorItem.Type.BOOTS),
            Identifier.of(MyneHeroes.MOD_ID,"strength_boost")));

    public static final AbilityUpgrade JUMP_BOOST = registerUpgrade(new AbilityUpgrade(
            Abilities.JUMP_BOOST,
            List.of(
                    ArmorItem.Type.HELMET,
                    ArmorItem.Type.CHESTPLATE,
                    ArmorItem.Type.LEGGINGS,
                    ArmorItem.Type.BOOTS),
            Identifier.of(MyneHeroes.MOD_ID,"jump_boost")));

    public static final AbilityUpgrade SPEED_BOOST = registerUpgrade(new AbilityUpgrade(
            Abilities.SPEED_BOOST,
            List.of(
                    ArmorItem.Type.HELMET,
                    ArmorItem.Type.CHESTPLATE,
                    ArmorItem.Type.LEGGINGS,
                    ArmorItem.Type.BOOTS),
            Identifier.of(MyneHeroes.MOD_ID,"speed_boost")));

    public static final ColorUpgrade COLOR = registerUpgrade(new ColorUpgrade(
            List.of(),
            Identifier.of(MyneHeroes.MOD_ID,"color")
    ));


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
