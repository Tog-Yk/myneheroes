package net.togyk.myneheroes.ability;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public interface RenderableAbility {
    void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider);
}
