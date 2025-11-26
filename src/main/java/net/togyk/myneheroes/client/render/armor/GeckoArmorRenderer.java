package net.togyk.myneheroes.client.render.armor;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.AdvancedGeckoArmorItem;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.Color;

public class GeckoArmorRenderer extends GeoArmorRenderer<AdvancedGeckoArmorItem> {
    private int index = 0;

    public GeckoArmorRenderer() {
        super(new GeckoArmorModel());
    }

    @Override
    public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    @Override
    public void render(MatrixStack poseStack, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, int colour) {
        super.render(poseStack, buffer, 15728880, packedOverlay, colour);
    }

    @Override
    public Color getRenderColor(AdvancedGeckoArmorItem animatable, float partialTick, int packedLight) {
        return Color.WHITE;
    }

    @Override
    public Identifier getTextureLocation(AdvancedGeckoArmorItem animatable) {
        ItemStack stack = getCurrentStack();
        ArmorMaterial.Layer layer = ((AdvancedGeckoArmorItem) stack.getItem()).getMaterial().value().layers().get(index);
        String layerPath = layer.getTexture(true).getPath();
        String layerName = layerPath.substring("textures/models/armor/".length(), layerPath.length() - "_layer_2.png".length());
        return Identifier.of(layer.getTexture(true).getNamespace(), "textures/models/armor/geo/"+layerName+".png");
    }

    @Override
    public void defaultRender(MatrixStack poseStack, AdvancedGeckoArmorItem animatable, VertexConsumerProvider bufferSource, @Nullable RenderLayer renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        poseStack.push();

        int renderColor = getRenderColor(animatable, partialTick, packedLight).argbInt();
        int packedOverlay = getPackedOverlay(animatable, 0, partialTick);
        BakedGeoModel model = getGeoModel().getBakedModel(getGeoModel().getModelResource(animatable));

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

                preApplyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, packedLight, packedLight, packedOverlay);
                actuallyRender(poseStack, animatable, model, renderType,
                        bufferSource, buffer, i != 0, partialTick, packedLight, packedOverlay, renderColor);
                applyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
                postRender(poseStack, animatable, model, bufferSource, buffer, i != 0, partialTick, packedLight, packedOverlay, renderColor);
            }
            firePostRenderEvent(poseStack, model, bufferSource, partialTick, packedLight);
        }

        poseStack.pop();

        renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, renderColor);
        this.index = 0;
    }
}
