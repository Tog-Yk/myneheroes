package net.togyk.myneheroes.client.render.entity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;

public class StationaryItemEntityRenderer extends ItemEntityRenderer {
    private final ItemRenderer itemRenderer;
    private final Random random = Random.create();

    public StationaryItemEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ItemEntity itemEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        ItemStack itemStack = itemEntity.getStack();
        this.random.setSeed((long)getSeed(itemStack));
        BakedModel bakedModel = this.itemRenderer.getModel(itemStack, itemEntity.getWorld(), null, itemEntity.getId());
        boolean bl = bakedModel.hasDepth();
        float k = bakedModel.getTransformation().getTransformation(ModelTransformationMode.GROUND).scale.y();
        matrixStack.translate(0.0F, 0.25F * k, 0.0F);
        renderStack(this.itemRenderer, matrixStack, vertexConsumerProvider, i, itemStack, bakedModel, bl, this.random);
        matrixStack.pop();
    }
}
