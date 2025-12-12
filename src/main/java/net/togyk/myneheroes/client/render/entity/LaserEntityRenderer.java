package net.togyk.myneheroes.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.client.render.LaserRenderer;
import net.togyk.myneheroes.client.render.entity.states.LaserEntityRendererState;
import net.togyk.myneheroes.entity.LaserEntity;

@Environment(EnvType.CLIENT)
public class LaserEntityRenderer extends ProjectileEntityRenderer<LaserEntity, LaserEntityRendererState> {
    public LaserEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(LaserEntityRendererState state) {
        return Identifier.of(MyneHeroes.MOD_ID, "textures/entity/projectiles/laser_inner.png");
    }

    @Override
    public LaserEntityRendererState createRenderState() {
        return new LaserEntityRendererState();
    }

    @Override
    public void updateRenderState(LaserEntity entity, LaserEntityRendererState state, float f) {
        super.updateRenderState(entity, state, f);
        state.velocity = entity.getVelocity();
        state.innerColor = entity.getInnerColor();
        state.color = entity.getColor();
    }

    @Override
    public void render(LaserEntityRendererState state, MatrixStack matrixStack, OrderedRenderCommandQueue queue, CameraRenderState cameraRenderState) {
        matrixStack.push();
        matrixStack.translate(0, state.height / 2, 0);
        LaserRenderer renderer = new LaserRenderer();
        //todo fix vertex consumers
        //renderer.render(matrixStack, , state.velocity, 0.25F, 1.0F, state.innerColor, state.color);

        matrixStack.pop();
    }
}