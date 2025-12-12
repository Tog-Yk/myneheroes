package net.togyk.myneheroes.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.client.render.CrossBeamRenderer;
import net.togyk.myneheroes.entity.WebSwingEntity;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Converted to 1.21.x render-state style.
 */
@Environment(EnvType.CLIENT)
public class SwingWebEntityRenderer extends EntityRenderer<WebSwingEntity, SwingWebEntityRenderer.State> {
    private static final Identifier TEXTURE = Identifier.of(MyneHeroes.MOD_ID, "textures/entity/projectiles/web.png");
    private static final Identifier BEAM_TEXTURE = Identifier.of(MyneHeroes.MOD_ID, "textures/entity/projectiles/web_string.png");

    public SwingWebEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    /**
     * Called by the engine to update the state. Fill our custom state with per-entity values here.
     */
    @Override
    public void updateRenderState(WebSwingEntity entity, State state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        // store values we need in render()
        state.velocity = entity.getVelocity();
        state.owner = entity.getOwner();
        state.tickDelta = tickDelta;
        // store bounding box center for convenience (optional)
        state.entityCenter = entity.getBoundingBox().getCenter();
    }

    /**
     * Render now receives the State object (instead of the Entity). Use the stored values.
     */
    @Override
    public void render(State state, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
        // If for some reason we don't have an associated entity center/velocity, bail early
        Vec3d direction = state.velocity == null ? Vec3d.ZERO : state.velocity;
        matrixStack.push();
        float size = 0.5f;

        // Slight vertical offset like original
        matrixStack.translate(0.0D, size / 4.0D, 0.0D);

        // Calculate rotation angles based on current velocity:
        float yaw1 = getRad(direction.x, direction.z);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(yaw1));

        double zx = Math.sqrt(Math.pow(direction.x, 2) + Math.pow(direction.z, 2));
        float pitch = getRad(-direction.y, zx);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotation(pitch));

        matrixStack.push();
        matrixStack.translate(-size / 2.0D, -size / 2.0D, 0.0D);

        // Render quad using translucent entity layer (same as before)
        VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(getTexture(state)));
        addQuad(matrixStack, vc,
                0f, 0f, 0f,
                size, 0f, 0f,
                size, size, 0f,
                0f, size, 0f,
                light,
                1f, 1f, 1f, 1f,
                1f, 1f);

        matrixStack.pop();
        matrixStack.pop();

        // Render connection to the owner if present
        Entity owner = state.owner;
        if (owner != null && state.entityCenter != null) {
            Vec3d start = state.entityCenter;
            Vec3d end = owner.getBoundingBox().getCenter().add(0.1D, 0.0D, 0.1D);

            CrossBeamRenderer beamRenderer = new CrossBeamRenderer(BEAM_TEXTURE);
            beamRenderer.setSize(0.25F / 2F);
            beamRenderer.setTextureSize(0.25F / 2F);

            // note: beamRenderer.render expects matrixStack, vertexConsumers, startOffset, direction, light, scale, color
            beamRenderer.render(matrixStack, vertexConsumers, new Vec3d(0, /*half height*/ (double)(/*use entity height if you have it*/ 0.0F), 0), end.subtract(start), light, 1.0F, 0xFFFFFFFF);
        }
    }

    @Override
    protected Identifier getTexture(State state) {
        // same texture for all instances; could make per-state if you want
        return TEXTURE;
    }

    // --- helper quad renderer (kept mostly unchanged) ---
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

        vertexConsumer.vertex(model, x1, y1, z1).color(r, g, b, a).texture(0f, 0f).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, normalVec.x(), normalVec.y(), normalVec.z());
        vertexConsumer.vertex(model, x2, y2, z2).color(r, g, b, a).texture(uSize, 0f).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, normalVec.x(), normalVec.y(), normalVec.z());
        vertexConsumer.vertex(model, x3, y3, z3).color(r, g, b, a).texture(uSize, vSize).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, normalVec.x(), normalVec.y(), normalVec.z());
        vertexConsumer.vertex(model, x4, y4, z4).color(r, g, b, a).texture(0f, vSize).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, normalVec.x(), normalVec.y(), normalVec.z());
    }

    private float getRad(double opposite, double adjacent) {
        double rawAngle;
        if (adjacent != 0.0) {
            rawAngle = Math.atan(opposite / adjacent);
        } else {
            return (float) (opposite > 0.0 ? 0.5 * Math.PI : -0.5 * Math.PI);
        }

        if (adjacent < 0.0) {
            rawAngle += Math.PI;
        }

        return (float) rawAngle;
    }

    /**
     * Custom per-entity render state used to carry the fields we need from the entity into the stateless render call.
     * The engine will call updateRenderState(WebSwingEntity, State, float) on the renderer each tick before render(...) is invoked.
     */
    public static class State extends EntityRenderState {
        // fields we need at render time:
        public Vec3d velocity = Vec3d.ZERO;
        public Entity owner = null;
        public float tickDelta = 0f;
        public Vec3d entityCenter = null;
    }
}
