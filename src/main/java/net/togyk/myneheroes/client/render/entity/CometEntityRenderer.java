package net.togyk.myneheroes.client.render.entity;

import com.google.common.collect.Maps;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.entity.CometEntity;
import net.togyk.myneheroes.entity.CometVariant;

import java.util.Map;

public class CometEntityRenderer extends ProjectileEntityRenderer<CometEntity> {
    protected CometEntityModel model;
    private static final Map<CometVariant, Identifier> ID_BY_VARIANT =
            Util.make(Maps.newEnumMap(CometVariant.class), map -> {
                map.put(CometVariant.DEFAULT,
                        Identifier.of(MyneHeroes.MOD_ID, "textures/entity/comet/comet.png"));
                map.put(CometVariant.SCULK,
                        Identifier.of(MyneHeroes.MOD_ID, "textures/entity/comet/sculk_comet.png"));
                map.put(CometVariant.KRYPTONITE,
                        Identifier.of(MyneHeroes.MOD_ID, "textures/entity/comet/kryptonite_comet.png"));
                map.put(CometVariant.VIBRANIUM,
                        Identifier.of(MyneHeroes.MOD_ID, "textures/entity/comet/vibranium_comet.png"));
            });

    public CometEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new CometEntityModel(context.getPart(CometEntityModel.COMET));
    }

    @Override
    public Identifier getTexture(CometEntity entity) {
        return ID_BY_VARIANT.get(entity.getVariant());
    }

    @Override
    public void render(CometEntity entity, float y, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        matrixStack.push();

        float size = entity.getSize();
        matrixStack.scale(size, size, size);

        Vec3d velocity = entity.getVelocity();
        float yaw = getRad(velocity.x, velocity.z);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(yaw/*yaw*/));

        double zx = Math.sqrt(Math.pow(velocity.x, 2) + Math.pow(velocity.z, 2));

        float pitch = getRad(velocity.y, zx);
        matrixStack.multiply(RotationAxis.NEGATIVE_X.rotation(pitch/*pitch*/));

        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotation((float) (entity.age + tickDelta) / 20/*yaw*/));
        matrixStack.translate(0, -1.5, 0);

        VertexConsumer consumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, this.model.getLayer(this.getTexture(entity)), false, false);
        this.model.render(matrixStack, consumer, light, OverlayTexture.DEFAULT_UV);

        matrixStack.pop();

        matrixStack.pop();
    }

    private float getRad(double opposite, double adjacent) {
        double rawAngle = 0;
        if (adjacent != 0) {
            rawAngle = Math.atan(opposite / adjacent);
        } else {
            rawAngle = Math.atan(opposite);
        }

        if (adjacent >= 0) {
            return (float) rawAngle;
        } else {
            return (float) (Math.PI + rawAngle);
        }
    }
}
