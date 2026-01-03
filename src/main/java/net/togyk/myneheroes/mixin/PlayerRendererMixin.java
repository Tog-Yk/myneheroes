package net.togyk.myneheroes.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.togyk.myneheroes.client.render.entity.feature.TintableSkinFeature;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.PowerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerRendererMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void addTintableSkinFeature(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        EntityRendererFeatureAccessor accessor = (EntityRendererFeatureAccessor) this;
        accessor.invokeAddFeature(new TintableSkinFeature(((PlayerEntityRenderer) (Object) this)));
    }

    @Inject(method = "renderArm", at = @At("TAIL"))
    private void renderNewArmTextures(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo ci) {
        for (Power power : PowerData.getPowers(player)) {
            if (!power.appliesSkin()) continue;

            Identifier base_texture = power.getBaseSkin();
            Identifier tintable_texture = power.getTintableSkin();
            Identifier emmisive_texture = power.getEmmisiveSkin();
            Identifier tintable_emmisive_texture = power.getEmmisiveTintableSkin();
            int opacity = power.getSkinOpacity();

            arm.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(base_texture)), light, OverlayTexture.DEFAULT_UV, ColorHelper.Argb.withAlpha(opacity, Colors.WHITE));
            sleeve.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(base_texture)), light, OverlayTexture.DEFAULT_UV, ColorHelper.Argb.withAlpha(opacity, Colors.WHITE));

            ResourceManager manager = MinecraftClient.getInstance().getResourceManager();
            if (!manager.getResource(tintable_texture).isEmpty()) {
                arm.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(tintable_texture)), light, OverlayTexture.DEFAULT_UV, ColorHelper.Argb.withAlpha(opacity, power.getTintableSkinColor()));
                sleeve.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(tintable_texture)), light, OverlayTexture.DEFAULT_UV, ColorHelper.Argb.withAlpha(opacity, power.getTintableSkinColor()));
            }

            if (!manager.getResource(emmisive_texture).isEmpty()) {
                arm.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(emmisive_texture)), 15728880, OverlayTexture.DEFAULT_UV, ColorHelper.Argb.withAlpha(opacity, Colors.WHITE));
                sleeve.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(emmisive_texture)), 15728880, OverlayTexture.DEFAULT_UV, ColorHelper.Argb.withAlpha(opacity, Colors.WHITE));
            }

            if (!manager.getResource(tintable_emmisive_texture).isEmpty()) {
                arm.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(tintable_emmisive_texture)), 15728880, OverlayTexture.DEFAULT_UV, ColorHelper.Argb.withAlpha(opacity, power.getEmmisiveTintableSkinColor()));
                sleeve.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(tintable_emmisive_texture)), 15728880, OverlayTexture.DEFAULT_UV, ColorHelper.Argb.withAlpha(opacity, power.getEmmisiveTintableSkinColor()));
            }
        }
    }
}
