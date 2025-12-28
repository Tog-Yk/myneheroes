package net.togyk.myneheroes.client.render.entity;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.togyk.myneheroes.Item.custom.StationaryItem;
import net.togyk.myneheroes.entity.StationaryItemEntity;
import org.joml.Vector3f;

public class StationaryItemEntityRenderer<T extends StationaryItemEntity, M extends EntityModel<T>> extends LivingEntityRenderer<T, M> {
    private final ItemRenderer itemRenderer;

    public StationaryItemEntityRenderer(EntityRendererFactory.Context context) {
        super(context, null, 0.0F);
        this.itemRenderer = context.getItemRenderer();
    }


    @Override
    public Identifier getTexture(T entity) {
        return null;
    }

    @Override
    public void render(T entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
        matrixStack.push();
        ItemStack stack = entity.getEquippedStack(null);

        // Calculate rotation angles based on current velocity:
        if (stack.getItem() instanceof StationaryItem stationary) {
            matrixStack.translate(0.0F, 0, 0.0F);

            //groundOffset

            Vector3f followDirection = entity.getFollowDirection();

            float yaw = getRad(followDirection.x, followDirection.z);
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(yaw/*yaw*/));

            double zx = Math.sqrt(Math.pow(followDirection.x, 2) + Math.pow(followDirection.z, 2));

            float pitch = getRad(followDirection.y, zx);
            matrixStack.multiply(RotationAxis.NEGATIVE_X.rotation(pitch/*pitch*/));

            switch (stationary.getStationaryFaceDirection()) {

                case NORTH -> {
                    // Already facing north
                    matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(0));
                }

                case SOUTH -> {
                    // Rotate 180° so south faces north
                    matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
                }

                case WEST -> {
                    // Rotate 90° so west faces north
                    matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90));
                }

                case EAST -> {
                    // Rotate -90° (or 270°) so east faces north
                    matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
                }

                case UP -> {
                    // Rotate -90° (or 270°) so east faces north
                    matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
                }

                case DOWN -> {
                    // Optional: adjust if your throwable can face vertically
                    matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90));
                }
            }

            BakedModel model = this.itemRenderer.getModel(stack, entity.getWorld(), null, entity.getId());
            float k = model.getTransformation().getTransformation(ModelTransformationMode.GROUND).scale.y();
            matrixStack.translate(0.0F, 0.25F * k, 0.0F);
            itemRenderer.renderItem(stack, ModelTransformationMode.GROUND, false, matrixStack, vertexConsumers, light, OverlayTexture.DEFAULT_UV, model);
        }
        matrixStack.pop();
    }

    private float getRad(double opposite, double adjacent) {
        double rawAngle = 0;
        if (adjacent != 0) {
            rawAngle = Math.atan(opposite / adjacent);
        } else {
            double sgn = Math.signum(opposite);
            rawAngle = 0.5*Math.PI * sgn;
        }

        if (adjacent >= 0) {
            return (float) rawAngle;
        } else {
            return (float) (Math.PI + rawAngle);
        }
    }
}
