package net.togyk.myneheroes.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.client.render.ReactorModel;
import net.togyk.myneheroes.client.render.armor.AdvancedArmorModel;
import net.togyk.myneheroes.client.render.armor.AdvancedHelmetWithFaceplateModel;
import net.togyk.myneheroes.client.render.upgrade.ToolbeltModel;
import net.togyk.myneheroes.client.render.upgrade.UpgradeModel;
import net.togyk.myneheroes.entity.ModEntities;

@Environment(EnvType.CLIENT)
public class ModEntityRenderers {
    public static void registerModEntityRenderers() {
        MyneHeroes.LOGGER.info("Registering Mod Entities for " + MyneHeroes.MOD_ID);
        EntityRendererRegistry.INSTANCE.register(ModEntities.LASER, LaserEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(ModEntities.STATIONARY_ITEM, StationaryItemEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(ModEntities.CALLABLE_STATIONARY_ITEM, StationaryItemEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(ModEntities.THROWN_ITEM, ThrownItemEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(ModEntities.CALLABLE_THROWN_ITEM, ThrownItemEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(ModEntities.STATIONARY_ARMOR, StationaryArmorEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(MeteorEntityModel.METEOR, MeteorEntityModel::getTexturedModelData);
        EntityRendererRegistry.INSTANCE.register(ModEntities.METEOR, MeteorEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(ModEntities.LIGHTNING_TRAIL, TrailEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(ModEntities.AFTERIMAGE_TRAIL, TrailEntityRenderer::new);

        EntityRendererRegistry.INSTANCE.register(ModEntities.WEB, WebEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(ModEntities.SWING_WEB, SwingWebEntityRenderer::new);


        EntityModelLayerRegistry.registerModelLayer(ReactorModel.REACTOR, ReactorModel::getTexturedModelData);

        EntityModelLayerRegistry.registerModelLayer(ToolbeltModel.TOOLBELT, ToolbeltModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(UpgradeModel.UPGRADE, UpgradeModel::getTexturedModelData);

        EntityModelLayerRegistry.registerModelLayer(AdvancedArmorModel.ADVANCED_ARMOR, AdvancedArmorModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(AdvancedHelmetWithFaceplateModel.ADVANCED_HELMET_W_FACEPLATE, AdvancedHelmetWithFaceplateModel::getTexturedModelData);
    }
}
