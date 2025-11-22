package net.togyk.myneheroes.client.render.armor;

import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.AdvancedGeckoArmorItem;
import net.togyk.myneheroes.MyneHeroes;
import software.bernie.geckolib.model.GeoModel;

public class GeckoArmorModel extends GeoModel<AdvancedGeckoArmorItem> {
    @Override
    public Identifier getModelResource(AdvancedGeckoArmorItem animatable) {
        return Identifier.of(MyneHeroes.MOD_ID, "geo/gecko_armor.geo.json");
    }

    @Override
    public Identifier getTextureResource(AdvancedGeckoArmorItem animatable) {
        //return Identifier.of(MyneHeroes.MOD_ID, "textures/armor/geo/" + animatable.getMaterial().getType().name()+ ".png");
        return null;
    }

    @Override
    public Identifier getAnimationResource(AdvancedGeckoArmorItem animatable) {
        return Identifier.of(MyneHeroes.MOD_ID, "animations/gecko_armor.animation.json");
    }
}
