package net.togyk.myneheroes.client.render.entity;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.togyk.myneheroes.Item.custom.SpinInfo;
import net.togyk.myneheroes.Item.custom.ThrowableItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.entity.ThrownItemEntity;
import org.joml.Vector3f;

import static net.minecraft.client.render.entity.ItemEntityRenderer.renderStack;


public class ThrownItemEntityRenderer extends ProjectileEntityRenderer<ThrownItemEntity> {
    public static final Identifier TEXTURE = Identifier.of(MyneHeroes.MOD_ID, "textures/entity/projectiles/thrown_item.png");
    private final ItemRenderer itemRenderer;
    private final Random random = Random.create();

    public ThrownItemEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public Identifier getTexture(ThrownItemEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(ThrownItemEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        Item item = entity.getDisplayStack().getItem();

        // Calculate rotation angles based on current velocity:
        if (item instanceof ThrowableItem throwable) {
            matrixStack.translate(0.0F, 0, 0.0F);

            //groundOffset
            if (entity.landed() && entity.getGroundedOffset() != null) {
                Vector3f groundOffset = entity.getGroundedOffset();
                matrixStack.translate(groundOffset.x, groundOffset.y, groundOffset.z);
            }

            if (throwable.getThrownAnimationType() != SpinInfo.AnimationType.FACE) {
                Vec3d velocity = entity.getVelocity();

                float yaw = getRad(velocity.x, velocity.z);
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(yaw/*yaw*/));

                double zx = Math.sqrt(Math.pow(velocity.x, 2) + Math.pow(velocity.z, 2));

                float followStrength = 1;
                if (zx == 0 && !entity.landed()) {
                    followStrength = (float) (Math.sqrt(Math.min(throwable.getFollowAnimationMaxSpeed(), velocity.lengthSquared()) / throwable.getFollowAnimationMaxSpeed()));
                }

                float pitch = getRad(velocity.y, zx);
                matrixStack.multiply(RotationAxis.NEGATIVE_X.rotation(pitch * followStrength/*pitch*/));
            }

            switch (throwable.getThrownFollowDirection()) {

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
            if (!entity.landed()) {
                switch (throwable.getThrownSpinType()) {
                    case POS_PITCH -> matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotation((float) (entity.age / Math.PI)));
                    case NEG_PITCH -> matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float) (entity.age / Math.PI)));
                    case POS_YAW -> matrixStack.multiply(RotationAxis.POSITIVE_X.rotation((float) (entity.age / Math.PI)));
                    case NEG_YAW -> matrixStack.multiply(RotationAxis.NEGATIVE_X.rotation((float) (entity.age / Math.PI)));
                    case POS_ROLL -> matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotation((float) (entity.age / Math.PI)));
                    case NEG_ROLL -> matrixStack.multiply(RotationAxis.POSITIVE_Z.rotation((float) (entity.age / Math.PI)));
                }
            }

            ItemStack itemStack = entity.getDisplayStack();
            this.random.setSeed(getSeed(itemStack));
            BakedModel bakedModel = this.itemRenderer.getModel(itemStack, entity.getWorld(), null, entity.getId());
            boolean bl = bakedModel.hasDepth();
            BlockPos lightPos = entity.getBlockPos();
            if (entity.getGroundedOffset().y < 0) {
                lightPos = lightPos.down();
            }
            World world = entity.getWorld();
            int light = LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, lightPos), world.getLightLevel(LightType.SKY, lightPos));
            renderStack(this.itemRenderer, matrixStack, vertexConsumerProvider, light, itemStack, bakedModel, bl, this.random);

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
