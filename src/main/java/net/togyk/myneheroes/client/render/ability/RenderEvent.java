package net.togyk.myneheroes.client.render.ability;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.util.PlayerAbilities;

import java.util.List;

@Environment(EnvType.CLIENT)
public class RenderEvent {
    public static void registerRenderEvent() {
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            Camera camera = context.camera();
            Vec3d camPos = camera.getPos();

            MatrixStack matrices = context.matrixStack();

            matrices.push();
            matrices.translate(-camPos.x, -camPos.y, -camPos.z);

            if (context.world() != null) {
                for (PlayerEntity player : context.world().getPlayers()) {
                    List<Ability> abilities = ((PlayerAbilities) player).myneheroes$getAbilities();
                    for (Ability ability : abilities) {
                        if (AbilityRendererRegistry.get(ability) instanceof AbilityRenderer<?> renderer) {
                            renderAbility(ability, renderer, context.matrixStack(), context.consumers(), context.tickCounter().getTickDelta(false));
                        }
                    }
                }
            }
            matrices.pop();
        });
    }

    @SuppressWarnings("unchecked")
    private static <T extends Ability> void renderAbility(Ability ability, AbilityRenderer<?> renderer, MatrixStack matrices, VertexConsumerProvider consumers, float tickDelta) {
        try {
            ((AbilityRenderer<T>) renderer).render((T) ability, matrices, consumers, tickDelta);
        } catch (ClassCastException e) {
            MyneHeroes.LOGGER.error("Renderer and ability types mismatch for ability: {}", ability.getId(), e);
        }
    }
}
