package net.togyk.myneheroes;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.togyk.myneheroes.Item.ModPredicates;
import net.togyk.myneheroes.block.ModBlockEntityTypes;
import net.togyk.myneheroes.block.ModBlocks;
import net.togyk.myneheroes.client.HudOverlay;
import net.togyk.myneheroes.Item.color.ModColorProvider;
import net.togyk.myneheroes.client.render.block_entity.MeteorRadarBlockEntityRenderer;
import net.togyk.myneheroes.client.render.entity.ModEntityRenderers;
import net.togyk.myneheroes.client.screen.ModHandledScreens;
import net.togyk.myneheroes.keybind.ModKeybindingHelper;
import net.togyk.myneheroes.keybind.ModKeyBindings;
import net.togyk.myneheroes.networking.ModMessages;

public class MyneHeroesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new HudOverlay());
        ModKeyBindings.registerKeyBinds();
        ModKeybindingHelper.registerModKeybingHelper();
        ModEntityRenderers.registerModEntityRenderers();

        ModMessages.registerClientMessages();

        ModColorProvider.registerColorProviders();

        ModHandledScreens.registerModScreenHandlerTypes();

        ModPredicates.registerModPredicates();

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.KRYPTONITE_CLUSTER, RenderLayer.getCutout());
        BlockEntityRendererFactories.register(ModBlockEntityTypes.METEOR_RADAR_BLOCK_ENTITY, MeteorRadarBlockEntityRenderer::new);
    }
}
