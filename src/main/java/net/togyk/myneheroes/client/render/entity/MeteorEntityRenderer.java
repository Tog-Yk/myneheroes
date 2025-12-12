package net.togyk.myneheroes.client.render.entity;

import com.google.common.collect.Maps;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.client.render.entity.states.MeteorEntityRendererState;
import net.togyk.myneheroes.entity.MeteorEntity;
import net.togyk.myneheroes.entity.MeteorVariant;

import java.util.Map;

public class MeteorEntityRenderer extends ProjectileEntityRenderer<MeteorEntity, MeteorEntityRendererState> {
    protected MeteorEntityModel model;
    private static final Map<MeteorVariant, Identifier> ID_BY_VARIANT =
            Util.make(Maps.newEnumMap(MeteorVariant.class), map -> {
                map.put(MeteorVariant.DEFAULT,
                        Identifier.of(MyneHeroes.MOD_ID, "textures/entity/meteor/meteor.png"));
                map.put(MeteorVariant.SCULK,
                        Identifier.of(MyneHeroes.MOD_ID, "textures/entity/meteor/sculk_meteor.png"));
                map.put(MeteorVariant.KRYPTONITE,
                        Identifier.of(MyneHeroes.MOD_ID, "textures/entity/meteor/kryptonite_meteor.png"));
                map.put(MeteorVariant.VIBRANIUM,
                        Identifier.of(MyneHeroes.MOD_ID, "textures/entity/meteor/vibranium_meteor.png"));
            });

    public MeteorEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new MeteorEntityModel(context.getPart(MeteorEntityModel.METEOR));
    }

    @Override
    public MeteorEntityRendererState createRenderState() {
        return null;
    }

    @Override
    protected Identifier getTexture(MeteorEntityRendererState state) {
        return ID_BY_VARIANT.get(state.variant);
    }
    @Override
    public void render(MeteorEntityRendererState meteorState, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, CameraRenderState cameraRenderState) {

        matrixStack.push();

        float size = meteorState.size;
        matrixStack.scale(size, size, size);

        Vec3d velocity = meteorState.velocity;
        float yaw = getRad(velocity.x, velocity.z);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(yaw/*yaw*/));

        double zx = Math.sqrt(Math.pow(velocity.x, 2) + Math.pow(velocity.z, 2));

        float pitch = getRad(velocity.y, zx);
        matrixStack.multiply(RotationAxis.NEGATIVE_X.rotation(pitch/*pitch*/));

        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotation((float) (meteorState.age) / 20/*yaw*/));
        matrixStack.translate(0, -1.5, 0);

        super.render(meteorState, matrixStack, orderedRenderCommandQueue, cameraRenderState);

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
