package net.togyk.myneheroes.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.client.render.ability.AbilityRenderer;
import net.togyk.myneheroes.client.render.ability.AbilityRendererRegistry;
import net.togyk.myneheroes.networking.PlayerJumpWatcherPayload;
import net.togyk.myneheroes.util.PlayerAbilities;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ModClientEvents {
    public static void registerEvents() {
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


        MyneHeroes.LOGGER.info("registering Client Events for" + MyneHeroes.MOD_ID);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                KeyBinding jumpKey = client.options.jumpKey;
                boolean isHoldingJumping = jumpKey.isPressed();

                ((PlayerAbilities) client.player).myneheroes$setIsHoldingJump(isHoldingJumping);
                PlayerJumpWatcherPayload payload = new PlayerJumpWatcherPayload(isHoldingJumping);
                ClientPlayNetworking.send(payload);
            }
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
