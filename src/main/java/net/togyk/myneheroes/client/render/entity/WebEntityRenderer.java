package net.togyk.myneheroes.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.entity.WebEntity;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class WebEntityRenderer extends ProjectileEntityRenderer<WebEntity> {
    public WebEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(WebEntity entity) {
        return Identifier.of(MyneHeroes.MOD_ID, "textures/entity/projectiles/web.png");
    }

    @Override
    public void render(WebEntity entity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
        matrixStack.push();
        float size = 0.5f;

        matrixStack.translate(0, size / 2, 0);

        Vec3d direction = entity.getVelocity();

        // Calculate rotation angles based on current velocity:
        float yaw1 = getRad(direction.x, direction.z);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(yaw1));

        double zx = Math.sqrt(Math.pow(direction.x, 2) + Math.pow(direction.z, 2));

        float pitch = getRad(-direction.y, zx);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotation(pitch));//*/
        matrixStack.push();


        matrixStack.translate(-size / 2, -size / 2, 0);

        Identifier texture = getTexture(entity);
        addQuad(matrixStack, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(texture)), 0, 0, 0, size, 0, 0, size, size, 0, 0, size, 0, light, 1, 1, 1, 1, 1, 1);

        matrixStack.pop();
        matrixStack.pop();
    }

    private void addQuad(
            MatrixStack matrixStack, VertexConsumer vertexConsumer,
            float x1, float y1, float z1,
            float x2, float y2, float z2,
            float x3, float y3, float z3,
            float x4, float y4, float z4,
            int light,
            float r, float g, float b, float a,
            float uSize, float vSize
    ) {
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f model = entry.getPositionMatrix();

        Vector3f normalVec = new Vector3f(x2 - x1, y2 - y1, z2 - z1);
        normalVec.cross(new Vector3f(x3 - x1, y3 - y1, z3 - z1));
        normalVec.normalize();

        vertexConsumer.vertex(model, x1, y1, z1).color(r, g, b, a).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, normalVec.x(), normalVec.y(), normalVec.z());
        vertexConsumer.vertex(model, x2, y2, z2).color(r, g, b, a).texture(uSize, 0).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, normalVec.x(), normalVec.y(), normalVec.z());
        vertexConsumer.vertex(model, x3, y3, z3).color(r, g, b, a).texture(uSize, vSize).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, normalVec.x(), normalVec.y(), normalVec.z());
        vertexConsumer.vertex(model, x4, y4, z4).color(r, g, b, a).texture(0, vSize).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, normalVec.x(), normalVec.y(), normalVec.z());
    }


    private float getRad(double opposite, double adjacent) {
        double rawAngle;
        if (adjacent != 0) {
            rawAngle = Math.atan(opposite / adjacent);
        } else {
            return (float) (opposite > 0 ? 0.5*Math.PI : -0.5*Math.PI);
        }

        if (adjacent < 0) {
            rawAngle += Math.PI;
        }

        return (float) rawAngle;
    }
}