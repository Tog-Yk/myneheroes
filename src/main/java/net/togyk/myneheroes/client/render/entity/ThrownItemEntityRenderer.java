package net.togyk.myneheroes.client.render.entity;

import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LightType;
import net.togyk.myneheroes.Item.custom.ThrowableItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.client.render.entity.states.ThrownItemEntityRendererState;
import net.togyk.myneheroes.entity.ThrownItemEntity;
import org.joml.Vector3f;


public class ThrownItemEntityRenderer extends ProjectileEntityRenderer<ThrownItemEntity, ThrownItemEntityRendererState> {
    public static final Identifier TEXTURE = Identifier.of(MyneHeroes.MOD_ID, "textures/entity/projectiles/thrown_item.png");
    private final ItemModelManager itemModelManager;
    private final Random random = Random.create();

    public ThrownItemEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemModelManager = context.getItemModelManager();
    }

    @Override
    public Identifier getTexture(ThrownItemEntityRendererState state) {
        return TEXTURE;
    }

    @Override
    public ThrownItemEntityRendererState createRenderState() {
        return new ThrownItemEntityRendererState();
    }

    @Override
    public void updateRenderState(ThrownItemEntity entity, ThrownItemEntityRendererState state, float f) {
        super.updateRenderState(entity, state, f);
        state.velocity = entity.getVelocity();
        state.groundedOffset = entity.getGroundedOffset();
        state.blockPos = entity.getBlockPos();
        state.landed = entity.landed();

        BlockPos lightPos = state.blockPos;
        if (state.groundedOffset.y < 0) {
            lightPos = lightPos.down();
        }
        state.light = LightmapTextureManager.pack(entity.getEntityWorld().getLightLevel(LightType.BLOCK, lightPos), entity.getEntityWorld().getLightLevel(LightType.SKY, lightPos));
        state.update(entity, entity.getDisplayStack(), this.itemModelManager);
    }

    @Override
    public void render(ThrownItemEntityRendererState state, MatrixStack matrixStack, OrderedRenderCommandQueue queue, CameraRenderState cameraRenderState) {
        matrixStack.push();
        Item item = state.displayStack.getItem();

        // Calculate rotation angles based on current velocity:
        if (item instanceof ThrowableItem throwable) {
            matrixStack.translate(0.0F, 0, 0.0F);

            //groundOffset
            if (state.landed && state.groundedOffset != null) {
                Vector3f groundOffset = state.groundedOffset;
                matrixStack.translate(groundOffset.x, groundOffset.y, groundOffset.z);
            }

            if (throwable.getAnimationType() != ThrowableItem.AnimationType.FACE) {
                Vec3d velocity = state.velocity;

                float yaw = getRad(velocity.x, velocity.z);
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(yaw/*yaw*/));

                double zx = Math.sqrt(Math.pow(velocity.x, 2) + Math.pow(velocity.z, 2));

                float followStrength = 1;
                if (zx == 0 && !state.landed) {
                    followStrength = (float) (Math.sqrt(Math.min(throwable.getFollowAnimationMaxSpeed(), velocity.lengthSquared()) / throwable.getFollowAnimationMaxSpeed()));
                }

                float pitch = getRad(velocity.y, zx);
                matrixStack.multiply(RotationAxis.NEGATIVE_X.rotation(pitch * followStrength/*pitch*/));
            }

            switch (throwable.getFollowDirection()) {

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

            matrixStack.push();
            if (!state.landed) {
                switch (throwable.getSpinType()) {
                    case POS_PITCH -> matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotation((float) (state.age / Math.PI)));
                    case NEG_PITCH -> matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float) (state.age / Math.PI)));
                    case POS_YAW -> matrixStack.multiply(RotationAxis.POSITIVE_X.rotation((float) (state.age / Math.PI)));
                    case NEG_YAW -> matrixStack.multiply(RotationAxis.NEGATIVE_X.rotation((float) (state.age / Math.PI)));
                    case POS_ROLL -> matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotation((float) (state.age / Math.PI)));
                    case NEG_ROLL -> matrixStack.multiply(RotationAxis.POSITIVE_Z.rotation((float) (state.age / Math.PI)));
                }
            }

            state.itemRenderState.render(matrixStack, queue, state.light, OverlayTexture.DEFAULT_UV, state.outlineColor);

            matrixStack.pop();

            matrixStack.pop();
        }
    }

    public static int getSeed(ItemStack stack) {
        return stack.isEmpty() ? 187 : Item.getRawId(stack.getItem()) + stack.getDamage();
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
