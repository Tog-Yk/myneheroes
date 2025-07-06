package net.togyk.myneheroes.client.render.ability;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.togyk.myneheroes.ability.Ability;

@Environment(EnvType.CLIENT)
public abstract class AbilityRenderer<T extends Ability> {
    public abstract void render(T ability, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, float tickDelta);
}
