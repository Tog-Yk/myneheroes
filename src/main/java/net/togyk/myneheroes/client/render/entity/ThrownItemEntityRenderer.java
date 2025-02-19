package net.togyk.myneheroes.client.render.entity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.entity.ThrownItemEntity;

import static net.minecraft.client.render.entity.ItemEntityRenderer.renderStack;


public class ThrownItemEntityRenderer extends ProjectileEntityRenderer<ThrownItemEntity> {
    public static final Identifier TEXTURE = Identifier.of(MyneHeroes.MOD_ID, "textures/entity/projectiles/thrown_item.png");
    private final ItemRenderer itemRenderer;
    private final Random random = Random.create();

    public ThrownItemEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public Identifier getTexture(ThrownItemEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(ThrownItemEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();

        ItemStack itemStack = entity.getItemStack();
        MyneHeroes.LOGGER.info(String.valueOf(itemStack));
        this.random.setSeed((long) getSeed(itemStack));
        BakedModel bakedModel = this.itemRenderer.getModel(itemStack, entity.getWorld(), null, entity.getId());
        boolean bl = bakedModel.hasDepth();
        float k = bakedModel.getTransformation().getTransformation(ModelTransformationMode.GROUND).scale.y();
        matrixStack.translate(0.0F, 0.25F * k, 0.0F);
        renderStack(this.itemRenderer, matrixStack, vertexConsumerProvider, i, itemStack, bakedModel, bl, this.random);

        matrixStack.pop();
    }

    public static int getSeed(ItemStack stack) {
        return stack.isEmpty() ? 187 : Item.getRawId(stack.getItem()) + stack.getDamage();
    }
}
