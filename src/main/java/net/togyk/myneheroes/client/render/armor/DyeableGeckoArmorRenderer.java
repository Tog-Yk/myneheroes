package net.togyk.myneheroes.client.render.armor;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.DyeableAdvancedGeckoArmorItem;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.loading.math.MolangQueries;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class DyeableGeckoArmorRenderer extends GeoArmorRenderer<DyeableAdvancedGeckoArmorItem> {
    private int index = 0;

    public DyeableGeckoArmorRenderer() {
        super(new DyeableGeckoArmorModel());
    }


    @Override
    public Identifier getTextureLocation(DyeableAdvancedGeckoArmorItem animatable) {
        ItemStack stack = getCurrentStack();
        ArmorMaterial.Layer layer = ((DyeableAdvancedGeckoArmorItem) stack.getItem()).getMaterial().value().layers().get(index);
        String layerPath = layer.getTexture(true).getPath();
        String layerName = layerPath.substring("textures/models/armor/".length(), layerPath.length() - "_layer_2.png".length());
        return Identifier.of(layer.getTexture(true).getNamespace(), "textures/models/armor/geo/"+layerName+".png");
    }

    @Override
    public void defaultRender(MatrixStack poseStack, DyeableAdvancedGeckoArmorItem animatable, VertexConsumerProvider bufferSource, @Nullable RenderLayer renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        poseStack.push();

        int renderColor = getRenderColor(animatable, partialTick, packedLight).argbInt();
        int packedOverlay = getPackedOverlay(animatable, 0, partialTick);
        BakedGeoModel model = getGeoModel().getBakedModel(getGeoModel().getModelResource(animatable, this));

        if (renderType == null)
            renderType = getRenderType(animatable, getTextureLocation(animatable), bufferSource, partialTick);

        if (buffer == null && renderType != null)
            buffer = bufferSource.getBuffer(renderType);

        preRender(poseStack, animatable, model, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, renderColor);


        if (firePreRenderEvent(poseStack, model, bufferSource, partialTick, packedLight)) {
            for (int i = 0; i < getAnimatable().getMaterial().value().layers().size(); i++) {
                this.index = i;

                renderType = getRenderType(animatable, getTextureLocation(animatable), bufferSource, partialTick);

                buffer = bufferSource.getBuffer(renderType);

                int light = getAnimatable().layerIsLightable(currentStack, i) ? (animatable.getLightLevel(currentStack, i) / 15) * 15728880 : packedLight;
                int color = getAnimatable().layerIsDyeable(i) ? getAnimatable().getColor(currentStack, i) : renderColor;

                preApplyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, light, light, packedOverlay);
                actuallyRender(poseStack, animatable, model, renderType,
                        bufferSource, buffer, i != 0, partialTick, light, packedOverlay, color);
                applyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, partialTick, light, packedOverlay);
                postRender(poseStack, animatable, model, bufferSource, buffer, i != 0, partialTick, light, packedOverlay, color);
            }
            firePostRenderEvent(poseStack, model, bufferSource, partialTick, packedLight);
        }

        poseStack.pop();

        renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, renderColor);
        doPostRenderCleanup();
        this.index = 0;
        MolangQueries.clearActor();
    }
}
