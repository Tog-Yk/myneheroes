package net.togyk.myneheroes.client.render.entity;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.entity.StationaryItemEntity;

public class StationaryItemEntityRenderer<T extends StationaryItemEntity, M extends EntityModel<T>> extends LivingEntityRenderer<T, M> {
    private final ItemRenderer itemRenderer;

    public StationaryItemEntityRenderer(EntityRendererFactory.Context context) {
        super(context, null, 0.0F);
        this.itemRenderer = context.getItemRenderer();
    }


    @Override
    public Identifier getTexture(T entity) {
        return null;
    }

    @Override
    public void render(T stationaryItemEntity, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        ItemStack stack = stationaryItemEntity.getEquippedStack(null);
        BakedModel model = this.itemRenderer.getModel(stack, stationaryItemEntity.getWorld(), null, stationaryItemEntity.getId());
        float k = model.getTransformation().getTransformation(ModelTransformationMode.GROUND).scale.y();
        matrices.translate(0.0F, 0.25F * k, 0.0F);
        itemRenderer.renderItem(stack, ModelTransformationMode.GROUND, false, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, model);
        matrices.pop();
    }
}
