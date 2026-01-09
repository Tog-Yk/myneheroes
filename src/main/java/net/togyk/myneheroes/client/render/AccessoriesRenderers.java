package net.togyk.myneheroes.client.render;

import io.wispforest.accessories.api.client.AccessoriesRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.togyk.myneheroes.Item.ModItems;
import net.togyk.myneheroes.client.render.accessory.ArmorRegistryAccessoryRenderer;

@Environment(EnvType.CLIENT)
public class AccessoriesRenderers {
    public static void  registerAccessoriesRenderers() {
        AccessoriesRendererRegistry.registerRenderer(ModItems.TOOLBELT, ArmorRegistryAccessoryRenderer::new);
        AccessoriesRendererRegistry.registerRenderer(ModItems.IRON_TOOLBELT, ArmorRegistryAccessoryRenderer::new);
        AccessoriesRendererRegistry.registerRenderer(ModItems.DIAMOND_TOOLBELT, ArmorRegistryAccessoryRenderer::new);
        AccessoriesRendererRegistry.registerRenderer(ModItems.NETHERITE_TOOLBELT, ArmorRegistryAccessoryRenderer::new);
        AccessoriesRendererRegistry.registerRenderer(ModItems.ARC_REACTOR, ArmorRegistryAccessoryRenderer::new);
    }
}
