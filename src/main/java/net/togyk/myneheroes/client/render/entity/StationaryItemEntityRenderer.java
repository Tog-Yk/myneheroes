package net.togyk.myneheroes.client.render.entity;

import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.client.render.entity.states.StationaryItemEntityRendererState;
import net.togyk.myneheroes.entity.StationaryItemEntity;

public class StationaryItemEntityRenderer<T extends StationaryItemEntity, M extends EntityModel<StationaryItemEntityRendererState>> extends LivingEntityRenderer<T, StationaryItemEntityRendererState, M> {
    private final ItemModelManager itemModelManager;

    public StationaryItemEntityRenderer(EntityRendererFactory.Context context) {
        super(context, null, 0.0F);
        this.itemModelManager = context.getItemModelManager();
        this.shadowRadius = 0.15F;
        this.shadowOpacity = 0.75F;
    }

    @Override
    public StationaryItemEntityRendererState createRenderState() {
        return new StationaryItemEntityRendererState();
    }

    @Override
    public void updateRenderState(T entity, StationaryItemEntityRendererState state, float f) {
        super.updateRenderState(entity, state, f);
        state.update(entity, entity.getItem(), this.itemModelManager);
    }

    @Override
    public void render(StationaryItemEntityRendererState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraRenderState) {
        matrices.push();
        if (!state.itemRenderState.isEmpty()) {
            state.itemRenderState.render(matrices, queue, state.light, OverlayTexture.DEFAULT_UV, state.outlineColor);
        }
        matrices.pop();
    }

    @Override
    public Identifier getTexture(StationaryItemEntityRendererState state) {
        return null;
    }
}
