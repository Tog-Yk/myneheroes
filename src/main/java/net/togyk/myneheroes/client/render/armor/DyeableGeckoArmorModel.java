package net.togyk.myneheroes.client.render.armor;

import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.DyeableAdvancedGeckoArmorItem;
import net.togyk.myneheroes.MyneHeroes;
import software.bernie.geckolib.model.GeoModel;

public class DyeableGeckoArmorModel extends GeoModel<DyeableAdvancedGeckoArmorItem> {
    @Override
    public Identifier getModelResource(DyeableAdvancedGeckoArmorItem animatable) {
        return Identifier.of(MyneHeroes.MOD_ID, "geo/gecko_armor.geo.json");
    }

    @Override
    public Identifier getTextureResource(DyeableAdvancedGeckoArmorItem animatable) {
        //return Identifier.of(MyneHeroes.MOD_ID, "textures/armor/geo/" + animatable.getMaterial().getType().name()+ ".png");
        return null;
    }

    @Override
    public Identifier getAnimationResource(DyeableAdvancedGeckoArmorItem animatable) {
        return Identifier.of(MyneHeroes.MOD_ID, "animations/gecko_armor.animation.json");
    }
}
