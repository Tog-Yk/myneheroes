package net.togyk.myneheroes;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.togyk.myneheroes.Item.ModPredicates;
import net.togyk.myneheroes.Item.color.ModColorProvider;
import net.togyk.myneheroes.block.ModBlockEntityTypes;
import net.togyk.myneheroes.block.ModBlocks;
import net.togyk.myneheroes.client.AbilityOverlay;
import net.togyk.myneheroes.client.render.ability.RenderEvent;
import net.togyk.myneheroes.client.render.block_entity.MeteorRadarBlockEntityRenderer;
import net.togyk.myneheroes.client.render.entity.ModEntityRenderers;
import net.togyk.myneheroes.client.screen.ModHandledScreens;
import net.togyk.myneheroes.keybind.ModKeyBinds;
import net.togyk.myneheroes.keybind.ModKeybindingHelper;
import net.togyk.myneheroes.networking.ModMessages;
import net.togyk.myneheroes.particle.ElectricityParticle;
import net.togyk.myneheroes.particle.ModParticles;

public class MyneHeroesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new AbilityOverlay());
        ModKeyBinds.registerKeyBinds();
        ModKeybindingHelper.registerModKeybingHelper();
        ModEntityRenderers.registerModEntityRenderers();

        ModMessages.registerClientMessages();

        ModColorProvider.registerColorProviders();

        ModHandledScreens.registerModScreenHandlerTypes();

        ModPredicates.registerModPredicates();

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.KRYPTONITE_CLUSTER, RenderLayer.getCutout());
        BlockEntityRendererFactories.register(ModBlockEntityTypes.METEOR_RADAR_BLOCK_ENTITY, MeteorRadarBlockEntityRenderer::new);

        RenderEvent.registerRenderEvent();

        ParticleFactoryRegistry.getInstance().register(ModParticles.ELECTRICITY_PARTICLE, ElectricityParticle.Factory::new);
    }
}
