package net.togyk.myneheroes.client.render.ability;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.togyk.myneheroes.ability.detailed.LaserEyesAbility;
import net.togyk.myneheroes.client.render.LaserRenderer;

@Environment(EnvType.CLIENT)
public class LaserEyesAbilityRenderer<T extends LaserEyesAbility> extends AbilityRenderer<T> {
    @Override
    public void render(T ability, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, float tickDelta) {
        LaserRenderer laserRenderer = ability.getLaserRenderer();
        if (ability.getStart(tickDelta) != null && ability.getEnd(tickDelta) != null) {
            laserRenderer.render(matrixStack, vertexConsumerProvider, ability.getStart(tickDelta), ability.getEnd(tickDelta), 1.0F, ability.getInnerColor(), ability.getColor());
        }
    }
}
