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
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.client.render.entity.feature.TintableSkinFeature;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.PlayerHoverFlightControl;
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

    @Inject(
            method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;" +
                    "Lnet/minecraft/client/util/math/MatrixStack;FFFF)V",
            at = @At("TAIL")
    )
    private void myneheroes$includeHoverFlying(AbstractClientPlayerEntity player, MatrixStack matrixStack, float f, float g, float h, float i, CallbackInfo ci) {
        float flightProgress = ((PlayerHoverFlightControl) player).myneheroes$getFlightProgress();
        if (flightProgress != 0) {
            float pitch = player.getPitch(h);

            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(((PlayerHoverFlightControl) player).myneheroes$getFlightProgress() * (-90.0F - pitch)));

            Vec3d vec3d = player.getRotationVec(h);
            Vec3d vec3d2 = player.lerpVelocity(h);
            double d = vec3d2.horizontalLengthSquared();
            double e = vec3d.horizontalLengthSquared();
            if (d > 0.0 && e > 0.0) {
                double n = (vec3d2.x * vec3d.x + vec3d2.z * vec3d.z) / Math.sqrt(d * e);
                double o = vec3d2.x * vec3d.z - vec3d2.z * vec3d.x;
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float) (Math.signum(o) * Math.acos(n))));
            }
        }
    }
}
