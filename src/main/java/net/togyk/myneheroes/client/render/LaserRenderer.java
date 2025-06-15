package net.togyk.myneheroes.client.render;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.MyneHeroes;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class LaserRenderer {
    public static final Identifier TEXTURE_INNER = Identifier.of(MyneHeroes.MOD_ID, "textures/entity/projectiles/laser_inner.png");
    public static final Identifier TEXTURE_OUTER = Identifier.of(MyneHeroes.MOD_ID, "textures/entity/projectiles/laser_outer.png");

    private float size = 0.25F;

    public void render(
            MatrixStack matrixStack, VertexConsumerProvider vertexConsumers,
            Vec3d start, Vec3d end,
            float alpha, int innerColor, int outerColor
    ) {
        matrixStack.push();
        matrixStack.translate(start.getX(), start.getY(), start.getZ());

        float length = (float) Math.sqrt(Math.pow(start.x - end.x, 2) + Math.pow(start.y - end.y, 2) + Math.pow(start.z - end.z, 2)) + size;
        render(matrixStack, vertexConsumers, new Vec3d(end.x - start.x, end.y - start.y, end.z - start.z), length, alpha, innerColor, outerColor);

        matrixStack.pop();
    }



    public void render(
            MatrixStack matrixStack, VertexConsumerProvider vertexConsumers,
            Vec3d direction, float length,
            float alpha, int innerColor, int outerColor
    ) {
        matrixStack.push();

        // Calculate rotation angles based on current velocity:
        float yaw = getRad(direction.x, direction.z);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(yaw));

        double zx = Math.sqrt(Math.pow(direction.x, 2) + Math.pow(direction.z, 2));

        float pitch = getRad(-direction.y, zx);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotation(pitch/*pitch*/));

        matrixStack.push();

        matrixStack.translate(-size / 2, -size / 2, -size / 2);

        matrixStack.push();
        matrixStack.translate(size/5, size / 5, size / 5);

        matrixStack.scale( 0.6F, 0.6F, ((length - size * 0.4F) / length));

        renderBeam(matrixStack, vertexConsumers, 15728880, size, size, length, alpha, innerColor, TEXTURE_INNER);
        matrixStack.pop();

        renderBeam(matrixStack, vertexConsumers, (int) (15728880 * 0.8), size, size, length, alpha, outerColor, TEXTURE_OUTER);

        matrixStack.pop();
        matrixStack.pop();
    }

    private void renderBeam(
            MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light,
            float width, float height, float length,
            float alpha, int color, Identifier texture
    ) {
        drawBox(matrixStack, vertexConsumers, light, width, height, length, alpha, color, texture);
    }

    private void drawBox(
            MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light,
            float width, float height, float length,
            float alpha, int color, Identifier texture
    ) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(texture));

        float a = ((color >> 24 & 0xFF) / 255.0f) * alpha;
        float r = (color >> 16 & 0xFF) / 255.0f;
        float g = (color >> 8 & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;

        // Bottom face
        addQuad(matrixStack, vertexConsumer, 0, 0, 0, width, 0, 0, width, 0, length, 0, 0, length, light, r, g, b, a, (float) (width / 0.25), (float) (length / 0.25));
        // Top face
        addQuad(matrixStack, vertexConsumer, 0, height, 0, width, height, 0, width, height, length, 0, height, length, light, r, g, b, a, (float) (width / 0.25), (float) (length / 0.25));
        // Front
        addQuad(matrixStack, vertexConsumer, 0, 0, length, width, 0, length, width, height, length, 0, height, length, light, r, g, b, a, (float) (width / 0.25), (float) (width / 0.25));
        // Back
        addQuad(matrixStack, vertexConsumer, 0, 0, 0, 0, height, 0, width, height, 0, width, 0, 0, light, r, g, b, a, (float) (width / 0.25), (float) (width / 0.25));
        // Left
        addQuad(matrixStack, vertexConsumer, 0, 0, 0, 0, 0, length, 0, height, length, 0, height, 0, light, r, g, b, a, (float) (width / 0.25), (float) (width / 0.25));
        // Right
        addQuad(matrixStack, vertexConsumer, width, 0, 0, width, height, 0, width, height, length, width, 0, length, light, r, g, b, a, (float) (width / 0.25), (float) (width / 0.25));
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

    public void setSize(float size) {
        this.size = size;
    }

    public float getSize() {
        return size;
    }
}
