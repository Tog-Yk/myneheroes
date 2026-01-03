package net.togyk.myneheroes.client.render.entity.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.PowerData;

@Environment(EnvType.CLIENT)
public class TintableSkinFeature extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public TintableSkinFeature(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        for (Power power : PowerData.getPowers(player)) {
            if (!power.appliesSkin()) continue;

            Identifier base_texture = power.getBaseSkin();
            Identifier tintable_texture = power.getTintableSkin();
            Identifier emmisive_texture = power.getEmmisiveSkin();
            Identifier tintable_emmisive_texture = power.getEmmisiveTintableSkin();

            int opacity = power.getSkinOpacity();

            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(base_texture));
            this.getContextModel().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, ColorHelper.Argb.withAlpha(opacity, Colors.WHITE));

            ResourceManager manager = MinecraftClient.getInstance().getResourceManager();
            if (!manager.getResource(tintable_texture).isEmpty()) {
                vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(tintable_texture));
                this.getContextModel().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, ColorHelper.Argb.withAlpha(opacity, power.getTintableSkinColor()));
            }

            if (!manager.getResource(emmisive_texture).isEmpty()) {
                vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(emmisive_texture));
                this.getContextModel().render(matrices, vertexConsumer, 15728880, OverlayTexture.DEFAULT_UV, ColorHelper.Argb.withAlpha(opacity, Colors.WHITE));
            }

            if (!manager.getResource(tintable_emmisive_texture).isEmpty()) {
                vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(tintable_emmisive_texture));
                this.getContextModel().render(matrices, vertexConsumer, 15728880, OverlayTexture.DEFAULT_UV, ColorHelper.Argb.withAlpha(opacity, power.getEmmisiveTintableSkinColor()));
            }
        }
    }
}
