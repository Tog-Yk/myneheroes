package net.togyk.myneheroes.client.render.entity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.entity.CometEntity;

public class CometEntityModel extends EntityModel<CometEntity> {
    public static final EntityModelLayer COMET = new EntityModelLayer(Identifier.of(MyneHeroes.MOD_ID, "comet"), "main");
    private final ModelPart comet;

    public CometEntityModel(ModelPart root) {
        this.comet = root.getChild("comet");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData comet = modelPartData.addChild("comet", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData cube_r1 = comet.addChild("cube_r1", ModelPartBuilder.create().uv(0, 25).cuboid(-6.5F, -6.0F, -6.5F, 13.0F, 12.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.6109F, -0.5236F, 0.5585F));

        ModelPartData cube_r2 = comet.addChild("cube_r2", ModelPartBuilder.create().uv(0, 50).cuboid(-5.5F, -5.0F, -5.5F, 11.0F, 10.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.6981F, 0.6981F, 0.3491F));

        ModelPartData cube_r3 = comet.addChild("cube_r3", ModelPartBuilder.create().uv(0, 0).cuboid(-7.0F, -6.0F, -6.0F, 13.0F, 12.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.6109F, -0.6981F, 0.1745F));
        return TexturedModelData.of(modelData, 128, 128);
    }
    @Override
    public void setAngles(CometEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        comet.render(matrices, vertices, light, overlay, color);
    }
}
